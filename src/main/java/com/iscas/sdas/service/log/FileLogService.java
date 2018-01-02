package com.iscas.sdas.service.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iscas.sdas.common.BaseService;
import com.iscas.sdas.dao.FileLogDao;
import com.iscas.sdas.dto.FileLogDto;

@Service
public class FileLogService extends BaseService<FileLogDao, FileLogDto> {
	@Autowired 
	FileLogDao fileLogDao;
	
	public String lastUpdateTime(String type){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = fileLogDao.ovupdatetime(type);
		if (date!=null) {
			return format.format(date);
		}
		return null;
	}
}
