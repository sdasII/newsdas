package com.iscas.sdas.controller.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.iscas.sdas.dto.FileLogDto;
import com.iscas.sdas.dto.TableInfoDto;
import com.iscas.sdas.dto.complain.AllComplaintDetailDtoWithBLOBs;
import com.iscas.sdas.dto.complain.AllComplaintDto;
import com.iscas.sdas.dto.work.AllCapacityWorkDto;
import com.iscas.sdas.service.CommonService;
import com.iscas.sdas.service.WorkService;
import com.iscas.sdas.service.complain.AllComplainService;
import com.iscas.sdas.service.log.FileLogService;
import com.iscas.sdas.service.work.OutServerService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
import com.iscas.sdas.util.ContinueFTP;
import com.iscas.sdas.util.FTPStatus;
import com.iscas.sdas.util.FileImport;
import com.iscas.sdas.util.Message;

import objects.JSON;
import tasks.BGTask;
import tasks.cell.CaculateTask;
import tasks.cell.CaculateTestTask;
import tasks.cell.TransferTask;
import tasks.cell.model.OffLineHealthModelBDTask;
import tasks.cell.netupload.CellUploadFileTask;
import tasks.cell.netupload.PutNetFile2HDFSTask;

@Controller
@RequestMapping("/data")
public class DataController{
	
	@Autowired
	WorkService workService;
	@Autowired
	CommonService commonService;
	@Autowired
	OutServerService outServerService;
	@Autowired
	FileLogService fileLogService;
	@Autowired
	AllComplainService allComplainService;
	
	@RequestMapping("/online")
	public ModelAndView online(){
		return new ModelAndView("/data/online");
	}
	@RequestMapping("/offline")
	public ModelAndView offline(HttpServletRequest request){
		ModelAndView modelAndView  = new ModelAndView("/data/offline");
		modelAndView.addObject("success", Constraints.RESULT_UNKNOWN);		
		return modelAndView;
	}
	
	
	@RequestMapping("/formatFile")
	public ModelAndView formatFile(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("data/offline");
		String yyyyMMdd = request.getParameter("YYYYMMDD");
		System.out.println("用户选择的年月日\t"+yyyyMMdd);
		String[] params = new String[]{yyyyMMdd};
		JSON result = new PutNetFile2HDFSTask().runTask(params);
		System.out.println("返回数据结果\t"+result);
		return modelAndView;
	}
	
