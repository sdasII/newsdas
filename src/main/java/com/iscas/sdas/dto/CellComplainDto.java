package com.iscas.sdas.dto;

import java.util.Date;

/**
 * 投诉信息dto
 * 
 * @author dongqun 2017年10月13日上午11:14:30
 */
public class CellComplainDto {

	private Date recordtime;
	private String phonenumber;
	private String live_cellname1, live_cellname2, live_cellname3;
	private String complaintdetailinfo;
	private String servicerequesttype;

	public String getLive_cellname1() {
		return live_cellname1;
	}

	public void setLive_cellname1(String live_cellname1) {
		this.live_cellname1 = live_cellname1;
	}

	public String getLive_cellname2() {
		return live_cellname2;
	}

	public void setLive_cellname2(String live_cellname2) {
		this.live_cellname2 = live_cellname2;
	}

	public String getLive_cellname3() {
		return live_cellname3;
	}

	public void setLive_cellname3(String live_cellname3) {
		this.live_cellname3 = live_cellname3;
	}

	public String getServicerequesttype() {
		return servicerequesttype;
	}

	public void setServicerequesttype(String servicerequesttype) {
		this.servicerequesttype = servicerequesttype;
	}

	public Date getRecordtime() {
		return recordtime;
	}

	public void setRecordtime(Date recordtime) {
		this.recordtime = recordtime;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getComplaintdetailinfo() {
		return complaintdetailinfo;
	}

	public void setComplaintdetailinfo(String complaintdetailinfo) {
		this.complaintdetailinfo = complaintdetailinfo;
	}
}
