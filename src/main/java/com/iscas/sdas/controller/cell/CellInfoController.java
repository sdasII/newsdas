package com.iscas.sdas.controller.cell;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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
import com.iscas.sdas.dto.FileLogDto;
import com.iscas.sdas.dto.cell.CellInfoDto;
import com.iscas.sdas.service.cell.CellInfoService;
import com.iscas.sdas.service.log.FileLogService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
import com.iscas.sdas.util.FileExport;
import com.iscas.sdas.util.FileImport;
/**
 * 小区配置
 * @author dongqun
 * 2018年1月2日下午1:41:37
 */
@Controller
@RequestMapping("/cellinfo")
public class CellInfoController extends BaseController<CellInfoDto> {
	@Autowired
	CellInfoService cellInfoService;
	@Autowired
	FileLogService fileLogService;
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
		String cellname = request.getParameter("cellname");
		if (!CommonUntils.isempty(in_used)) {
			cellDto.setIn_used(Integer.parseInt(in_used));
		}
		if (!CommonUntils.isempty(cellname)) {
			cellDto.setCell_code(cellname);
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
    		headMap.put("cell_code", "cell_code");
    		headMap.put("station_code", "station_code");
    		headMap.put("in_used", "in_used");	
    		headMap.put("cell_name", "cell_name");	
    		headMap.put("cell_coordinate", "cell_coordinate");	
    		headMap.put("cell_info", "cell_info");	
    		headMap.put("state_type_1", "state_type_1");	
    		headMap.put("state_type_2", "state_type_2");	
    		headMap.put("normal_model", "normal_model");
    		headMap.put("station_longitude", "station_longitude");
    		headMap.put("station_latitude", "station_latitude");
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
	
	@RequestMapping("/import")
	public ModelAndView importSet(HttpServletRequest request) {
		ModelAndView modelMap = new ModelAndView("cell/cellinfo");		
		List<CellInfoDto> result = new ArrayList<>();
		String path = null;
		FileLogDto fileLogDto = new FileLogDto();
		long starttime = System.currentTimeMillis();
		fileLogDto.setStarttime(new Date());
		fileLogDto.setType("配置文件");
		try {
			path = CommonUntils.SignalFileImprot(request, fileLogDto);
		} catch (Exception e1) {
			e1.printStackTrace();
			fileLogDto.setResult(0);
			modelMap.addObject("success", Constraints.RESULT_FAIL + ":导入失败！");
		}
		if (path != null) {
				int rows = FileImport.tablerows(path);
				for (int i = 0; i < rows; i++) {
					CellInfoDto dto = new CellInfoDto();
					result.add(dto);
				}
				try {
					FileImport.settingFileImportWork(path, result);// 将excel映射为对象
					try {
						cellInfoService.clearTable();
						boolean result2 = cellInfoService.insert(result);
						if (result2) {
							cellInfoService.restartData();
						}
						modelMap.addObject("success", Constraints.RESULT_SUCCESS+"导入成功！");
						fileLogDto.setResult(1);
					} catch (Exception e) {
						e.printStackTrace();
						fileLogDto.setResult(0);
						modelMap.addObject("success", Constraints.RESULT_FAIL + ":配置文件导入失败！");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					fileLogDto.setResult(0);
					modelMap.addObject("success", Constraints.RESULT_FAIL + ":导入失败或配置文件损坏！");
				}
			
		} else {
			fileLogDto.setResult(0);
		}
		long endtime = System.currentTimeMillis();
		fileLogDto.setEndtime(new Date());
		long alltime = endtime - starttime;
		fileLogDto.setAlltime(alltime);
		List<FileLogDto> fileLogDtos = new ArrayList<>();
		fileLogDtos.add(fileLogDto);
		fileLogService.insert(fileLogDtos);
		return modelMap;
	}
}
