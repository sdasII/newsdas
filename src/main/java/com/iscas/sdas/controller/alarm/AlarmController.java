package com.iscas.sdas.controller.alarm;

import java.lang.reflect.Method;
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
import com.iscas.sdas.dto.alarm.AlarmDto;
import com.iscas.sdas.dto.cell.CellInfoDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;
import com.iscas.sdas.dto.result.CellResultHistory;
import com.iscas.sdas.service.alarm.AlarmService;
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
	 * 最新一小时预警
	 * @param request
	 * @return
	 */
	@RequestMapping("/lastHourAlarm")
	@ResponseBody
	public ModelMap lastHourAlarm(HttpServletRequest request){
		String type=request.getParameter("type");
		ModelMap map = new ModelMap();
		AlarmDto dto = new AlarmDto();
		if (CommonUntils.isempty(type)) {
			List<CellInfoDto> dtos = alarmService.lastHourAlarm(dto);
			map.addAttribute(Constraints.RESULT_ROW, dtos);
		}else if (!CommonUntils.isempty(type) && !"10".equals(type)) {
			int app_result = Integer.valueOf(request.getParameter("type"));
			dto.setApp_result(app_result);
			List<CellInfoDto> dtos = alarmService.lastHourAlarm(dto);
			map.addAttribute(Constraints.RESULT_ROW, dtos);
		}else if (!CommonUntils.isempty(type) && "10".equals(type)) {
			List<CellInfoDto> dtos = alarmService.lastHourOthersAlarm();
			map.addAttribute(Constraints.RESULT_ROW, dtos);
		}
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
		ModelAndView modelAndView = new ModelAndView("/cell/detail");
		modelAndView.addObject("cellname", cellname);
		return modelAndView;
	}
	/**
	 * 所有t_cell_result_history的查询
	 * @author dongqun
	 * 2017年12月28日下午3:29:54
	 * @param num
	 * @param size
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping("/celllist")
	@ResponseBody
	public ModelMap allcelllist(@RequestParam(value = "currpage", required = true, defaultValue = "1") String num,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") String size,
			@RequestParam(value = "type", required = true, defaultValue = "day") String type,HttpServletRequest request){
		ModelMap map = new ModelMap();
		String cellname = request.getParameter("cellname");
		String result = request.getParameter("result");
		String starttime= null,endtime = null;
		if (Constraints.SELECT.equals(type)) {
			starttime = request.getParameter("starttime");
			endtime = request.getParameter("endtime");
		}
		int pageNum = Integer.parseInt(num);
		int pageSize = Integer.parseInt(size);
		PageHelper.startPage(pageNum, pageSize);
		String ttype = null;
		ttype = request.getParameter("cell");
		if (!"cell".equals(ttype)) {
			ttype = null;
		}
		if ("-1".equals(result)) {
			result = null;
		}
		PageDto<CellResultHistory> pageDto = alarmService.getCellList(cellname,type,starttime,endtime,ttype,result);
		map.addAttribute(Constraints.RESULT_ROW, pageDto);	
		return map;
	}
	/**
	 * 获取子列表数据--一天
	 * @author dongqun
	 * 2018年1月5日上午11:25:02
	 * @param request
	 * @return
	 */
	@RequestMapping("/celllist/incell")
	@ResponseBody
	public ModelMap celllist(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String cellname = request.getParameter("name");
		String yyyyMMdd = request.getParameter("yyyymmdd");
		String result = request.getParameter("type");
		CellResultHistoryDto cellResultHistoryDto = alarmService.getOneAlarm(cellname, yyyyMMdd);
		List<CellResultHistory> cells = generateData(cellResultHistoryDto);
		List<CellResultHistory> rows = new ArrayList<>();
		for (int i = 0; i < cells.size(); i++) {
			if (!CommonUntils.isempty(result) && !"-1".equals(result) && cells.get(i).getResult()!=null&&!"10".equals(result)) {			
				if (cells.get(i).getResult().equals(Integer.valueOf(result))) {
					CellResultHistory dto = cells.get(i);
					rows.add(dto);
				}
			}
			if (CommonUntils.isempty(result) || "-1".equals(result)) {
				CellResultHistory dto = cells.get(i);
				rows.add(dto);
			}	
			if ("10".equals(result)) {
				Integer value = cells.get(i).getResult();
				if (value!=null) {
					if (value==0||value==1) {
						CellResultHistory dto = cells.get(i);
						rows.add(dto);
					}
				}
				
			}
		}		
		map.addAttribute(Constraints.RESULT_ROW, rows);
	
		return map;
	}
	/**
	 * 生成子列表的数据
	 * @author dongqun
	 * 2018年1月5日上午11:25:48
	 * @param dto
	 * @return
	 */
	private List<CellResultHistory> generateData(CellResultHistoryDto dto){
		List<CellResultHistory> result = new ArrayList<>();
		if (dto!=null) {
			Method[] methods = dto.getClass().getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("getRange")) {
					CellResultHistory crh = new CellResultHistory();
					crh.setCalcultime(dto.getCreate_time());
					crh.setCellname(dto.getCell_code());
					crh.setYyyymmdd(dto.getYyyyMMdd());
					String strhour = methodName.substring(9);
					crh.setHour(Integer.valueOf(strhour));
					try {
						Integer r = (Integer)method.invoke(dto, null);
						crh.setResult(r);
					} catch (Exception e) {
						e.printStackTrace();
					}
					result.add(crh);
				}
			}
		}
		return result;
	}

}
