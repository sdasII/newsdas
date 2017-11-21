package com.iscas.sdas.util;

public enum FTPStatus {
	Remote_File_Noexist,//"文件不存在";
	Local_Bigger_Remote,//本地文件大于远程文件
	Download_From_Break_Success,//续传下载成功
	Download_From_Break_Failed,//续传下载失败
	Download_New_Success,//下载成功
	Download_New_Failed,//下载失败
	Create_Directory_Fail,//远程目录创建失败
	Upload_From_Break_Success,
	Upload_From_Break_Failed,
	Upload_New_File_Success,
	Upload_New_File_Failed,
	Create_Directory_Success,
	Delete_Remote_Faild,
	File_Exits,
	Remote_Bigger_Local,
	CONNECT_FAIL,
	File_No_Format;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
}
