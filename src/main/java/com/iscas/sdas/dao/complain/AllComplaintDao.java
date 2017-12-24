package com.iscas.sdas.dao.complain;

import com.iscas.sdas.common.BaseDao;
import com.iscas.sdas.dto.complain.AllComplaintDto;

public interface AllComplaintDao extends BaseDao<AllComplaintDto>{

	long allCounts();
}