package com.iscas.sdas.dto.cell;

import com.iscas.sdas.common.BaseDto;

/**
 * t_cell_info表
 * 
 * @author dongqun 2017年11月17日上午11:03:10
 */
public class CellInfoDto extends BaseDto {

	private String cell_code;
	private String station_code;
	private String cell_name;
	private Integer in_used;
	private String cell_coordinate;
	private String cell_info;
	private String state_type_1, state_type_2;
	private String normal_model;
	private String station_latitude;
	private String station_longitude;

	private Integer app_result;
	
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

	public String getCell_coordinate() {
		return cell_coordinate;
	}

	public void setCell_coordinate(String cell_coordinate) {
		this.cell_coordinate = cell_coordinate;
	}

	public String getCell_info() {
		return cell_info;
	}

	public void setCell_info(String cell_info) {
		this.cell_info = cell_info;
	}

	public String getState_type_1() {
		return state_type_1;
	}

	public void setState_type_1(String state_type_1) {
		this.state_type_1 = state_type_1;
	}

	public String getState_type_2() {
		return state_type_2;
	}

	public void setState_type_2(String state_type_2) {
		this.state_type_2 = state_type_2;
	}

	public String getNormal_model() {
		return normal_model;
	}

	public void setNormal_model(String normal_model) {
		this.normal_model = normal_model;
	}

	public String getStation_latitude() {
		return station_latitude;
	}

	public void setStation_latitude(String station_latitude) {
		this.station_latitude = station_latitude;
	}

	public String getStation_longitude() {
		return station_longitude;
	}

	public void setStation_longitude(String station_longitude) {
		this.station_longitude = station_longitude;
	}

	public Integer getApp_result() {
		return app_result;
	}

	public void setApp_result(Integer app_result) {
		this.app_result = app_result;
	}
	
}
