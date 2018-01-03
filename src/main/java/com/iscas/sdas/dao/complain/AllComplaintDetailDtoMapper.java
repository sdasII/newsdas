package com.iscas.sdas.dao.complain;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iscas.sdas.dto.complain.AllComplaintDetailDtoWithBLOBs;

public interface AllComplaintDetailDtoMapper {
	
    int insert(AllComplaintDetailDtoWithBLOBs record);
    
    List<AllComplaintDetailDtoWithBLOBs> select(@Param("number")String number);
}