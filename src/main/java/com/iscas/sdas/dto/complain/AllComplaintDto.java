package com.iscas.sdas.dto.complain;

import java.util.Date;

import com.iscas.sdas.common.BaseDto;

public class AllComplaintDto extends BaseDto{
	
    private String depend_date;

    private Date record_time;

    private String ascription_city;

    private String phone_number;

    private String work_city;

    private String work_scene;

    private String work_cellname;

    private String live_city;

    private String live_scene;

    private String live_cellname;

    private Double distance;

    private String live_cgi1;

    private String live_cgi2;

    private String live_cgi3;

    private String live_cellname1;

    private String live_cellname2;

    private String live_cellname3;

    private String workcgi;

    private String livecgi;

    

    public String getDepend_date() {
		return depend_date;
	}

	public void setDepend_date(String depend_date) {
		this.depend_date = depend_date;
	}

	public Date getRecord_time() {
		return record_time;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

	public String getAscription_city() {
		return ascription_city;
	}

	public void setAscription_city(String ascription_city) {
		this.ascription_city = ascription_city;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getWork_city() {
		return work_city;
	}

	public void setWork_city(String work_city) {
		this.work_city = work_city;
	}

	public String getWork_scene() {
		return work_scene;
	}

	public void setWork_scene(String work_scene) {
		this.work_scene = work_scene;
	}

	public String getWork_cellname() {
		return work_cellname;
	}

	public void setWork_cellname(String work_cellname) {
		this.work_cellname = work_cellname;
	}

	public String getLive_city() {
		return live_city;
	}

	public void setLive_city(String live_city) {
		this.live_city = live_city;
	}

	public String getLive_scene() {
		return live_scene;
	}

	public void setLive_scene(String live_scene) {
		this.live_scene = live_scene;
	}

	public String getLive_cellname() {
		return live_cellname;
	}

	public void setLive_cellname(String live_cellname) {
		this.live_cellname = live_cellname;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getLive_cgi1() {
		return live_cgi1;
	}

	public void setLive_cgi1(String live_cgi1) {
		this.live_cgi1 = live_cgi1;
	}

	public String getLive_cgi2() {
		return live_cgi2;
	}

	public void setLive_cgi2(String live_cgi2) {
		this.live_cgi2 = live_cgi2;
	}

	public String getLive_cgi3() {
		return live_cgi3;
	}

	public void setLive_cgi3(String live_cgi3) {
		this.live_cgi3 = live_cgi3;
	}

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

	public String getWorkcgi() {
        return workcgi;
    }

    public void setWorkcgi(String workcgi) {
        this.workcgi = workcgi == null ? null : workcgi.trim();
    }

    public String getLivecgi() {
        return livecgi;
    }

    public void setLivecgi(String livecgi) {
        this.livecgi = livecgi == null ? null : livecgi.trim();
    }

	@Override
	public boolean equals(Object obj) {
		AllComplaintDto allComplaintDto = (AllComplaintDto)obj;
		boolean result = true;
		if (this.depend_date!=null) {
			if (!this.depend_date.equals(allComplaintDto.getDepend_date())) {
				return false;
			}
		}else {
			if (allComplaintDto.getDepend_date()!=null) {
				return false;
			}
		}
		if (this.ascription_city!=null) {
			if (!this.ascription_city.equals(allComplaintDto.getAscription_city())) {
				return false;
			}
		}else {
			if (allComplaintDto.getAscription_city()!=null) {
				return false;
			}
		}
		if (this.phone_number!=null) {
			if (!this.phone_number.equals(allComplaintDto.getPhone_number())) {
				return false;
			}
		}else {
			if (allComplaintDto.getPhone_number()!=null) {
				return false;
			}
		}
		if (this.work_cellname!=null) {
			if (!this.work_cellname.equals(allComplaintDto.getWork_cellname())) {
				return false;
			}
		}else {
			if (allComplaintDto.getWork_cellname()!=null) {
				return false;
			}
		}
		if (this.work_city != null) {
			if (!this.work_city.equals(allComplaintDto.getWork_city())) {
				return false;
			}
		}else {
			if (allComplaintDto.getWork_city()!=null) {
				return false;
			}
		}
		if (this.work_scene != null) {
			if (!this.work_scene.equals(allComplaintDto.getWork_scene())) {
				return false;
			}
		}else {
			if (allComplaintDto.getWork_scene()!=null) {
				return false;
			}
		}
		if (this.live_cellname != null) {
			if (!this.live_cellname.equals(allComplaintDto.getLive_cellname())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_cellname()!=null) {
				return false;
			}
		}
		if (this.live_cellname1 != null) {
			if (!this.live_cellname1.equals(allComplaintDto.getLive_cellname1())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_cellname1()!=null) {
				return false;
			}
		}
		if (this.live_cellname2 != null) {
			if (!this.live_cellname2.equals(allComplaintDto.getLive_cellname2())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_cellname2()!=null) {
				return false;
			}
		}
		if (this.live_cellname3 != null) {
			if (!this.live_cellname3.equals(allComplaintDto.getLive_cellname3())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_cellname3()!=null) {
				return false;
			}
		}
		if (this.live_city != null) {
			if (!this.live_city.equals(allComplaintDto.getLive_city())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_city()!=null) {
				return false;
			}
		}
		if (this.live_scene != null) {
			if (!this.live_scene.equals(allComplaintDto.getLive_scene())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_scene()!=null) {
				return false;
			}
		}
		if (this.livecgi != null) {
			if (!this.livecgi.equals(allComplaintDto.getLivecgi())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLivecgi()!=null) {
				return false;
			}
		}
		if (this.live_cgi1 != null) {
			if (!this.live_cgi1.equals(allComplaintDto.getLive_cgi1())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_cgi1()!=null) {
				return false;
			}
		}
		if (this.live_cgi2 != null) {
			if (!this.live_cgi2.equals(allComplaintDto.getLive_cgi2())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_cgi2()!=null) {
				return false;
			}
		}
		if (this.live_cgi3 != null) {
			if (!this.live_cgi3.equals(allComplaintDto.getLive_cgi3())) {
				return false;
			}
		}else {
			if (allComplaintDto.getLive_cgi3()!=null) {
				return false;
			}
		}
		if (this.workcgi!=null) {
			if (!this.workcgi.equals(allComplaintDto.getWorkcgi())) {
				return false;
			}
		}else{
			if (allComplaintDto.getWorkcgi()!=null) {
				return false;
			}
		}
		if (this.distance!=null) {
			if (!this.distance.equals(allComplaintDto.getDistance())) {
				return false;
			}
		}else {
			if (allComplaintDto.getDistance()!=null) {
				return false;
			}
		}
		/*if (this.record_time!=null) {
			if (allComplaintDto.getRecord_time()!=null) {
				if (!this.record_time.toString().equals(allComplaintDto.getRecord_time().toString())) {
					System.out.println(this.record_time.toString());
					System.out.println(allComplaintDto.getRecord_time().toString());
					return false;
				}
			}else {
				return false;
			}
			
		}else {
			if (allComplaintDto.getRecord_time()!=null) {
				return false;
			}
		}*/
		
					
		return result;
		
	}
    
}