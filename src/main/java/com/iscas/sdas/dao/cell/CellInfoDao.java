package com.iscas.sdas.dao.cell;

import java.util.List;

import com.iscas.sdas.common.BaseDao;
import com.iscas.sdas.dto.cell.CellInfoDto;

public interface CellInfoDao extends BaseDao<CellInfoDto> {
	
	 CellInfoDto getCellinfo(CellInfoDto dto);
	 
	 Integer clear();
	 
	 Integer getAllMonitorCounts();
	 
	 List<CellInfoDto> getAllMonitorCells();
	 
	 Integer clearHealthInfo();
	 
	 Integer clearHealthModel();
	 
	 Integer clearResultHistory();
	 
	 Integer resetCapacityTable();
	 
	 Integer cleatResultRt();
}
