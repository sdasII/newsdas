package com.iscas.sdas.service.cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iscas.sdas.common.BaseService;
import com.iscas.sdas.dao.cell.CellInfoDao;
import com.iscas.sdas.dto.cell.CellInfoDto;
@Service
public class CellInfoService extends BaseService<CellInfoDao, CellInfoDto> {
	
	@Autowired
	CellInfoDao cellInfoDao;
	
	public CellInfoDto getCellinfo(CellInfoDto dto){
		return cellInfoDao.getCellinfo(dto);
	}
	/**
	 * 配置文件中监控小区的数量
	 * @author dongqun
	 * 2018年1月2日下午1:52:45
	 * @return
	 */
	public int allMonitorCounts(){
		return cellInfoDao.getAllMonitorCounts();
	}
}
