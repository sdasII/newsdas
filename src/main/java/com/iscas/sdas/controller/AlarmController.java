package com.iscas.sdas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dto.AlarmDto;
import com.iscas.sdas.dto.result.CellResultHistory;
import com.iscas.sdas.service.AlarmService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
/**
 * 判别结果--t_cell_result_info(实时表) t_cell_result_history(历史表)
 * @author dongqun
 * 2017年12月21日下午5:01:30
 */
@Controller
@RequestMapping("/alarm")
public class AlarmController {
	@Autowired
	AlarmService alarmService;
	/**
	 * 当日预警
	 * @param cellname
	 * @return
	 */
	@RequestMapping("/currentday")
	@ResponseBody
	public ModelMap currentDayAlarm(){
		ModelMap map = new ModelMap();
		List<AlarmDto> alarmDtos =  alarmService.currentDayAlarm();
		map.addAttribute(Constraints.RESULT_ROW, alarmDtos);
		return map;
	}
	/**
	 * 所有预警
	 * @return
	 */
	@RequestMapping("/all")
	@ResponseBody
	public ModelMap allAlarm(@RequestParam(value = "currpage", required = true, defaultValue = "1") String num,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") String size,HttpServletRequest request){
		ModelMap map = new ModelMap();
		AlarmDto alarmDto = new AlarmDto();
		String cellname = request.getParameter("cellname");
		String daynum = request.getParameter("daynum");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		if (!CommonUntils.isempty(cellname)) {
			alarmDto.setCell_code(cellname);
		}
		if (!CommonUntils.isempty(daynum)) {
			alarmDto.setDaynum(daynum);
		}
		if (!CommonUntils.isempty(starttime)) {
			alarmDto.setStarttime(starttime);
		}
		if (!CommonUntils.isempty(endtime)) {
			alarmDto.setEndtime(endtime);
		}
		int pageNum = Integer.parseInt(num);
		int pageSize = Integer.parseInt(size);
		PageHelper.startPage(pageNum, pageSize);
		List<AlarmDto> alarmDtos =  alarmService.allAlarm(alarmDto);	
		PageInfo<AlarmDto> pageInfo = new PageInfo<>(alarmDtos);
		List<AlarmDto> rows = new ArrayList<>();
		for (int i = 0; i < alarmDtos.size(); i++) {
			AlarmDto dto = alarmDtos.get(i);
			rows.add(dto);
		}
		PageDto<AlarmDto> pageDto = new PageDto<>();
		pageDto.setTotal(pageInfo.getTotal());
		pageDto.setRows(rows);
		map.addAttribute(Constraints.RESULT_ROW, pageDto);
		return map;
	}
	@RequestMapping("/")
	public ModelAndView page(){
		return new ModelAndView("alarm/alarm");
	}
	/**
	 * 全部小区的判别结果更新时间
	 * @return
	 */
	@RequestMapping("/updatetime")
	@ResponseBody
	public String update(){
		return alarmService.getUpdateTime();
	}
	/**
	 * 指定小区的更新时间
	 * @param cellname
	 * @return
	 */
	@RequestMapping("/cellupdatetime")
	@ResponseBody
	public String cellupdate(@RequestParam(required=true,value="cellname")String cellname){
		return alarmService.getCellUpdateTime(cellname);
	}
	/**
	 * 指定小区最近一天数据
	 * @param cellname
	 * @return
	 */
	@RequestMapping("/celllastday")
	@ResponseBody
	public ModelMap cellLastDay(@RequestParam(required=true,value="cellname")String cellname){
		ModelMap map = new ModelMap();
		AlarmDto alarmDto = new AlarmDto();
		alarmDto.setCell_code(cellname);
		List<AlarmDto> alarmDtos = alarmService.getCellByLastDay(alarmDto);
		map.addAttribute(Constraints.RESULT_ROW, alarmDtos);
		return map;
	}
	
	
	/**
	 * 最新一小时预警
	 * @param request
	 * @return
	 */
	@RequestMapping("/lastHourAlarm")
	@ResponseBody
	public ModelMap lastHourAlarm(HttpServletRequest request){
		int app_result = Integer.valueOf(request.getParameter("type"));
		ModelMap map = new ModelMap();
		AlarmDto dto = new AlarmDto();
		dto.setApp_result(app_result);
		List<AlarmDto> dtos = alarmService.lastHourAlarm(dto);
		map.addAttribute(Constraints.RESULT_ROW, dtos);
		return map;
	}
	/**
	 * 最近一小时预警的数量及分类数
	 * @param request
	 * @return
	 */
	@RequestMapping("/lastHourClassCount")
	@ResponseBody
	public ModelMap lastHourClassCount(HttpServletRequest request){
		ModelMap map = new ModelMap();
		JSONObject object = alarmService.lastHourClassCount();
		map.addAttribute(Constraints.RESULT_ROW, object);
		return map;
	}
	/**
	 * 详细信息页面跳转
	 * @return
	 */
	@RequestMapping("/todetail")
	public ModelAndView detail(HttpServletRequest request){
		String cellname=request.getParameter("cell_code");
		ModelAndView modelAndView = new ModelAndView("/general/detail");
		modelAndView.addObject("cellname", cellname);
		return modelAndView;
	}
	
	@RequestMapping("/celllist")
	@ResponseBody
	public ModelMap celllist(@RequestParam(value = "currpage", required = true, defaultValue = "1") String num,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") String size,HttpServletRequest request){
		ModelMap map = new ModelMap();
		CellResultHistory cellResultHistory = new CellResultHistory();
		String name = request.getParameter("name");
		if (!CommonUntils.isempty(name)) {
			cellResultHistory.setCellname(name);
		}
		int pageNum = Integer.parseInt(num);
		int pageSize = Integer.parseInt(size);
		PageHelper.startPage(pageNum, pageSize);
		List<CellResultHistory> cells = alarmService.getCellList(cellResultHistory);
		PageInfo<CellResultHistory> pageInfo = new PageInfo<>(cells);
		List<CellResultHistory> rows = new ArrayList<>();
		for (int i = 0; i < cells.size(); i++) {
			CellResultHistory dto = cells.get(i);
			rows.add(dto);
		}
		PageDto<CellResultHistory> pageDto = new PageDto<>();
		pageDto.setTotal(pageInfo.getTotal());
		pageDto.setRows(rows);
		map.addAttribute(Constraints.RESULT_ROW, pageDto);
	
		return map;
	}
}
