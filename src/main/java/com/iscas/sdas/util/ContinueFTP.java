package com.iscas.sdas.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;


  
/**
 * 支持断点续传的FTP实用类  
 * @author BenZhou  http://www.bt285.cn
 * @version 0.1 实现基本断点上传下载  
 * @version 0.2 实现上传下载进度汇报  
 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持  
 */  
public class ContinueFTP {   
    public FTPClient ftpClient = new FTPClient();   
       
    public ContinueFTP(){   
        //设置将过程中使用到的命令输出到控制台   
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));   
    }   
       
    /** *//**  
     * 连接到FTP服务器  
     * @param hostname 主机名  
     * @param port 端口  
     * @param username 用户名  
     * @param password 密码  
     * @return 是否连接成功  
     * @throws IOException  
     */  
    public boolean connect(String hostname,int port,String username,String password) throws IOException{   
        ftpClient.connect(hostname, port);   
        ftpClient.setControlEncoding("UTF-8");   
        if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){   
            if(ftpClient.login(username, password)){   
                return true;   
            }   
        }   
        disconnect();   
        return false;   
    }   
       
    /** *//**  
     * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报  
     * @param remote 远程文件路径  
     * @param local 本地文件路径  
     * @return 上传的状态  
     * @throws IOException  
     */  
    public FTPStatus download(String remote,String local) throws IOException{   
        //设置被动模式   
        ftpClient.enterLocalPassiveMode();   
        //设置以二进制方式传输   
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);   
        FTPStatus result;   
           
        //检查远程文件是否存在   
        FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("GBK"),"UTF-8"));   
        if(files.length != 1){   
            System.out.println("远程文件不存在");   
            return FTPStatus.Remote_File_Noexist;   
        }   
           
        long lRemoteSize = files[0].getSize();   
        File f = new File(local);   
        //本地存在文件，进行断点下载   
        if(f.exists()){   
            long localSize = f.length();   
            //判断本地文件大小是否大于远程文件大小   
            if(localSize >= lRemoteSize){   
                System.out.println("本地文件大于远程文件，下载中止");   
                return FTPStatus.Local_Bigger_Remote;   
            }   
               
            //进行断点续传，并记录状态   
            FileOutputStream out = new FileOutputStream(f,true);   
            ftpClient.setRestartOffset(localSize);   
            InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"),"UTF-8"));   
            byte[] bytes = new byte[1024];   
            long step = lRemoteSize /100;   
            long process=localSize /step;   
            int c;   
            while((c = in.read(bytes))!= -1){   
                out.write(bytes,0,c);   
                localSize+=c;   
                long nowProcess = localSize /step;   
                if(nowProcess > process){   
                    process = nowProcess;   
                    if(process % 10 == 0)   
                        System.out.println("下载进度："+process);   
                    //TODO 更新文件下载进度,值存放在process变量中   
                }   
            }   
            in.close();   
            out.close();   
            boolean isDo = ftpClient.completePendingCommand();   
            if(isDo){   
                result = FTPStatus.Download_From_Break_Success;   
            }else {   
                result = FTPStatus.Download_From_Break_Failed;   
            }   
        }else {   
            OutputStream out = new FileOutputStream(f);   
            InputStream in= ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"),"UTF-8"));   
            byte[] bytes = new byte[1024];   
            long step = lRemoteSize /100;   
            long process=0;   
            long localSize = 0L;   
            int c;   
            while((c = in.read(bytes))!= -1){   
                out.write(bytes, 0, c);   
                localSize+=c;   
                long nowProcess = localSize /step;   
                if(nowProcess > process){   
                    process = nowProcess;   
                    if(process % 10 == 0)   
                        System.out.println("下载进度："+process);   
                    //TODO 更新文件下载进度,值存放在process变量中   
                }   
            }   
            in.close();   
            out.close();   
            boolean upNewStatus = ftpClient.completePendingCommand();   
            if(upNewStatus){   
                result = FTPStatus.Download_New_Success;   
            }else {   
                result = FTPStatus.Download_New_Failed;   
            }   
        }   
        return result;   
    }   
       
    /** *//**  
     * 上传文件到FTP服务器，支持断点续传  
     * @param local 本地文件名称，绝对路径  
     * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext或是 http://www.guihua.org /subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构  
     * @return 上传结果  
     * @throws IOException  
     */  
    public FTPStatus upload(MultipartFile multlocal,String remote) throws IOException{   
        //设置PassiveMode传输   
        ftpClient.enterLocalPassiveMode();   
        //设置以二进制流的方式传输   
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);   
        ftpClient.setControlEncoding("UTF-8");   
        FTPStatus result;   
        //对远程目录的处理   
        String remoteFileName = remote;   
        //检查远程是否存在文件   
        FTPFile[] files = ftpClient.listFiles(remoteFileName);   
        if(files.length == 1){   
            long remoteSize = files[0].getSize();   
            if(remoteSize==multlocal.getSize()){   
                return FTPStatus.File_Exits;   
            }else if(remoteSize > multlocal.getSize()){   
                return FTPStatus.Remote_Bigger_Local;   
            }                  
            //尝试移动文件内读取指针,实现断点续传   
            result = uploadFile(remoteFileName, multlocal, ftpClient,remoteSize);            
            //如果断点续传没有成功，则删除服务器上文件，重新上传   
            if(result == FTPStatus.Upload_From_Break_Failed){   
                if(!ftpClient.deleteFile(remoteFileName)){   
                    return FTPStatus.Delete_Remote_Faild;   
                }   
                result = uploadFile(remoteFileName, multlocal, ftpClient,0);   
            }   
        }else {   
            result = uploadFile(remoteFileName, multlocal, ftpClient,0); 
        }   
        return result;   
    }   
    /** *//**  
     * 断开与远程服务器的连接  
     * @throws IOException  
     */  
    public void disconnect() throws IOException{   
        if(ftpClient.isConnected()){   
            ftpClient.disconnect();   
        }   
    }   
       
    /** *//**  
     * 递归创建远程服务器目录  
     * @param remote 远程服务器文件绝对路径  
     * @param ftpClient FTPClient对象  
     * @return 目录创建是否成功  
     * @throws IOException  
     */  
    public FTPStatus CreateDirecroty(String remote,FTPClient ftpClient) throws IOException{   
    	FTPStatus status = FTPStatus.Create_Directory_Success;   
        String directory = remote.substring(0,remote.lastIndexOf("/")+1);   
        if(!directory.equalsIgnoreCase("/")&&!ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"),"iso-8859-1"))){   
            //如果远程目录不存在，则递归创建远程服务器目录   
            int start=0;   
            int end = 0;   
            if(directory.startsWith("/")){   
                start = 1;   
            }else{   
                start = 0;   
            }   
            end = directory.indexOf("/",start);   
            while(true){   
                String subDirectory = new String(remote.substring(start,end).getBytes("GBK"),"iso-8859-1");   
                if(!ftpClient.changeWorkingDirectory(subDirectory)){   
                    if(ftpClient.makeDirectory(subDirectory)){   
                        ftpClient.changeWorkingDirectory(subDirectory);   
                    }else {   
                        System.out.println("创建目录失败");   
                        return FTPStatus.Create_Directory_Fail;   
                    }   
                }   
                   
                start = end + 1;   
                end = directory.indexOf("/",start);   
                   
                //检查所有目录是否创建完毕   
                if(end <= start){   
                    break;   
                }   
            }   
        }   
        return status;   
    }
    /**
     * 文件上传，新文件和断点续传
     * @param remoteFile 目标存储文件全路径
     * @param multipartFile 源文件
     * @param ftpClient 
     * @param remoteSize 目标文件已有大小
     * @return
     * @throws IOException
     */
    public FTPStatus uploadFile(String remoteFile,MultipartFile multipartFile,FTPClient ftpClient,long remoteSize) throws IOException{   
    	FTPStatus status=null;   
        //显示进度的上传   
        //long step = multipartFile.getSize() / 100;   
        //long process = 0;   
        //long localreadbytes = 0L;   
        //RandomAccessFile raf = new RandomAccessFile(localFile,"r");   
        InputStream in = multipartFile.getInputStream(); 
        //OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"),"UTF-8"));   
        //断点续传   
        if(remoteSize>0){   
            ftpClient.setRestartOffset(remoteSize);   
            //process = remoteSize /step;  
            in.skip(remoteSize);
            //raf.seek(remoteSize);   
            //localreadbytes = remoteSize;   
        }   
        /*byte[] bytes = new byte[1024];   
        int c;  
        while((c = in.read(bytes))!= -1){   
            out.write(bytes,0,c);   
            localreadbytes+=c;   
            if(localreadbytes / step != process){   
                process = localreadbytes / step;   
                System.out.println("上传进度:" + process);   
                //TODO 汇报上传状态   
            }   
        }   
        out.flush();   
        in.close();   
        out.close();*/  
        //System.out.println("存储文件名："+new String(remoteFile.getBytes("UTF-8"),"UTF-8"));
        boolean result = ftpClient.storeFile(remoteFile, in);
        //boolean result =ftpClient.completePendingCommand();   
        if(remoteSize > 0){   
            status = result?FTPStatus.Upload_From_Break_Success:FTPStatus.Upload_From_Break_Failed;   
        }else {   
            status = result?FTPStatus.Upload_New_File_Success:FTPStatus.Upload_New_File_Failed;   
        }   
        return status;   
    }   
       
    public static void main(String[] args) {   
        ContinueFTP myFtp = new ContinueFTP();   
        try {   
            myFtp.connect("192.168.21.181", 21, "nid", "123");   
//          myFtp.ftpClient.makeDirectory(new String("电视剧".getBytes("GBK"),"iso-8859-1"));   
//          myFtp.ftpClient.changeWorkingDirectory(new String("电视剧".getBytes("GBK"),"iso-8859-1"));   
//          myFtp.ftpClient.makeDirectory(new String("走西口".getBytes("GBK"),"iso-8859-1"));   
//          System.out.println(myFtp.upload("http://www.5a520.cn /yw.flv", "/yw.flv",5));   
//          System.out.println(myFtp.upload("http://www.5a520.cn /走西口24.mp4","/央视走西口/新浪网/走西口24.mp4"));   
            System.out.println(myFtp.download("/央视走西口/新浪网/走西口24.mp4", "E:\\走西口242.mp4"));   
            myFtp.disconnect();   
        } catch (IOException e) {   
            System.out.println("连接FTP出错："+e.getMessage());   
        }   
    }   
}  