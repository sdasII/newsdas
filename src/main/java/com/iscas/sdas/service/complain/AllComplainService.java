package com.iscas.sdas.service.complain;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iscas.sdas.common.BaseService;
import com.iscas.sdas.dao.complain.AllComplaintDao;
import com.iscas.sdas.dao.complain.AllComplaintDetailDtoMapper;
import com.iscas.sdas.dto.complain.AllComplaintDetailDtoWithBLOBs;
import com.iscas.sdas.dto.complain.AllComplaintDto;

/**
 * 投诉部分业务处理
 * @author dongqun
 * 2017年11月29日上午10:56:41
 */
@Service
public class AllComplainService extends BaseService<AllComplaintDao, AllComplaintDto> {
	
	@Autowired
	AllComplaintDetailDtoMapper allComplaintDetailDtoMapper;
	
	public boolean insertDetails(List<AllComplaintDetailDtoWithBLOBs> dtos){
		for (AllComplaintDetailDtoWithBLOBs dto : dtos) {
			try {
				allComplaintDetailDtoMapper.insert(dto);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
}
