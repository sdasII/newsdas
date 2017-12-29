package com.iscas.sdas.dao.cell;

import com.iscas.sdas.common.BaseDao;
import com.iscas.sdas.dto.cell.CellInfoDto;

public interface CellInfoDao extends BaseDao<CellInfoDto> {
	
	 CellInfoDto getCellinfo(CellInfoDto dto);
}
