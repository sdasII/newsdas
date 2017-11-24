package com.iscas.sdas.controller.cell;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.iscas.sdas.common.BaseController;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dto.cell.CellInfoDto;
import com.iscas.sdas.service.cell.CellInfoService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
@Controller
@RequestMapping("/cellinfo")
public class CellInfoController extends BaseController<CellInfoDto> {
	@Autowired
	CellInfoService cellInfoService;
	/**
	 * 页面跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cellinfo")
	public ModelAndView cellinfo(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("cell/cellinfo");
		return view;
	}
	/**
	 * 分页列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getlist")
	@ResponseBody
	public ModelMap getlist(@RequestParam(value = "currpage", required = true, defaultValue = "1") String num,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") String size,HttpServletRequest request){
		ModelMap map = new ModelMap();
		CellInfoDto cellDto = new CellInfoDto();
		String in_used=request.getParameter("type");
		if (!CommonUntils.isempty(in_used)) {
			cellDto.setIn_used(Integer.parseInt(in_used));
		}
		PageDto<CellInfoDto> pageDto=cellInfoService.getPageList(cellDto,num,size);
		map.addAttribute(Constraints.RESULT_ROW, pageDto);
		return map;
	}
	/**
	 * 设为使用状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/setUsed")
	@ResponseBody
	public ModelMap setUsed(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String[] ids =request.getParameter("ids").split(",");
		 for(String s:ids)
		 {	
			 CellInfoDto info=new CellInfoDto();
		 	 info.setCell_code(s);
			 CellInfoDto dto=cellInfoService.getCellinfo(info);
			 if(dto.getIn_used()==0){
				 dto.setIn_used(1);
				 cellInfoService.update(dto);
			 }
		 }
		return map;
	}
	/**
	 * 取消使用状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/clearUsed")
	@ResponseBody
	public ModelMap clearUsed(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String[] ids =request.getParameter("ids").split(",");
		 for(String s:ids)
		 {
			 CellInfoDto info=new CellInfoDto();
		 	 info.setCell_code(s);
			 CellInfoDto dto=cellInfoService.getCellinfo(info);
			 if(dto.getIn_used()==1){
				 dto.setIn_used(0);
				 cellInfoService.update(dto);
			 }
		 }
		return map;
	}
}
