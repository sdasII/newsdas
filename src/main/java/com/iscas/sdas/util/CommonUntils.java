package com.iscas.sdas.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.iscas.sdas.dto.FileLogDto;
import com.iscas.sdas.service.log.FileLogService;
/**
 * 通用工具类
 * @author Administrator
 *
 */
public class CommonUntils {
	
	private static final String root_dir = PropertyUtil.getValueByKey("configer.properties","root_dir");
	//存放工单，小区配置等临时文件夹
	private static final String root_temp_dir = PropertyUtil.getValueByKey("configer.properties","root_temp_dir");
	//private static final String root_temp_dir = "E:/newsdas/";
	//测试csv文件存储路径
	private static final String path_csv_dir = PropertyUtil.getValueByKey("configer.properties","path_csv_dir");
/*	private static final String root_dir = "/home/hadoop/systempdata/";
	//存放工单，小区配置等临时文件夹
	private static final String root_temp_dir = "/home/hadoop/systempdata/tempdata/";
	//private static final String root_temp_dir = "E:/newsdas/";
	//测试csv文件存储路径
	private static final String path_csv_dir = "/home/hadoop/systempdata/test";
*/	
	static Logger logger = Logger.getLogger(CommonUntils.class);
	/**
	 * 判断字符串是否为null或者""
	 * Administrator
	 * 2017年9月27日下午8:41:36
	 * @param str
	 * @return
	 */
	public static boolean isempty(String str) {
		if (str==null) {
			return true;
		}else if (str=="") {
			return true;
		}else if (str==" ") {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 生成导入文件路径
	 * Administrator
	 * 2017年9月27日下午8:42:05
	 * @return
	 */
	public static String generatePath() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1; 
		int date = c.get(Calendar.DATE); 

		String path = PropertyUtil.getValueByKey("configer.properties","import_file_path") + "\\" + year + "\\" + month + "-" + date;
		//String path = "E:\\load\\" + year + "\\" + month + "-" + date;
		
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		path += "\\";

		return path;
	}
	
	/**
	* 日期转换成字符串
	* @param date 
	* @return str
	*/
	public static String DateToStr(Date date) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String str = format.format(date);
	   return str;
	} 

	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	/**
	 * 字符串转换成时间戳
	 * Administrator
	 * 2017年9月27日下午8:38:38
	 * @param str
	 * @return
	 */
	public static Timestamp strToTimestap(String str) {
		Timestamp ts = null;
	    try {
			ts =  Timestamp.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return ts;
	}
	/**
	 * 先判断是否存在指定路径，如果不存在，生成指定路径
	 * @author dongqun
	 * 2018年1月4日上午10:33:17
	 * @param path
	 * @return
	 */
	private static boolean generateDir(String path){
		boolean state = true;
		File file = new File(path);
		if (!file.exists()) {
			state = file.mkdirs();
		}
		return state;
	}
	/**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName 用户名
     * @return 替换后的用户名
     */
    public static String userNameReplaceWithStar(String userName) {
        String userNameAfterReplaced = "";

        if (userName == null){
            userName = "";
        }

        int nameLength = userName.length();

        if (nameLength <= 1) {
            userNameAfterReplaced = "*";
        } else if (nameLength == 2) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{0})\\d(?=\\d{1})");
        } else if (nameLength >= 3 && nameLength <= 6) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{1})\\d(?=\\d{1})");
        } else if (nameLength == 7) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{1})\\d(?=\\d{2})");
        } else if (nameLength == 8) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{2})\\d(?=\\d{2})");
        } else if (nameLength == 9) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{2})\\d(?=\\d{3})");
        } else if (nameLength == 10) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{3})\\d(?=\\d{3})");
        } else if (nameLength >= 11) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{3})\\d(?=\\d{4})");
        }

        return userNameAfterReplaced;

    }

    /**
     * 实际替换动作
     *
     * @param username username
     * @param regular  正则
     * @return
     */
    private static String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }

    /**
     * 身份证号替换，保留前四位和后四位
     *
     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param idCard 身份证号
     * @return
     */
    public static String idCardReplaceWithStar(String idCard) {

        if (idCard.isEmpty() || idCard == null) {
            return null;
        } else {
            return replaceAction(idCard, "(?<=\\d{4})\\d(?=\\d{4})");
        }
    }

    /**
     * 银行卡替换，保留后四位
     *
     * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param bankCard 银行卡号
     * @return
     */
    public static String bankCardReplaceWithStar(String bankCard) {

        if (bankCard.isEmpty() || bankCard == null) {
            return null;
        } else {
            return replaceAction(bankCard, "(?<=\\d{0})\\d(?=\\d{4})");
        }
    }
    /**
     * 多文件上传
     * @param request
     * @return
     */
	public static List<String> MultipleFilesImport(HttpServletRequest request) {
		List<String> filepaths = new ArrayList<>();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest mutiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = mutiRequest.getFiles("file");
			for (MultipartFile file : files) {
				if (file != null) {
					int index = file.getOriginalFilename().lastIndexOf(".");
					if (index>0) {
						String filename = file.getOriginalFilename().substring(0, index) +"-"+ System.currentTimeMillis()+file.getOriginalFilename().substring(index);
						if (generateDir(root_temp_dir)) {
							String filepath = root_temp_dir + filename;						
							File targetfile = new File(filepath);
							if (targetfile.exists()) {
								targetfile.delete();
							}
							try {
								file.transferTo(targetfile);
								filepaths.add(filepath);
							} catch (IllegalStateException | IOException e) {
								e.printStackTrace();
							}
						}											
					}			
				}
			}
		}
		return filepaths;
	}
	/**
	 * 单个文件导入---带日志
	 * @param service
	 * @param request
	 * @param fileLogDto
	 * @return
	 * @throws IllegalStateException 
	 * @throws IOException 
	 * @throws IllegalStatException 
	 */
	public static String SignalFileImprot(HttpServletRequest request,FileLogDto fileLogDto) throws IllegalStateException, IOException  {
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest mutiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = mutiRequest.getFiles("file");
			if (files.size()>0) {
				MultipartFile file = files.get(0);
				if (file != null) {
					int index = file.getOriginalFilename().lastIndexOf(".");
					if (index>0) {
						String filename = file.getOriginalFilename().substring(0, index) +"-"+ System.currentTimeMillis()+file.getOriginalFilename().substring(index);
						if (generateDir(root_temp_dir)) {
							String filepath = root_temp_dir + filename;
							fileLogDto.setFilename(file.getOriginalFilename());						
							File targetfile = new File(filepath);
							if (targetfile.exists()) {
								targetfile.delete();
							}
							file.transferTo(targetfile);
							return filepath;
						}								
					}			
				}			
			}
		}
		return null;
	}
	/**
	 * 多个文件导入---自动存储日志
	 * @param request
	 * @param filepath
	 * @param filename
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static void MultipleFilesImportWithLog(FileLogService service,HttpServletRequest request,String type) throws Exception {
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest mutiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = mutiRequest.getFiles("file");
			for (MultipartFile file : files) {			
				if (file != null) {					
					FileLogDto fileLogDto = new FileLogDto();
					String str_filename = root_dir + file.getOriginalFilename();	
					fileLogDto.setFilename(file.getOriginalFilename());
					fileLogDto.setType(type);
					fileLogDto.setStarttime(new Date());
					long start = System.currentTimeMillis();
					File targetfile = new File(str_filename);
					if (targetfile.exists()) {
						targetfile.delete();
					}
					try {
						file.transferTo(targetfile);
						fileLogDto.setResult(1);
						long end = System.currentTimeMillis();
						long alltime =  end-start;//耗时（秒）
						fileLogDto.setEndtime(new Date());
						fileLogDto.setAlltime(alltime);	
					} catch (Exception e) {
						fileLogDto.setResult(0);
						long end = System.currentTimeMillis();
						long alltime =  end-start;//耗时（秒）
						fileLogDto.setEndtime(new Date());
						fileLogDto.setAlltime(alltime);	
						throw e;
					}	
					service.insertOne(fileLogDto);				
				}			
			}
		}

	}
	/**
	 * 判断一月有多少天
	 * 一、三、五、七、八、十、十二都是三十一天，二月28天，闰年29天，其余月份年年都是30天
	 * @param month
	 * @return
	 */
	public static int daysInMonth(int year,int month){
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (isleapyear(year)) {
				return 29;
			}else {
				return 28;
			}
		default:
			return -1;
		}
	}
	/**
	 * 判断是否为闰年
	 * 由用户输入任意一个年份，能被4整除但不能被100整除，或者能被400整除，是闰年
	 * @param year
	 * @return
	 */
	public static boolean isleapyear(int year) {
        if ((year%4==0&&year%100!=0)||year%400==0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
    /**
     * 创建指定文件目录
     * @param yyyyMMdd
     * @return
     * @throws IOException
     */
    public static String createDirecroty(String rootDir,String yyyyMMdd) throws IOException {
		String yearDir = yyyyMMdd.substring(0, 4);
		String monthDir = yyyyMMdd.substring(4, 6);
		String dayDir = yyyyMMdd.substring(6);
		String directory =rootDir +"/" + yearDir + "/" + monthDir + "/" + dayDir;	
		File file = new File(directory);
		if (!file.exists()) {
			file.mkdirs();
		}
		directory += "/";
		return directory;
	}
    public static String ZipFileImprot(HttpServletRequest request,FileLogDto fileLogDto,String yyyyMMdd) throws IllegalStateException, IOException  {	
    	// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）	
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest mutiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = mutiRequest.getFiles("file");
			for (MultipartFile file : files) {
				if (file != null) {
					int index = file.getOriginalFilename().lastIndexOf(".");
					if (index>0) {
						double allbytes = file.getSize();
						double process = 0;
						double localreadbytes = 0L;
						String filename = file.getOriginalFilename();
						String filepath = createDirecroty(root_dir, yyyyMMdd) + filename;
						fileLogDto.setFilename(file.getOriginalFilename());						
						File targetfile = new File(filepath);
						if (targetfile.exists()) {
							targetfile.delete();
						}
						InputStream is = file.getInputStream();
						FileOutputStream fileos = new FileOutputStream(targetfile);
						byte[] bytes = new byte[1024 * 1024];
						int c;
						//System.out.println("7.....开始写数据！");
						while ((c = is.read(bytes)) != -1) {
							fileos.write(bytes, 0, c);
							localreadbytes += c;
							double temp = localreadbytes / allbytes;
							if (temp * 100 != process) {
								process = (localreadbytes / allbytes) * 100;	
								// TODO 汇报上传状态	
								Constraints.setFtp_upload_progress(process);
							}
						}
						fileos.flush();
						fileos.close();
						is.close();
						//file.transferTo(targetfile);
						return filepath;				
					}			
				}
			}
		}
		return null;
	}
    public static String CsvFileImprot(HttpServletRequest request,FileLogDto fileLogDto,String yyyyMMdd) throws IllegalStateException, IOException  {
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest mutiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = mutiRequest.getFiles("file");
			for (MultipartFile file : files) {
				if (file != null) {
					int index = file.getOriginalFilename().lastIndexOf(".");
					if (index>0) {
						String filename = file.getOriginalFilename().substring(0, index) +"-"+ System.currentTimeMillis()+file.getOriginalFilename().substring(index);
						String filepath = createDirecroty(path_csv_dir, yyyyMMdd) + filename;
						fileLogDto.setFilename(file.getOriginalFilename());						
						File targetfile = new File(filepath);
						if (targetfile.exists()) {
							targetfile.delete();
						}
						file.transferTo(targetfile);
						return filepath;				
					}			
				}
			}
		}
		return null;
	}

}
