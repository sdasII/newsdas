package com.iscas.sdas.controller.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.iscas.sdas.dto.work.AllCapacityWorkDto;
import com.iscas.sdas.service.CommonService;
import com.iscas.sdas.service.WorkService;
import com.iscas.sdas.service.log.FileLogService;
import com.iscas.sdas.service.work.OutServerService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
import com.iscas.sdas.util.ContinueFTP;
import com.iscas.sdas.util.FTPStatus;
import com.iscas.sdas.util.FileImport;

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

	/**
	 * 文件上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
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
			/*try {
				path = CommonUntils.FileImprot(request, fileLogDto);
			} catch (Exception e1) {
				e1.printStackTrace();
				fileLogDto.setResult(0);
				modelAndView.addObject("success", Constraints.RESULT_FAIL+ ":上传失败！");
			}*/
			if (path!=null) {
				try {
					String[] args = new String[3];
					args[0] = path;
					args[1] = time;//XXX 
					args[2]=cal_time;
					fileLogDto.setMethodstart(new Date());
					//new CellUploadFileTask().runTask(args);
					//new CellUploadFileOfExpertTask().runTask(args);	
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
		} else if ("capacity".equals(type)) {
			String tablename = "t_performance_work";
			List<TableInfoDto> tableInfoDtos = commonService.tableindex(tablename);
			List<AllCapacityWorkDto> performanceWorkDtos = new ArrayList<>();
			String path = null;
			FileLogDto fileLogDto = new FileLogDto();
			long starttime = System.currentTimeMillis();
			fileLogDto.setStarttime(new Date());
			fileLogDto.setType("性能工单数据");
			try {
				path = CommonUntils.FileImprot(request, fileLogDto);			
			} catch (Exception e1) {
				e1.printStackTrace();
				fileLogDto.setResult(0);
				modelAndView.addObject("success", Constraints.RESULT_FAIL+ ":上传失败！");
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
							workService.clearPerformanceWork(); // 清空表
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
	 * 上传原始文件--采用ftp协议，支持断点续传
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadfile")
	@ResponseBody
	public ModelMap uploadFile(HttpServletRequest request) {
		System.out.println("1...controller 获取请求！");
		ModelMap model = new ModelMap();
		String time = request.getParameter("time");				
		FTPStatus status = originDateUpload(request,time);
		model.addAttribute("status",status.toString());
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
			//myFtp.connect("49.4.6.47", 21, "ftpadmin", "ftp_qd123");
			System.out.println("3...连接到ftp");
			myFtp.connect("192.168.0.199", 21, "ftpadmin", "ftp_qd123");
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
	/**
	 * 获取上传进度（务必在调用/uploadfile后使用）
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadstatus")
	@ResponseBody
	public ModelMap getUploadStatus(HttpServletRequest request){
		ModelMap map = new ModelMap();
		ContinueFTP ftp = (ContinueFTP)request.getSession().getAttribute(Constraints.SESSION_FTP_STATUS);
		if (ftp!=null) {
			map.addAttribute("progress", ftp.getProgress());
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		}else {
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}

}
