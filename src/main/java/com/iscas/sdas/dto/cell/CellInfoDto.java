package com.iscas.sdas.dto.cell;

import com.iscas.sdas.common.BaseDto;

/**
 * t_cell_info表
 * @author dongqun
 * 2017年11月17日上午11:03:10
 */
public class CellInfoDto extends BaseDto{

	private String cell_code;
	private String station_code;
	private String cell_name;
	private Integer in_used;
	public String getCell_code() {
		return cell_code;
	}
	public void setCell_code(String cell_code) {
		this.cell_code = cell_code;
	}
	public String getStation_code() {
		return station_code;
	}
	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}
	public Integer getIn_used() {
		return in_used;
	}
	public void setIn_used(Integer in_used) {
		this.in_used = in_used;
	}
	public String getCell_name() {
		return cell_name;
	}
	public void setCell_name(String cell_name) {
		this.cell_name = cell_name;
	}
	
	
}
