package com.iscas.sdas.dao;

import java.util.List;

import com.iscas.sdas.dto.AlarmDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;
import com.iscas.sdas.dto.result.CellResultHistory;

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
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> celllist(CellResultHistory cellResultHistory);
}