	/**
	 * 网管文件上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload/network")
	public ModelAndView upload(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("data/offline");
		String type = request.getParameter("type");
		if ("network".equals(type)) {
			String time = request.getParameter("time");
			String cal_time=request.getParameter("cal_time");
			String path = request.getParameter("path");
			FileLogDto fileLogDto = new FileLogDto();
			long starttime = System.currentTimeMillis();
			fileLogDto.setStarttime(new Date());
			fileLogDto.setType("中兴网管指标数据");
			if (path!=null) {
				try {
					String[] args = new String[3];
					args[0] = time;//XXX 
					args[1]=cal_time;
					fileLogDto.setMethodstart(new Date());
					JSON result = new CellUploadFileTask().runTask(args);
					System.out.println("返回数据结果\t"+result);
					fileLogDto.setMethodend(new Date());
					modelAndView.addObject("success", Constraints.RESULT_SUCCESS);
					fileLogDto.setResult(1);
				} catch (Exception e) {
					e.printStackTrace();
					fileLogDto.setResult(0);
					modelAndView.addObject("success", Constraints.RESULT_FAIL+ ":调用后台方法失败！");
				}
			}else {
				fileLogDto.setResult(0);
			}
			long endtime = System.currentTimeMillis();
			fileLogDto.setEndtime(new Date());
			long alltime = endtime - starttime;
			fileLogDto.setAlltime(alltime);
			List<FileLogDto> fileLogDtos = new ArrayList<>();
			fileLogDtos.add(fileLogDto);
			fileLogService.insert(fileLogDtos);
		}
		return modelAndView;
	}
	
	/**
	 * 性能工单导入
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload/capacitywork")
	public ModelAndView uploadCapacityWork(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("data/offline");
		String tablename = "t_performance_work";
		List<TableInfoDto> tableInfoDtos = commonService.tableindex(tablename);
		List<AllCapacityWorkDto> performanceWorkDtos = new ArrayList<>();
		String path = null;
		FileLogDto fileLogDto = new FileLogDto();
		long starttime = System.currentTimeMillis();
		fileLogDto.setStarttime(new Date());
		fileLogDto.setType("性能工单数据");
		try {
			path = CommonUntils.SignalFileImprot(request, fileLogDto);
		} catch (Exception e1) {
			e1.printStackTrace();
			fileLogDto.setResult(0);
			modelAndView.addObject("success", Constraints.RESULT_FAIL + ":上传失败！");
		}
		if (path != null) {
			if (tableInfoDtos != null && tableInfoDtos.size() > 0) {
				int rows = FileImport.tablerows(path);
				for (int i = 0; i < rows; i++) {
					AllCapacityWorkDto workDto = new AllCapacityWorkDto();
					performanceWorkDtos.add(workDto);
				}
				try {
					FileImport.importwork(path, performanceWorkDtos, tableInfoDtos);// 将excel映射为对象
					try {
						//workService.clearPerformanceWork(); // 清空表
						workService.insertPerformanceWork(performanceWorkDtos);// 插入表并将questionflag置为-1
						modelAndView.addObject("success", Constraints.RESULT_SUCCESS);
						fileLogDto.setResult(1);
					} catch (Exception e) {
						e.printStackTrace();
						fileLogDto.setResult(0);
						modelAndView.addObject("success", Constraints.RESULT_FAIL + ":文件导入失败！");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					fileLogDto.setResult(0);
					modelAndView.addObject("success", Constraints.RESULT_FAIL + ":文件损坏！");
				}
			}
		} else {
			fileLogDto.setResult(0);
		}
		long endtime = System.currentTimeMillis();
		fileLogDto.setEndtime(new Date());
		long alltime = endtime - starttime;
		fileLogDto.setAlltime(alltime);
		List<FileLogDto> fileLogDtos = new ArrayList<>();
		fileLogDtos.add(fileLogDto);
		fileLogService.insert(fileLogDtos);
		return modelAndView;
	}
	
	
	/**
	 * 上传原始文件--采用ftp协议，支持断点续传
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadfile")
	@ResponseBody
	public ModelMap uploadFile(HttpServletRequest request) {
		System.out.println("1...controller 获取请求！");
		ModelMap model = new ModelMap();
		String yyyyMMdd = request.getParameter("time");				
		FTPStatus status = originDateUpload(request,yyyyMMdd);
		model.addAttribute("status",status.toString());
		//JSON json = upload2HDFS(yyyyMMdd);
		return model;
	}
	
	private FTPStatus originDateUpload(HttpServletRequest request, String yyyyMMdd) {
		MultipartHttpServletRequest mutiRequest = (MultipartHttpServletRequest) request;
		MultipartFile sourceFile = mutiRequest.getFiles("file").get(0);
		String filename = sourceFile.getOriginalFilename();
		System.out.println("2...获取到文件！");
		FileLogDto fileLogDto = new FileLogDto();
		fileLogDto.setStarttime(new Date());
		fileLogDto.setType("中兴网管指标原始数据");
		fileLogDto.setFilename(filename);
		ContinueFTP myFtp = new ContinueFTP();
		try {
			myFtp.connect("49.4.6.146", 21, "hadoop", "nfs_qd123");	
			//myFtp.connect("192.168.0.31", 21, "hadoop", "nfs_qd123");
			//myFtp.connect("172.16.0.151", 21, "hadoop", "nfs_qd123");	
			
			System.out.println("3...连接到ftp");
			request.getSession().setAttribute(Constraints.SESSION_FTP_STATUS, myFtp);						
			long starttime = System.currentTimeMillis();
			FTPStatus status = myFtp.upload(sourceFile, filename,yyyyMMdd);
			fileLogDto.setEndtime(new Date());
			long endtime = System.currentTimeMillis();
			long alltime = endtime - starttime;
			fileLogDto.setAlltime(alltime);
			if (status.toString().equals(FTPStatus.Upload_From_Break_Success.toString())||status.toString().equals(FTPStatus.Upload_New_File_Success.toString())) {
				fileLogDto.setResult(1);
			}else {
				fileLogDto.setResult(0);
			}
			fileLogService.insertOne(fileLogDto);
			myFtp.disconnect();	
			
			return status;
		} catch (Exception e) {
			request.getSession().setAttribute(Constraints.SESSION_FTP_STATUS, myFtp);
			return FTPStatus.CONNECT_FAIL;
		}

	}
	
	//zip文件转换上传到hdfs上
	private JSON upload2HDFS(String yyyyMMdd) {
		BGTask task = new TransferTask();
		String[] params = new String[]{yyyyMMdd};
		return task.runTask(params);
	}
	
	/**
	 * 导入投诉数据
	 * @return
	 */
	@RequestMapping("/uploadcomplain")
	@ResponseBody
	public ModelMap uploadComplaints(HttpServletRequest request,HttpServletResponse response){
		ModelMap map = new ModelMap(); 
		String file_time = request.getParameter("time")+" 00:00:00";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(file_time);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		List<String> paths = null;
		try {
			paths = CommonUntils.MultipleFilesImport(request);		
		} catch (Exception e1) {
			e1.printStackTrace();
			map.addAttribute(Constraints.RESULT_SUCCESS, "文件上传失败！");
		}
		String result1 = null,result2 = null,file1 = null,file2 = null;
		for (String path : paths) {
			if (path.contains("客户投诉常驻小区")) {
				result1 = uploadComplainCell(date, request,path);
				file1 = path.substring(path.lastIndexOf("/"));
			}else if (path.contains("客户投诉情况")) {
				result2 = uploadComplainDetail(request, path);
				file2 = path.substring(path.lastIndexOf("/"));
			}
		}
		if (file1==null) {
			file1 = "客户投诉常驻小区文件命名不规范";
			result1 = "文件请以《XXX客户投诉常驻小区XXX》命名";
		}
		if (file2==null) {
			file2 = "客户投诉投诉文件命名不规范";
			result2 = "文件请以《XXX客户投诉情况XXX》命名";
		}	
		String allStatus = file1 +": "+result1+";"+file2+": "+result2;
		map.addAttribute(Constraints.RESULT_SUCCESS, allStatus);		
		return map;
	}
	
