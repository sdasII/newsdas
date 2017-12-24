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
	public boolean insertDetails(List<AllComplaintDetailDtoWithBLOBs> dtos){
		boolean result = true;
		
		for (AllComplaintDetailDtoWithBLOBs dto : dtos) {
			try {
				allComplaintDetailDtoMapper.insert(dto);				
			} catch (Exception e) {
				//e.printStackTrace();
				result = false;
				continue;
			}
		}
		return result;
	}
	/**
	 * 插入投诉常驻小区
	 * @param dtos
	 * @return
	 */
	public boolean insertCell(List<AllComplaintDto> dtos) {
		boolean result = true;
		int pageSize = 1000;
		for (int i = 0; i < dtos.size(); i++) {
			
			
			//long size = dao.allCounts();
			try {
				AllComplaintDto insertDto = dtos.get(i);
				allComplaintDao.insert(insertDto);
			} catch (Exception e) {
				result = false;
				continue;
			}
			/*int compareCount = 0;// 比较次数
			for (int page = 0; page * 1000 < size; page++) {
				AllComplaintDto dto = new AllComplaintDto();
				PageHelper.startPage(page, pageSize);
				List<AllComplaintDto> pagedata = dao.getPageList(dto);
				for (int j = 0; j < pagedata.size(); j++) {
					if (!insertDto.equals(pagedata.get(j))) {
						compareCount++;
					}
				}
			}
			if (compareCount == size) {
				allComplaintDao.insert(insertDto);
			}*/

		}
		return result;

	}
}
