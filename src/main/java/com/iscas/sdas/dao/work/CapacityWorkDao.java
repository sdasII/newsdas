package com.iscas.sdas.dao.work;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iscas.sdas.common.BaseDao;
import com.iscas.sdas.dto.work.CapacityWorkDto;

public interface CapacityWorkDao extends BaseDao<CapacityWorkDto>{
	/**
	 * 从可疑工单表中获取最近一天的数据
	 * @return
	 */
	List<CapacityWorkDto> getListWhithinLastDay(@Param("work")CapacityWorkDto capacityWorkDto);
	/**
	 * 从可疑工单表中获取最近一周的数据
	 * @author dongqun
	 * 2017年12月27日下午2:12:35
	 * @param cellname
	 * @return
	 */
	List<CapacityWorkDto> getListWhithinLastWeek(@Param("work")CapacityWorkDto capacityWorkDto);
	/**
	 * 从可疑工单表中获取最近一月的数据
	 * @author dongqun
	 * 2017年12月27日下午2:12:55
	 * @param cellname
	 * @return
	 */
	List<CapacityWorkDto> getListWhithinLastMonth(@Param("work")CapacityWorkDto capacityWorkDto);
	/**
	 * 从可疑工单表中获取任意时间段的数据
	 * @author dongqun
	 * 2017年12月27日下午2:13:10
	 * @param work
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	List<CapacityWorkDto> getListWhithinSelect(@Param("work")CapacityWorkDto work,@Param("starttime")String starttime,@Param("endtime")String endtime);
	/**
	 * 可疑工单表全部区域
	 * @author dongqun
	 * 2017年12月27日下午2:27:56
	 * @return
	 */
	List<String> getbelongaera();
}
