package com.iscas.sdas.service.alarm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dao.alarm.AlarmDao;
import com.iscas.sdas.dto.alarm.AlarmDto;
import com.iscas.sdas.dto.cell.CellInfoDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;
import com.iscas.sdas.dto.result.CellResultHistory;
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
	public List<CellInfoDto> lastHourAlarm(AlarmDto dto){
		List<AlarmDto> alarms =  alarmDao.alarmLastHour(dto);
		CellInfoDto Dto = new CellInfoDto();
		List<CellInfoDto> all = cellInfoService.getalllist(Dto);
		List<CellInfoDto> result = new ArrayList<>();
		for (AlarmDto alarmDto : alarms) {
			for (CellInfoDto cellInfoDto : all) {
				if (alarmDto.getCell_code().equals(cellInfoDto.getCell_code())) {
					cellInfoDto.setApp_result(alarmDto.getApp_result());
					/*if (cellInfoDto.getStation_longitude()!=null||cellInfoDto.getStation_latitude()!=null) {
						System.out.println("有坐标！");
					}*/
					result.add(cellInfoDto);
					break;
				}
			}
		}
		return result;
	};
	/**
	 * 最新一小时无计算数据的预警数据
	 * @author dongqun
	 * 2018年1月8日上午10:53:10
	 * @param dto
	 * @return
	 */
	public List<CellInfoDto> lastHourOthersAlarm(){
		CellInfoDto cellInfoDto = new CellInfoDto();
		List<CellInfoDto> all = cellInfoService.getalllist(cellInfoDto);
		List<AlarmDto> events,criticals,healths;
		AlarmDto alarmDto = new AlarmDto();						
		alarmDto.setApp_result(0);
		events = alarmDao.alarmLastHour(alarmDto);
		alarmDto.setApp_result(1);
		criticals = alarmDao.alarmLastHour(alarmDto);
		alarmDto.setApp_result(2);
		healths = alarmDao.alarmLastHour(alarmDto);
		if (healths!=null) {
			for (AlarmDto dto : healths) {
				String cellcode = dto.getCell_code();
				for (int i = 0; i < all.size(); i++) {
					String cellInfoCode = all.get(i).getCell_code();
					if (cellcode.equals(cellInfoCode)) {
						all.remove(i);
					}
				}										
			}
		}
		if (criticals!=null) {
			for (AlarmDto dto : criticals) {
				String cellcode = dto.getCell_code();
				for (int i = 0; i < all.size(); i++) {
					String cellInfoCode = all.get(i).getCell_code();
					if (cellcode.equals(cellInfoCode)) {
						all.remove(i);
					}
				}							
			}
		}
		if (events!=null) {
			for (AlarmDto dto : events) {
				String cellcode = dto.getCell_code();
				for (int i = 0; i < all.size(); i++) {
					String cellInfoCode = all.get(i).getCell_code();
					if (cellcode.equals(cellInfoCode)) {
						all.remove(i);
					}
				}							
			}
		}
		return all;
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
			//dto.setApp_result(null);
			//List<AlarmDto> list = alarmDao.alarmLastHour(null);						
			dto.setApp_result(0);
			//list.clear();
			List<AlarmDto> list = alarmDao.alarmLastHour(dto);
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
	public PageDto<CellResultHistory> getCellList(String cellname,String type,String starttime,String endtime,String ttype,String result){
		
		List<CellResultHistory> sources;
		
		
		if (Constraints.DAY.equals(type)) {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellResultListLastDay(cellname,ttype,result);
		}else if (Constraints.WEEK.equals(type)) {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellResultListLastWeek(cellname,ttype,result);
		}else if (Constraints.MONTH.equals(type)) {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellResultListLastMonth(cellname,ttype,result);
		}else {
			//PageHelper.startPage(pageNum, pageSize);
			sources = alarmDao.cellResultListBySelect(cellname,starttime,endtime,ttype,result);
		}
		PageInfo<CellResultHistory> pageInfo = new PageInfo<>(sources);
		List<CellResultHistory> rows = new ArrayList<>();
		for (int i = 0; i < sources.size(); i++) {
			CellResultHistory dto = sources.get(i);
			rows.add(dto);
		}
		PageDto<CellResultHistory> pageDto = new PageDto<>();
		pageDto.setTotal(pageInfo.getTotal());
		pageDto.setRows(rows);
		return pageDto;
			
	}
	
	public CellResultHistoryDto getOneAlarm(String cellname,String yyyyMMdd){
		return alarmDao.getOneAlarm(cellname, yyyyMMdd);
	}

}
