package com.iscas.sdas.dto.result;

import java.util.Date;

public class CellResultHistory {
	private String cellname;
	private String yyyymmdd;
	private Integer hour;
	private Integer result;
	private Date calcultime;

	public String getCellname() {
		return cellname;
	}

	public void setCellname(String cellname) {
		this.cellname = cellname;
	}

	public String getYyyymmdd() {
		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Date getCalcultime() {
		return calcultime;
	}

	public void setCalcultime(Date calcultime) {
		this.calcultime = calcultime;
	}
	
}
