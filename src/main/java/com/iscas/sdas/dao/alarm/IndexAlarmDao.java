package com.iscas.sdas.dao.alarm;

import java.util.List;

import com.iscas.sdas.common.BaseDao;
import com.iscas.sdas.dto.alarm.IndexAlarmDto;

public interface IndexAlarmDao extends BaseDao<IndexAlarmDto>{
	
	/**
	 * 获取最近一天指标预警
	 * @return
	 */
	List<IndexAlarmDto> getPageListCurrentDay();
	/**
	 * 指标预警页面对应小区获取最近一天指标预警
	 * @return
	 */
	List<IndexAlarmDto> getLastDay(IndexAlarmDto indexAlarmDto);
	
	
}
