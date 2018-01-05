package com.iscas.sdas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dao.AlarmDao;
import com.iscas.sdas.dto.AlarmDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;
import com.iscas.sdas.service.cell.CellInfoService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;

@Service
public class AlarmService {

	@Autowired
	AlarmDao alarmDao;
	@Autowired
	CellInfoService cellInfoService;
	
	public List<AlarmDto> currentDayAlarm(){
		List<AlarmDto> alarmDtos = alarmDao.currentDayAlarm();
		for (int i=0;i<alarmDtos.size();i++) {
			if (alarmDtos.get(i).getCount() ==0) {
				alarmDtos.remove(i);
			}
		}
		return alarmDtos;
	}
	
	public List<AlarmDto> allAlarm(AlarmDto alarmDto){
		return alarmDao.allDayAlarm(alarmDto);
	}
	/**
	 * 最新一小时预警数据
	 * @param dto
	 * @return
	 */
	public List<AlarmDto> lastHourAlarm(AlarmDto dto){
		return alarmDao.alarmLastHour(dto);
	};
	/**
	 * 最新一小时各类预警的总数和数量
	 * 0事件1亚健康2健康3计算无数据
	 * @return
	 */
	public JSONObject lastHourClassCount(){
		int all,events,criticals,healths,others;
		try {
			JSONObject object = new JSONObject();
			all = cellInfoService.allMonitorCounts();
			object.put("all", all);						
			AlarmDto dto = new AlarmDto();
			dto.setApp_result(null);
			List<AlarmDto> list = alarmDao.alarmLastHour(null);						
			dto.setApp_result(0);
			list.clear();
			list = alarmDao.alarmLastHour(dto);
			events = list.size();
			object.put("event", events);
			dto.setApp_result(1);
			list.clear();
			list = alarmDao.alarmLastHour(dto);
			criticals = list.size();
			object.put("critical", criticals);
			dto.setApp_result(2);
			list.clear();
			list = alarmDao.alarmLastHour(dto);
			healths = list.size();
			object.put("health", healths);
			others = all-events-criticals-healths;
			object.put("others", others);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	/**
	 * 首页预警数据更新时间
	 * @return
	 */
	public String getUpdateTime(){
		String yyyyMMdd = alarmDao.getLastDay();
		String hour = alarmDao.getLastHour();
		if (!CommonUntils.isempty(yyyyMMdd) && !CommonUntils.isempty(hour)) {
			return yyyyMMdd.substring(0,4)+"-"+yyyyMMdd.substring(4, 6)+"-"+yyyyMMdd.substring(6)+","+hour+":00";
		}else {
			return null;
		}
	}
	
	/**
	 * 小区预警数据更新时间
	 * @return
	 */
	public String getCellUpdateTime(String cellname){
		String yyyyMMdd = alarmDao.getLastDayInCell(cellname);
		String hour = alarmDao.getLastHourInCell(cellname);
		if (!CommonUntils.isempty(yyyyMMdd) && !CommonUntils.isempty(hour)) {
			return yyyyMMdd.substring(0,4)+"-"+yyyyMMdd.substring(4, 6)+"-"+yyyyMMdd.substring(6)+","+hour+":00";
		}else {
			return null;
		}
	}

	/**
	 * 小区历史列表
	 * @param cellResultHistory
	 * @return
	 */
	public PageDto<CellResultHistoryDto> getCellList(String cellname,String type,String starttime,String endtime){
		
		List<CellResultHistoryDto> sources;
		
		
		if (Constraints.DAY.equals(type)) {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellListLastDay(cellname);
		}else if (Constraints.WEEK.equals(type)) {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellListLastWeek(cellname);
		}else if (Constraints.MONTH.equals(type)) {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellListLastMonth(cellname);
		}else {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellListBySelect(cellname,starttime,endtime);
		}
		PageInfo<CellResultHistoryDto> pageInfo = new PageInfo<>(sources);
		List<CellResultHistoryDto> rows = new ArrayList<>();
		for (int i = 0; i < sources.size(); i++) {
			CellResultHistoryDto dto = sources.get(i);
			rows.add(dto);
		}
		PageDto<CellResultHistoryDto> pageDto = new PageDto<>();
		pageDto.setTotal(pageInfo.getTotal());
		pageDto.setRows(rows);
		return pageDto;
			
	}
	
	public CellResultHistoryDto getOneAlarm(String cellname,String yyyyMMdd){
		return alarmDao.getOneAlarm(cellname, yyyyMMdd);
	}

}
