package com.iscas.sdas.dto.cell;
/**
 * t_cell_info表
 * @author dongqun
 * 2017年11月17日上午11:03:10
 */
public class CellInfoDto {

	private String cell_code;
	private String station_code;
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
	
	
}
