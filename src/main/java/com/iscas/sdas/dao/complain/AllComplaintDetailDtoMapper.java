package com.iscas.sdas.dao.complain;

import java.util.List;

import com.iscas.sdas.dto.complain.AllComplaintDetailDtoWithBLOBs;

public interface AllComplaintDetailDtoMapper {
	
    int insert(AllComplaintDetailDtoWithBLOBs record);
    
    List<AllComplaintDetailDtoWithBLOBs> select();
}