package com.iscas.sdas.dao.work;

import java.util.List;

import com.iscas.sdas.dto.work.AllCapacityWorkDto;

public interface AllCapacityWorkDao {
	
	int delete();
	
    int deleteByPrimaryKey(Integer alarmId);

    int insert(AllCapacityWorkDto record);

    int insertSelective(AllCapacityWorkDto record);

    AllCapacityWorkDto selectByPrimaryKey(Integer alarmId);

    int updateByPrimaryKeySelective(AllCapacityWorkDto record);

    int updateByPrimaryKey(AllCapacityWorkDto record);
    
    List<AllCapacityWorkDto> isexist(Integer alarm_id);
}