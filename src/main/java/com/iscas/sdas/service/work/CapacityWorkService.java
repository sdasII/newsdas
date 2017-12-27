package com.iscas.sdas.service.work;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iscas.sdas.common.BaseService;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dao.work.CapacityWorkDao;
import com.iscas.sdas.dto.work.CapacityWorkDto;
import com.iscas.sdas.util.Constraints;

@Service
public class CapacityWorkService extends BaseService<CapacityWorkDao, CapacityWorkDto>{

	@Autowired
	CapacityWorkDao capacityWorkDao;
	
	/**
	 * 获取工单验证的数据
	 * @author dongqun
	 * 2017年12月27日下午2:23:49
	 * @param workDto
	 * @param type
	 * @param starttime
	 * @param endtime
	 * @param num
	 * @param size
	 * @return
	 */
	public PageDto<CapacityWorkDto> getPageList(CapacityWorkDto workDto,String type,String starttime,String endtime,String num,String size){
		List<CapacityWorkDto> works = null;
		int pageNum = Integer.parseInt(num);
		int pageSize = Integer.parseInt(size);
	
		if (Constraints.DAY.equals(type)) {
			PageHelper.startPage(pageNum, pageSize);
			works = capacityWorkDao.getListWhithinLastDay(workDto);
		}else if (Constraints.WEEK.equals(type)) {
			PageHelper.startPage(pageNum, pageSize);
			works = capacityWorkDao.getListWhithinLastWeek(workDto);
		}else if (Constraints.MONTH.equals(type)) {
			PageHelper.startPage(pageNum, pageSize);
			works = capacityWorkDao.getListWhithinLastMonth(workDto);
		}else {
			PageHelper.startPage(pageNum, pageSize);
			works = capacityWorkDao.getListWhithinSelect(workDto, starttime, endtime);
		}
		PageInfo<CapacityWorkDto> pageInfo = new PageInfo<>(works);
		List<CapacityWorkDto> rows = new ArrayList<>();
		for (int i = 0; i < works.size(); i++) {
			CapacityWorkDto dto1 = works.get(i);
			rows.add(dto1);
		}
		PageDto<CapacityWorkDto> pageDto = new PageDto<>();
		pageDto.setTotal(pageInfo.getTotal());
		pageDto.setRows(rows);
		return pageDto;
	}
	/**
	 * 获取工单验证的数据——不分页
	 * @author dongqun
	 * 2017年12月27日下午2:34:22
	 * @param workDto
	 * @param type
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<CapacityWorkDto> getAllList(CapacityWorkDto workDto,String type,String starttime,String endtime){
		List<CapacityWorkDto> works = null;
	
		if (Constraints.DAY.equals(type)) {
			works = capacityWorkDao.getListWhithinLastDay(workDto);
		}else if (Constraints.WEEK.equals(type)) {
			works = capacityWorkDao.getListWhithinLastWeek(workDto);
		}else if (Constraints.MONTH.equals(type)) {
			works = capacityWorkDao.getListWhithinLastMonth(workDto);
		}else {
			works = capacityWorkDao.getListWhithinSelect(workDto, starttime, endtime);
		}
		return works;
	}
	
	public List<String> getbelongaera(){
		return capacityWorkDao.getbelongaera();
	}
	
	
}
