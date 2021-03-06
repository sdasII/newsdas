package com.iscas.sdas.util;

public class Constraints {

	public static String RESULT_ROW = "rows";	
	public static String RESULT_WORKS = "works";
	public static String RESULT_SUCCESS = "success";
	public static String RESULT_FAIL = "fail";
	public static String RESULT_UNKNOWN = "unkown";
	public static final String BTN_SEARCH = "查询";
	public static final String BTN_INSERT = "新增";
	public static final String BTN_DELETE = "删除";
	public static final String BTN_UPDATE = "更新";
	public static final String BTN_IMPORT = "导入";
	public static final String BTN_EXPORT = "导出";
	public static final String BTN_CANCLE = "取消";
	public static final String BTN_SAVE = "保存";
	public static final String BTN_EDIT = "编辑";
	public static final String BTN_CUSTOM = "自定义";
	public static final String ROLE_ADMIN = "管理员";
	public static final String ROLE_USER = "普通用户";
	public static final String SESSION_FTP_STATUS = "FTPObj";
	public static final String DAY = "day";
	public static final String WEEK = "week";
	public static final String MONTH = "month";
	public static final String SELECT = "select";
	
	public static ThreadLocal<Integer> global_upload_progress = new ThreadLocal<Integer>();
	
	
	public static Integer getGlobal_upload_progress() {
		return global_upload_progress.get();
	}
	public static void setGlobal_upload_progress(Integer progress) {
		global_upload_progress.set(progress);
	}
	public static void removeGlobal_upload_progress(){
		global_upload_progress.remove();
	}
	
	public static int ftp_upload_progress;
	
	public synchronized static int getFtp_upload_progress() {
		return ftp_upload_progress;
	}
	public synchronized static void setFtp_upload_progress(double ftp_upload_progress) {
		Constraints.ftp_upload_progress = (int)ftp_upload_progress;
	}
	
	

}
