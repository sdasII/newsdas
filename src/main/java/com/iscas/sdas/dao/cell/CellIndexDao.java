package com.iscas.sdas.dao.cell;

import org.apache.ibatis.annotations.Param;

import com.iscas.sdas.dto.cell.BaseGroupIndex;
import com.iscas.sdas.dto.cell.BaseIndex;
import com.iscas.sdas.dto.cell.BaseIndex2;
import com.iscas.sdas.dto.cell.BaseWeight;

public interface CellIndexDao {
	
	BaseWeight getCellWeight(String cellname);
	
	BaseIndex getCellIndexContent(@Param("cellname")String cellname,@Param("indexcode")String indexcode);

	BaseGroupIndex getGroupIndexContent(@Param("grouptype")String grouptype,@Param("indexcode")String indexcode);
	
	BaseIndex2 getCellIndexContentByMonth(@Param("cellname")String cellname,@Param("indexcode")String indexcode,@Param("yyyyMM")String yyyyMM);
}
