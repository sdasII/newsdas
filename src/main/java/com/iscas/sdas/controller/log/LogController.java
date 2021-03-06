package com.iscas.sdas.controller.log;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dto.FileLogDto;
import com.iscas.sdas.service.log.FileLogService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;

@Controller
@RequestMapping("/log")
public class LogController {
	@Autowired
	FileLogService fileLogService;
	
	@RequestMapping("/file/page")
	public ModelAndView fileLogPage(HttpServletRequest request){
		String type=request.getParameter("type");
		ModelAndView mv=new ModelAndView("/data/log");
		if (!CommonUntils.isempty(type)) {
			mv.addObject("type", type);
		}
		else{
			mv.addObject("type", "");
		}
		return mv;
	}
	
	@RequestMapping("/file/list")
	@ResponseBody
	public ModelMap fileLog(@RequestParam(value = "currpage", required = true, defaultValue = "1") String num,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") String size,
			HttpServletRequest request) {
		ModelMap map = new ModelMap();
		FileLogDto dto = new FileLogDto();
		String type=request.getParameter("type").trim();
		String result=request.getParameter("result");
		if("4".equals(type)){
			dto.setType("投诉工单数据");
		}else{
			if (!CommonUntils.isempty(type)) {
				dto.setType(type);
			}
			if (!CommonUntils.isempty(result)) {
				dto.setResult(Integer.parseInt(result));
			}
		}
		PageDto<FileLogDto> pageDto = fileLogService.getPageList(dto, num, size);
		map.addAttribute(Constraints.RESULT_ROW, pageDto);
		return map;
	}
}
