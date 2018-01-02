package com.iscas.sdas.controller.work;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.iscas.sdas.dto.FileLogDto;
import com.iscas.sdas.dto.TableInfoDto;
import com.iscas.sdas.dto.work.AllCapacityWorkDto;
import com.iscas.sdas.service.CommonService;
import com.iscas.sdas.service.WorkService;
import com.iscas.sdas.service.log.FileLogService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
import com.iscas.sdas.util.FileImport;

import objects.JSON;
import tasks.BGTask;
import tasks.sheet.SheetCheckTask;


@Controller
@RequestMapping("/work")
public class WorkController {
	
	@Autowired
	CommonService commonService;
	@Autowired
	WorkService workService;
	@Autowired
	FileLogService fileLogService;
	
	@RequestMapping("/capacity")
	public ModelAndView capacity(){
		return new ModelAndView("work/capacity");
	}
	
	@RequestMapping("/device")
	public ModelAndView device(){
		return new ModelAndView("/work/device");
	}
	
	@RequestMapping("/indexinfo")
	public ModelAndView indexinfo(){
		return new ModelAndView("/work/indexinfo");
	}
	
	@RequestMapping("/outservice")
	public ModelAndView outservice(){
		return new ModelAndView("/work/outservice");
	}
	/**
	 * 导入性能工单
	 * @param request
	 * @return
	 */
	@RequestMapping("/import/capacity")
	public ModelAndView fileimport(HttpServletRequest request){
		ModelAndView map = new ModelAndView("/work/capacity");
		String tablename = "t_performance_work";
		List<TableInfoDto> tableInfoDtos = commonService.tableindex(tablename);		
		List<AllCapacityWorkDto> performanceWorkDtos = new ArrayList<>();
		List<String> paths = CommonUntils.MultipleFilesImport(request);
		if (paths!=null && paths.size()>0) {
			if (tableInfoDtos!=null && tableInfoDtos.size()>0) {
				int rows = FileImport.tablerows(paths.get(0));
				for (int i = 0; i < rows; i++) {
					AllCapacityWorkDto workDto = new AllCapacityWorkDto();
					performanceWorkDtos.add(workDto);
				}
				try {
					FileImport.importwork(paths.get(0), performanceWorkDtos, tableInfoDtos);//将excel映射为对象
					workService.clearPerformanceWork();	//清空表
					workService.insertPerformanceWork(performanceWorkDtos);//插入表并将questionflag置为-1
				} catch (Exception e) {
					map.addObject(Constraints.RESULT_SUCCESS, false);
					e.printStackTrace();
				}
			}
		}else {
			map.addObject(Constraints.RESULT_SUCCESS, false);
		}		
		return map;
		
	}

	/**
	 * 工单验证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public ModelMap workvalidate() throws Exception{
		ModelMap map = new ModelMap();
		try {
			BGTask task = new SheetCheckTask();
			JSON ret = task.runTask(new String[]{});
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
			logDto.setFilename("性能工单验证");
			logDto.setType("性能工单验证");
			logDto.setStarttime(startdate);
			logDto.setEndtime(enddate);
			if (ret.getType().equals(objects.JSON.TYPE.SUCCESS)) {
				logDto.setResult(1);
			}else{
				logDto.setResult(0);
			}
			fileLogService.insertOne(logDto);
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		} catch (Exception e) {
			e.printStackTrace();
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}
	
	
}
