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
	@Autowired
	AllComplaintDao allComplaintDao;
	/**
	 * 插入投诉情况
	 * @param dtos
	 * @return
	 */
	public void insertDetails(List<AllComplaintDetailDtoWithBLOBs> dtos){

		
		for (AllComplaintDetailDtoWithBLOBs dto : dtos) {
			 	String number = dto.getNumber();
			 	List<AllComplaintDetailDtoWithBLOBs> list = allComplaintDetailDtoMapper.select(number);
				if (list==null) {
					allComplaintDetailDtoMapper.insert(dto);
				}else if (list!=null && list.size()==0) {
					allComplaintDetailDtoMapper.insert(dto);
				}										 
		}

	}
	/**
	 * 插入投诉常驻小区
	 * @param dtos
	 * @return
	 */
	public void insertCell(List<AllComplaintDto> dtos) {
		for (int i = 0; i < dtos.size(); i++) {									
			try {
				AllComplaintDto insertDto = dtos.get(i);
				allComplaintDao.insert(insertDto);
			} catch (Exception e) {
				continue;
			}			
		}
	}
}
