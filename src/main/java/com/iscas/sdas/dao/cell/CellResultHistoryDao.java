package com.iscas.sdas.dao.cell;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iscas.sdas.dto.cell.CellResultHistoryDto;
/**
 * 小区健康度判别结果历史数据
 * @author dongqun
 * 2017年11月16日上午10:27:31
 */
public interface CellResultHistoryDao {
	/**
	 * 最后一天的数据
	 * @return
	 */
    List<CellResultHistoryDto> historyWithinLastDay(String cellname);
    /**
     * 最近7天的数据
     * @return
     */
    List<CellResultHistoryDto> historyWithinLastWeek(String cellname);
    /**
     * 最近月
     * @param cellname
     * @return
     */
    List<CellResultHistoryDto> historyWithinLastMonth(String cellname);
    /**
     * 一定时间段的数据
     * @param cellname
     * @param starttime
     * @param endtime
     * @return
     */
    List<CellResultHistoryDto> historyWithinSelect(@Param("cellname")String cellname,@Param("start")String starttime,@Param("end")String endtime);
    /**
     * 按月查询数据
     * @param yyyyMM
     * @return
     */
    List<CellResultHistoryDto> resultByMonth(String yyyyMM);
}