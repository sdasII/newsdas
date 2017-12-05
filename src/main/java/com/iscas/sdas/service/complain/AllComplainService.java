package com.iscas.sdas.service.complain;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
	public boolean insertCell(List<AllComplaintDto> dtos){
		boolean result = true;
		int pageSize = 1000;
		int page = 0;	
		/*
		 * 第一页前1000条数据
		 */
		AllComplaintDto allComplaintDto = new AllComplaintDto();	
		PageHelper.startPage(page, pageSize);	
		List<AllComplaintDto> pagedata_firtst = dao.getPageList(allComplaintDto);
		PageInfo<AllComplaintDto> pageInfo = new PageInfo<>(pagedata_firtst);
		long count = pageInfo.getTotal();
		if (count!=0) {
			for (; page*1000 < count; page++) {
				if (page==0) {
					for (int i = 0; i < dtos.size(); i++) {
						AllComplaintDto insertDto = dtos.get(i);
						int compare = 0;
						for (int j = 0 ; j < pagedata_firtst.size();j++) {
							if (!insertDto.equals(pagedata_firtst.get(j))) {
								compare++;
							}
							if (compare==pagedata_firtst.size()) {
								allComplaintDao.insert(insertDto);
								//System.out.println("插入第"+i+"条");
							}
						}
					}
					
										
				}else {
					AllComplaintDto allComplaintDto2 = new AllComplaintDto();	
					PageHelper.startPage(page, pageSize);	
					List<AllComplaintDto> pagedata_next = dao.getPageList(allComplaintDto2);
					boolean flag = true;
					for (AllComplaintDto dto : dtos) {
						int compare = 0;
						for (int k = 0 ; k < pagedata_next.size();k++) {
							if (!dto.equals(pagedata_next.get(k))) {
								compare++;
							}
							if (compare==pagedata_next.size()) {
								allComplaintDao.insert(dto);
							}
						}
					}
				}
					
			}
		} else {
			for (AllComplaintDto dto : dtos) {
				try {
					allComplaintDao.insert(dto);
				} catch (Exception e) {
					result = false;
					continue;
				}
			}
		}

		
			
		return result;

	}
}
