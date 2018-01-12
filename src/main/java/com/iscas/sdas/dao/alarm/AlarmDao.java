package com.iscas.sdas.dao.alarm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iscas.sdas.dto.alarm.AlarmDto;
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
	
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListLastDay(@Param("cellname")String cellname);
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListLastWeek(@Param("cellname")String cellname);
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListLastMonth(@Param("cellname")String cellname);
	//t_cell_result_history中的小区信息
	List<CellResultHistoryDto> cellListBySelect(@Param("cellname")String cellname,@Param("start")String starttime,@Param("end")String endtime);

	CellResultHistoryDto getOneAlarm(@Param("cellname")String cellname,@Param("time")String yyyyMMdd);
	
	//v_cell_result中的小区信息
	List<CellResultHistory> cellResultListLastDay(@Param("cellname")String cellname,@Param("type")String type,@Param("result")String result);
	//v_cell_result中的小区信息
	List<CellResultHistory> cellResultListLastWeek(@Param("cellname")String cellname,@Param("type")String type,@Param("result")String result);
	//v_cell_result中的小区信息
	List<CellResultHistory> cellResultListLastMonth(@Param("cellname")String cellname,@Param("type")String type,@Param("result")String result);
	//v_cell_result中的小区信息
	List<CellResultHistory> cellResultListBySelect(@Param("cellname")String cellname,@Param("start")String starttime,@Param("end")String endtime,@Param("type")String type,@Param("result")String result);
}
