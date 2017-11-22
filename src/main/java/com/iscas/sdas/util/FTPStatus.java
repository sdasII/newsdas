package com.iscas.sdas.util;

public enum FTPStatus {
	Remote_File_Noexist("文件不存在"),//"预下载文件不存在";
	Local_Bigger_Remote("本地文件大于远程文件"),//本地文件大于远程文件
	Download_From_Break_Success("续传下载成功"),//续传下载成功
	Download_From_Break_Failed("续传下载失败"),//续传下载失败
	Download_New_Success("新建下载成功"),//下载成功
	Download_New_Failed("新建下载失败"),//下载失败
	Create_Directory_Fail("创建目录失败"),//远程目录创建失败
	Delete_Remote_Faild("删除目录失败"),
	Upload_From_Break_Success("断点续传成功"),
	Upload_From_Break_Failed("断点续传失败"),
	Upload_New_File_Success("新建上传成功"),
	Upload_New_File_Failed("新建上传失败"),
	Create_Directory_Success("创建目录成功"),	
	File_Exits("文件已存在"),
	Remote_Bigger_Local("远程文件大于本地文件"),//远端文件比即将上传的要大，无须上传或重新命名要上传的文件名
	CONNECT_FAIL("FTP连接失败"),
	File_No_Format("文件不规范");

	String value;
	private FTPStatus(String value) {
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return this.value;
	}
	
	
}
