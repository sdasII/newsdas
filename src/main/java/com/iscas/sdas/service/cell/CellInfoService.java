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
}
