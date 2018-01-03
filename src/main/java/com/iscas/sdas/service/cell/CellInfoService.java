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
	/**
	 * 清空表
	 * @author dongqun
	 * 2018年1月2日下午2:33:25
	 * @return
	 */
	public int clearTable(){
		return cellInfoDao.clear();
	}
	/**
	 * 重新配置后，清空数据重新计算
	 * @author dongqun
	 * 2018年1月2日下午7:42:02
	 */
	public void restartData(){
		cellInfoDao.resetCapacityTable();
		cellInfoDao.clearHealthInfo();
		cellInfoDao.clearHealthModel();
		cellInfoDao.clearResultHistory();
		cellInfoDao.cleatResultRt();
	}
}
