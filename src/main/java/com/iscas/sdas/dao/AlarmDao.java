package com.iscas.sdas.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iscas.sdas.dto.AlarmDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;

public interface AlarmDao {

	List<AlarmDto> currentDayAlarm();

	List<AlarmDto> allDayAlarm(AlarmDto alarmDto);
	//所有小区的
	List<AlarmDto> alarmLastHour(AlarmDto alarmDto);

	String getLastDay();

	String getLastHour();

	String getLastDayInCell(String cellname);

	String getLastHourInCell(String cellname);
	
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListLastDay(@Param("cellname")String cellname);
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListLastWeek(@Param("cellname")String cellname);
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListLastMonth(@Param("cellname")String cellname);
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListBySelect(@Param("cellname")String cellname,@Param("start")String starttime,@Param("end")String endtime);

	CellResultHistoryDto getOneAlarm(@Param("cellname")String cellname,@Param("time")String yyyyMMdd);
}
