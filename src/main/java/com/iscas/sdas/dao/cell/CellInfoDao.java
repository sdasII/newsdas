package com.iscas.sdas.dao.cell;

import com.iscas.sdas.common.BaseDao;
import com.iscas.sdas.dto.cell.CellInfoDto;

public interface CellInfoDao extends BaseDao<CellInfoDto> {
	
	 CellInfoDto getCellinfo(CellInfoDto dto);
	 
	 Integer clear();
	 
	 Integer getAllMonitorCounts();
	 
	 Integer clearHealthInfo();
	 
	 Integer clearHealthModel();
	 
	 Integer clearResultHistory();
	 
	 Integer clearCapacityTable();
	 
	 Integer cleatResultRt();
}
