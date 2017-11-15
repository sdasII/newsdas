package com.iscas.sdas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iscas.sdas.dao.AlarmDao;
import com.iscas.sdas.dto.AlarmDto;
import com.iscas.sdas.util.CommonUntils;

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
	 * @return
	 */
	public JSONObject lastHourClassCount(){
		try {
			AlarmDto dto = new AlarmDto();
			List<AlarmDto> list = alarmDao.alarmLastHour(dto);
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
			// TODO Auto-generated catch block
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
			return "---- -- --,--:--";
		}
	}
}
