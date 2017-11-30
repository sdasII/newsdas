package com.iscas.sdas.dao.complain;

import com.iscas.sdas.dto.complain.AllComplaintDetailDtoWithBLOBs;

public interface AllComplaintDetailDtoMapper {
	
    int insert(AllComplaintDetailDtoWithBLOBs record);
}