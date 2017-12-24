package com.iscas.sdas.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iscas.sdas.dao.AlarmDao;
import com.iscas.sdas.dto.AlarmDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;
import com.iscas.sdas.dto.result.CellResultHistory;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;

@Service
public class AlarmService {

	@Autowired
	AlarmDao alarmDao;
	
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
		try {
			AlarmDto dto = new AlarmDto();
			dto.setApp_result(null);
			List<AlarmDto> list = alarmDao.alarmLastHour(null);
			JSONObject object = new JSONObject();
			object.put("all", list.size());
			dto.setApp_result(0);
			list.clear();
			list = alarmDao.alarmLastHour(dto);
			object.put("event", list.size());
			dto.setApp_result(1);
			list.clear();
			list = alarmDao.alarmLastHour(dto);
			object.put("critical", list.size());
			dto.setApp_result(2);
			list.clear();
			list = alarmDao.alarmLastHour(dto);
			object.put("health", list.size());
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
	 * 小区最近一天数据
	 * @param alarmDto
	 * @return
	 */
	public List<AlarmDto> getCellByLastDay(AlarmDto alarmDto){
		return alarmDao.alarmLastDay(alarmDto);
	}
	/**
	 * 小区历史列表
	 * @param cellResultHistory
	 * @return
	 */
	public List<CellResultHistory> getCellList(String cellname,String type,String starttime,String endtime){
		
		List<CellResultHistoryDto> sources;
		
		if (Constraints.DAY.equals(type)) {
			sources = alarmDao.cellListLastDay(cellname);
		}else if (Constraints.WEEK.equals(type)) {
			sources = alarmDao.cellListLastWeek(cellname);
		}else if (Constraints.MONTH.equals(type)) {
			sources = alarmDao.cellListLastMonth(cellname);
		}else {
			sources = alarmDao.cellListBySelect(cellname,starttime,endtime);
		}
		List<CellResultHistory> result = new ArrayList<>();
		for (CellResultHistoryDto dto : sources) {
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