	private String uploadComplainCell(Date date,HttpServletRequest request,String path){
		String result = null;
		if (date!=null) {
			String tablename = "t_complaint_residentarea";
			List<TableInfoDto> fields = commonService.tableindex(tablename);//获取表中的所有字段***用户导入表中字段数据量应与该数据相等
			List<AllComplaintDto> allComplaints = new ArrayList<>();
			FileLogDto fileLogDto = new FileLogDto();
			long starttime = System.currentTimeMillis();
			fileLogDto.setStarttime(new Date());
			fileLogDto.setType("投诉工单数据");			
			if (path != null) {
				String filename = path.substring(path.lastIndexOf("/"));
				fileLogDto.setFilename(filename);
				if (fields != null && fields.size() > 0) {
					int rows = FileImport.tablerows(path);//根据带导入表中数据的条数预先生成等量的dto对象
					for (int i = 0; i < rows; i++) {
						AllComplaintDto dto = new AllComplaintDto();
						dto.setRecord_time(date);
						allComplaints.add(dto);
					}
					try {
						FileImport.importwork(path, allComplaints, fields);// 将excel映射为对象
						try {						
							allComplainService.insertCell(allComplaints);
							result = "导入成功! ";
							fileLogDto.setResult(1);
						} catch (Exception e) {
							e.printStackTrace();
							fileLogDto.setResult(0);
							result = "文件导入失败！";
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						fileLogDto.setResult(0);
						result = "文件损坏！";
					}			
				}
			}else {
				fileLogDto.setResult(0);
			}
			long endtime = System.currentTimeMillis();
			fileLogDto.setEndtime(new Date());
			long alltime = endtime - starttime;
			fileLogDto.setAlltime(alltime);
			fileLogService.insertOne(fileLogDto);
		}
		return result;
	}
	
	private String uploadComplainDetail(HttpServletRequest request, String path) {
		String result = null;
		String tablename = "t_complaint_detail";
		List<TableInfoDto> fields = commonService.tableindex(tablename);// 获取表中的所有字段***用户导入表中字段数据量应与该数据相等
		List<AllComplaintDetailDtoWithBLOBs> allDetails = new ArrayList<>();
		FileLogDto fileLogDto = new FileLogDto();
		long starttime = System.currentTimeMillis();
		fileLogDto.setStarttime(new Date());
		fileLogDto.setType("投诉工单数据");
		if (path != null) {
			String filename = path.substring(path.lastIndexOf("/"));
			fileLogDto.setFilename(filename);
			if (fields != null && fields.size() > 0) {
				int rows = FileImport.tablerows(path);// 根据带导入表中数据的条数预先生成等量的dto对象
				for (int i = 0; i < rows; i++) {
					AllComplaintDetailDtoWithBLOBs dto = new AllComplaintDetailDtoWithBLOBs();
					allDetails.add(dto);
				}
				try {
					FileImport.importwork(path, allDetails, fields);// 将excel映射为对象
					try {
						allComplainService.insertDetails(allDetails);
						result = "导入成功! ";
						fileLogDto.setResult(1);									
					} catch (Exception e) {
						e.printStackTrace();
						fileLogDto.setResult(0);
						result = "文件导入失败！";
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					fileLogDto.setResult(0);
					result = "文件损坏！";
				}
			}
		} else {
			fileLogDto.setResult(0);
		}
		long endtime = System.currentTimeMillis();
		fileLogDto.setEndtime(new Date());
		long alltime = endtime - starttime;
		fileLogDto.setAlltime(alltime);
		fileLogService.insertOne(fileLogDto);

		return result;
	}

	/**
	 * 单个csv网管文件上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/csvUpload")
	@ResponseBody
	public ModelMap uploadCsvTestFile(HttpServletRequest request) {
		ModelMap map = new ModelMap();
		String time = request.getParameter("time");
		FileLogDto fileLogDto = new FileLogDto();
		long starttime = System.currentTimeMillis();
		fileLogDto.setStarttime(new Date());
		fileLogDto.setType("单个中兴网管指标数据");
		fileLogDto.setStarttime(new Date());
		String path = null;	
		try {
			path = CommonUntils.CsvFileImprot(request, fileLogDto, time);
			if (!CommonUntils.isempty(path)) {
				fileLogDto.setResult(1);
			}else {
				fileLogDto.setResult(0);
			}
			map.addAttribute("success", Constraints.RESULT_SUCCESS + ":上传成功！");
		} catch (Exception e1) {
			e1.printStackTrace();
			fileLogDto.setResult(0);
			map.addAttribute("success", Constraints.RESULT_FAIL + ":上传失败！");
		}		
		long endtime = System.currentTimeMillis();
		fileLogDto.setEndtime(new Date());
		long alltime = endtime - starttime;
		fileLogDto.setAlltime(alltime);
		List<FileLogDto> fileLogDtos = new ArrayList<>();
		fileLogDtos.add(fileLogDto);
		fileLogService.insert(fileLogDtos);
		return map;
	}//*/
	
	/**
	 * 单个csv网管文件分析
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/csvStatistic")
	@ResponseBody
	public ModelMap statisticCsvTestFile(HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();
		String filetime = request.getParameter("filetime");
		String modeltime = request.getParameter("modeltime");
		BGTask task = new CaculateTestTask();
		String[] args = new String[]{modeltime,filetime};
		JSON ret = task.runTask(args);
		String filename = task.getAppName();
		map.addAttribute("rows",ret);
		System.out.println(ret);
		if (ret!=null) {
			long start = ret.getStart();
			long end = ret.getEnd();
			long alltime = end-start;
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    String s = format.format(start);  
		    Date startdate = format.parse(s);
			String e = format.format(end);
			Date enddate = format.parse(e);
			FileLogDto logDto = new FileLogDto();
			logDto.setAlltime(alltime);
			logDto.setFilename(filename);
			logDto.setType("单个csv网管文件分析");
			logDto.setStarttime(startdate);
			logDto.setEndtime(enddate);
			if (ret.getType() == (objects.JSON.TYPE.SUCCESS)) {
				logDto.setResult(1);
			}else{
				logDto.setResult(0);
			}
			fileLogService.insertOne(logDto);
		}
		return map;

	}
	/**
	 * zip网管文件分析
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/zipStatistic")
	@ResponseBody
	public ModelMap statisticZipFile(HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();
		String filetime = request.getParameter("filetime");
		String modeltime = request.getParameter("modeltime");
		JSON ret1 = upload2HDFS(filetime);//上传+分片
		if (ret1.getType() == JSON.TYPE.FAILED) {
			//原始数据处理异常
			map.addAttribute("rows",ret1);
			if (ret1!=null) {
				long start = ret1.getStart();
				long end = ret1.getEnd();
				long alltime = end-start;
				SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    String s = format.format(start);  
			    Date startdate = format.parse(s);
				String e = format.format(end);
				Date enddate = format.parse(e);
				FileLogDto logDto = new FileLogDto();
				logDto.setAlltime(alltime);
				logDto.setFilename(ret1.getAppid());
				logDto.setType("原始网管数据预处理");
				logDto.setStarttime(startdate);
				logDto.setEndtime(enddate);
				logDto.setResult(0);
				fileLogService.insertOne(logDto);
			}
			return map;
		}
		BGTask task = new CaculateTask();
		String[] args = new String[] { modeltime, filetime };
		JSON ret = task.runTask(args);
		String filename = task.getAppName();
		map.addAttribute("rows",ret);
		if (ret!=null) {
			long start = ret.getStart();
			long end = ret.getEnd();
			long alltime = end-start;
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    String s = format.format(start);
		    Date startdate = format.parse(s);
			String e = format.format(end);
			Date enddate = format.parse(e);
			FileLogDto logDto = new FileLogDto();
			logDto.setAlltime(alltime);
			logDto.setFilename(filename);
			logDto.setType("ZIP网管文件分析");
			logDto.setStarttime(startdate);
			logDto.setEndtime(enddate);
			if (ret.getType() == (objects.JSON.TYPE.SUCCESS)) {
				logDto.setResult(1);
			}else{
				logDto.setResult(0);
			}
			fileLogService.insertOne(logDto);
		}
		return map;

	}
	/**
	 * zip网管文件模式计算
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/modelCalculate")
	@ResponseBody
	public ModelMap modelCalculate(HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();
		String modeltime = request.getParameter("modeltime");
		BGTask task = new OffLineHealthModelBDTask();
		JSON ret = task.runTask(modeltime);
		map.addAttribute("rows",ret);
		String filename = task.getAppName();
		if (ret!=null) {
			long start = ret.getStart();
			long end = ret.getEnd();
			long alltime = end-start;
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    String s = format.format(start);  
		    Date startdate = format.parse(s);
			String e = format.format(end);
			Date enddate = format.parse(e);
			FileLogDto logDto = new FileLogDto();
			logDto.setAlltime(alltime);
			logDto.setFilename(filename);
			logDto.setType("zip网管文件模式计算");
			logDto.setStarttime(startdate);
			logDto.setEndtime(enddate);
			if (ret.getType() == (objects.JSON.TYPE.SUCCESS)) {
				logDto.setResult(1);
			}else{
				logDto.setResult(0);
			}
			fileLogService.insertOne(logDto);
		}
		return map;

	}
}
