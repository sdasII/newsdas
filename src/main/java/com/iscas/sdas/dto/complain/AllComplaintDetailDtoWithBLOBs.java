package com.iscas.sdas.dto.complain;

public class AllComplaintDetailDtoWithBLOBs extends AllComplaintDetailDto{
	
    private String servicerequesttype;

    private String complaintinfo;

    private String preinfocoll1value;

    private String terbrandmodel;

    private String ternettype;

    public String getServicerequesttype() {
        return servicerequesttype;
    }

    public void setServicerequesttype(String servicerequesttype) {
        this.servicerequesttype = servicerequesttype == null ? null : servicerequesttype.trim();
    }

    public String getComplaintinfo() {
        return complaintinfo;
    }

    public void setComplaintinfo(String complaintinfo) {
        this.complaintinfo = complaintinfo == null ? null : complaintinfo.trim();
    }

    public String getPreinfocoll1value() {
        return preinfocoll1value;
    }

    public void setPreinfocoll1value(String preinfocoll1value) {
        this.preinfocoll1value = preinfocoll1value == null ? null : preinfocoll1value.trim();
    }

    public String getTerbrandmodel() {
        return terbrandmodel;
    }

    public void setTerbrandmodel(String terbrandmodel) {
        this.terbrandmodel = terbrandmodel == null ? null : terbrandmodel.trim();
    }

    public String getTernettype() {
        return ternettype;
    }

    public void setTernettype(String ternettype) {
        this.ternettype = ternettype == null ? null : ternettype.trim();
    }
}