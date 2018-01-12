package com.iscas.sdas.dao.model;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iscas.sdas.dto.model.IndexModel;

public interface IndexModelDao {
	
	IndexModel getCellIndexContentByMonth(@Param("cellname")String cellname,@Param("indexcode")String indexcode,@Param("yyyyMM")String yyyyMM);
	
	List<IndexModel> getCellIndexContents(@Param("cellname")String cellname,@Param("indexcode")String indexcode);
	
}
