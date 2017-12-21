package com.iscas.sdas.dao;

import java.util.List;

import com.iscas.sdas.dto.AlarmDto;

public interface AlarmDao {

	List<AlarmDto> currentDayAlarm();

	List<AlarmDto> allDayAlarm(AlarmDto alarmDto);
	//所有小区的
	List<AlarmDto> alarmLastHour(AlarmDto alarmDto);

	String getLastDay();

	String getLastHour();

	String getLastDayInCell(String cellname);

	String getLastHourInCell(String cellname);
	//指定小区的
	List<AlarmDto> alarmLastDay(AlarmDto alarmDto);
}
