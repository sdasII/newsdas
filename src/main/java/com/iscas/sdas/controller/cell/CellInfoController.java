package com.iscas.sdas.controller.cell;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.iscas.sdas.common.BaseController;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dto.cell.CellInfoDto;
import com.iscas.sdas.service.cell.CellInfoService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
import com.iscas.sdas.util.FileExport;
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
	
	@RequestMapping("/export")
	public  void resultExport(HttpServletRequest request,HttpServletResponse response){
        try {
        	CellInfoDto cellInfoDto = new CellInfoDto();       	
    		List<CellInfoDto> list = cellInfoService.getalllist(cellInfoDto);
    		Map<String,String> headMap = new LinkedHashMap<>();
    		JSONArray sourcesJson = null;
    		headMap.put("cell_name", "小区名称");
    		headMap.put("station_code", "所属基站");
    		headMap.put("in_used", "是否使用");	
        	if (list!=null) {
        		sourcesJson = JSONArray.parseArray(JSON.toJSONString(list));
			} 
        	String title= "小区配置表导出";
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            FileExport.exportExcelX(title, headMap, sourcesJson, null, 0, os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"); 
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((title + ".xlsx").getBytes(), "iso-8859-1"));
            response.setContentLength(content.length);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);

            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
