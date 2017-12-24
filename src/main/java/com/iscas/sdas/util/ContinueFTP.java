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
 * 
 * @author BenZhou http://www.bt285.cn
 * @version 0.1 实现基本断点上传下载
 * @version 0.2 实现上传下载进度汇报
 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持
 */
public class ContinueFTP{

	/** 本地字符编码 */
	private static String LOCAL_CHARSET = "GBK";

	// FTP协议里面，规定文件名编码为iso-8859-1
	private static String SERVER_CHARSET = "ISO-8859-1";
	
	private static final String ROOT_PATH = "/home/hadoop/systempdata/";

	public FTPClient ftpClient = new FTPClient();

	public ContinueFTP() {
		// 设置将过程中使用到的命令输出到控制台
		this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}
	// 上传/下载进度
	private int progress=0;

	// 获取当前进度
	public int getProgress() {
		return progress;
	}

	// 设置当前进度
	private void setProgress(double progress) {
		this.progress = (int) progress;
	}

	/** */
	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	public boolean connect(String hostname, int port, String username, String password) throws IOException {
		ftpClient.connect(hostname, port);
		System.out.println("2.1...连接ftp！");
		//ftpClient.setControlEncoding("UTF-8");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				System.out.println("2.2...登录到ftp！");
				if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
						"OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
						LOCAL_CHARSET = "UTF-8";
						System.out.println("2.3...设置编码！");
				}
				ftpClient.setControlEncoding(LOCAL_CHARSET);			
				// 设置被动模式
				ftpClient.enterLocalPassiveMode();
				// 设置以二进制方式传输
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				return true;
			}
		}
		disconnect();
		return false;
	}

	/** */
	/**
	 * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
	 * 
	 * @param remote
	 *            远程文件路径
	 * @param local
	 *            本地文件路径
	 * @return 上传的状态
	 * @throws IOException
	 */
	public FTPStatus download(String remote, String local) throws IOException {
		FTPStatus result;

		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("GBK"), "UTF-8"));
		if (files.length != 1) {
			System.out.println("远程文件不存在");
			return FTPStatus.Remote_File_Noexist;
		}

		long lRemoteSize = files[0].getSize();
		File f = new File(local);
		// 本地存在文件，进行断点下载
		if (f.exists()) {
			long localSize = f.length();
			// 判断本地文件大小是否大于远程文件大小
			if (localSize >= lRemoteSize) {
				System.out.println("本地文件大于远程文件，下载中止");
				return FTPStatus.Local_Bigger_Remote;
			}

			// 进行断点续传，并记录状态
			FileOutputStream out = new FileOutputStream(f, true);
			ftpClient.setRestartOffset(localSize);
			InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"), "UTF-8"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			long process = localSize / step;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						System.out.println("下载进度：" + process);
					// TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean isDo = ftpClient.completePendingCommand();
			if (isDo) {
				result = FTPStatus.Download_From_Break_Success;
			} else {
				result = FTPStatus.Download_From_Break_Failed;
			}
		} else {
			OutputStream out = new FileOutputStream(f);
			InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"), "UTF-8"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			long process = 0;
			long localSize = 0L;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						System.out.println("下载进度：" + process);
					// TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean upNewStatus = ftpClient.completePendingCommand();
			if (upNewStatus) {
				result = FTPStatus.Download_New_Success;
			} else {
				result = FTPStatus.Download_New_Failed;
			}
		}
		return result;
	}

	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param multlocal
	 *            源文件
	 * @param remote
	 *            远程文件名
	 * @throws IOException
	 */
	public FTPStatus upload(MultipartFile multlocal, String remote, String time) throws IOException {

		FTPStatus result;
		// 远程文件名
		String remoteFileName = remote;
		// 递归生成远程目录
		System.out.println("4.....创建指定目录！前");
		String remotePath = CreateDirecroty(ftpClient, time);
		System.out.println("4.....创建指定目录！后");
		if (!FTPStatus.Create_Directory_Fail.toString().equals(remotePath)) {
			// 检查远程是否存在文件  new String(fileName.getBytes(LOCAL_CHARSET),SERVER_CHARSET)
			FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes(LOCAL_CHARSET),SERVER_CHARSET));
			if (files.length == 1) {
				long remoteSize = files[0].getSize();
				if (remoteSize == multlocal.getSize()) {
					return FTPStatus.File_Exits;
				} else if (remoteSize > multlocal.getSize()) {
					return FTPStatus.Remote_Bigger_Local;
				}
				// 尝试移动文件内读取指针,实现断点续传
				System.out.println("5.....开始上传！");
				result = uploadFile(remoteFileName, multlocal, ftpClient, remoteSize);
				// 如果断点续传没有成功，则删除服务器上文件，重新上传
				if (result == FTPStatus.Upload_From_Break_Failed) {
					if (!ftpClient.deleteFile(remoteFileName)) {
						return FTPStatus.Delete_Remote_Faild;
					}
					result = uploadFile(remoteFileName, multlocal, ftpClient, 0);
				}
			} else {
				result = uploadFile(remoteFileName, multlocal, ftpClient, 0);
			}
		} else {
			return FTPStatus.Create_Directory_Fail;
		}
		return result;
	}

	/** */
	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/** */
	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote
	 *            远程服务器文件名
	 * @param ftpClient
	 *            FTPClient对象
	 * @param yyyyMMdd
	 *            时间目录
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public String CreateDirecroty(FTPClient ftpClient, String yyyyMMdd) throws IOException {
		FTPStatus status = FTPStatus.Create_Directory_Success;
		String yearDir = yyyyMMdd.substring(0, 4);
		String monthDir = yyyyMMdd.substring(4, 6);
		String dayDir = yyyyMMdd.substring(6);
		String directory = yearDir + "/" + monthDir + "/" + dayDir + "/";
		if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(directory)) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = directory.substring(start, end);
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录失败");
						return FTPStatus.Create_Directory_Fail.toString();
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status.toString();
	}

	/**
	 * 文件上传，新文件和断点续传
	 * 
	 * @param remoteFile
	 *            目标存储文件全路径
	 * @param multipartFile
	 *            源文件
	 * @param ftpClient
	 * @param remoteSize
	 *            目标文件已有大小
	 * @return
	 * @throws IOException
	 */
	public FTPStatus uploadFile(String remoteFile, MultipartFile multipartFile, FTPClient ftpClient, long remoteSize)
			throws IOException {
		FTPStatus status = null;
		// 显示进度的上传
		// long step = multipartFile.getSize() / 100;
		double allbytes = multipartFile.getSize();
		double process = 0;
		double localreadbytes = 0L;
		InputStream in = multipartFile.getInputStream();
		OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes(LOCAL_CHARSET),SERVER_CHARSET));
		System.out.println("6.....进入上传函数！");
		// 断点续传
		if (remoteSize > 0) {
			ftpClient.setRestartOffset(remoteSize);
			process = (remoteSize / allbytes) * 100;
			in.skip(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] bytes = new byte[1024 * 1024];
		int c;
		System.out.println("7.....开始写数据！");
		while ((c = in.read(bytes)) != -1) {
			out.write(bytes, 0, c);
			localreadbytes += c;
			double temp = localreadbytes / allbytes;
			if (temp * 100 != process) {
				process = (localreadbytes / allbytes) * 100;
				System.out.println(process);
				// TODO 汇报上传状态
				
				setProgress(process);
				Constraints.setFtp_upload_progress(progress);
			}
		}
		out.flush();
		out.close();
		in.close();
		System.out.println("8.....写完数据！");
		// boolean result = ftpClient.storeFile(remoteFile, in);
		boolean result = ftpClient.completePendingCommand();
		System.out.println("9.....上传结束！");
		if (remoteSize > 0) {
			status = result ? FTPStatus.Upload_From_Break_Success : FTPStatus.Upload_From_Break_Failed;
		} else {
			status = result ? FTPStatus.Upload_New_File_Success : FTPStatus.Upload_New_File_Failed;
		}
		return status;
	}
	
}