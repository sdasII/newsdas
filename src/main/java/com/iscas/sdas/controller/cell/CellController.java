package com.iscas.sdas.controller.cell;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dto.GroupIndexMeatdata;
import com.iscas.sdas.dto.TotalHealthInfoDto;
import com.iscas.sdas.dto.TotalHealthInfoDto2;
import com.iscas.sdas.dto.cell.BaseCellHealth;
import com.iscas.sdas.dto.cell.CellHealthTableDto;
import com.iscas.sdas.dto.cell.CellInfoDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;
import com.iscas.sdas.dto.cell.SignalCellResult;
import com.iscas.sdas.dto.cell.SignalCellResult2;
import com.iscas.sdas.dto.result.TotalResultInfoDto;
import com.iscas.sdas.service.cell.CellService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
import com.iscas.sdas.util.FileExport;
/**
 * 小区有关：全部表、分组、健康度 
 * @author Administrator
 *
 */

@Controller
@RequestMapping("/cell")
public class CellController {

	@Autowired
	CellService cellService;
	/**
	 * 小区列表页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/celltable")
	public ModelAndView celllist(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("group/list");
		return view;
		
	}
	/**
	 * 小区列表（加查询条件）--加分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/getcelllist")
	@ResponseBody
	public ModelMap getlist(@RequestParam(value = "currpage", required = true, defaultValue = "1") String num,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") String size,HttpServletRequest request){
		ModelMap map = new ModelMap();
		CellInfoDto cellDto = new CellInfoDto();
		String name = request.getParameter("name");
		if (!CommonUntils.isempty(name)) {
			cellDto.setCell_code(name);
		}
		int pageNum = Integer.parseInt(num);
		int pageSize = Integer.parseInt(size);
		PageHelper.startPage(pageNum, pageSize);
		List<CellInfoDto> cellDtos = cellService.getCellInfoList(cellDto);
		PageInfo<CellInfoDto> pageInfo = new PageInfo<>(cellDtos);
		List<CellInfoDto> rows = new ArrayList<>();
		for (int i = 0; i < cellDtos.size(); i++) {
			CellInfoDto dto = cellDtos.get(i);
			rows.add(dto);
		}
		PageDto<CellInfoDto> pageDto = new PageDto<>();
		pageDto.setTotal(pageInfo.getTotal());
		pageDto.setRows(rows);
		map.addAttribute(Constraints.RESULT_ROW, pageDto);
	
		return map;
	}
	@RequestMapping("/group")
	@ResponseBody
	public ModelMap getgroup(){
		ModelMap map = new ModelMap();
		List<String> groups = cellService.getCellGroup();
		map.addAttribute(Constraints.RESULT_ROW, groups);
		return map;
	}
	/**
	 * 小组模型
	 * @param grouptype
	 * @return
	 */
	@RequestMapping("/groupindexs")
	@ResponseBody
	public ModelMap getgroupindes(@RequestParam(value="type",defaultValue="I",required = true)String grouptype){
		ModelMap map = new ModelMap();
		List<GroupIndexMeatdata> groups = cellService.getGroupIndexs(grouptype);
		map.addAttribute(Constraints.RESULT_ROW, groups);
		return map;
	}
	/**
	 * 小区健康度历史
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/healthtrend")
	@ResponseBody
	public ModelMap healthtrend(HttpServletRequest request,
			@RequestParam(required=true,defaultValue="day",value="type")String type){
		ModelMap map = new ModelMap();
		String cellname = request.getParameter("cellname");
		String starttime = null,endtime = null;
		if ("select".equals(type)) {
			starttime = request.getParameter("starttime");
			endtime = request.getParameter("endtime");
		}
		List<TotalHealthInfoDto> list = cellService.generateCellHealthTrend(cellname,type,starttime,endtime);
		map.addAttribute(Constraints.RESULT_ROW, list);
		return map;
	}
	/**
	 * 小区健康度历史表格
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/healthtable")
	@ResponseBody
	public ModelMap healthtable(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String cellname = request.getParameter("cellname");
		List<CellHealthTableDto> list = cellService.generateCellHealthTable(cellname);
		map.addAttribute(Constraints.RESULT_ROW, list);
		return map;
	}
	
	/**
	 * 小区属于组别
	 * @param cellname
	 * @return
	 */
	@RequestMapping("/belonggroup")
	@ResponseBody
	public ModelMap getbelonggroup(@RequestParam(value="cellname",defaultValue="I",required = true)String cellname){
		ModelMap map = new ModelMap();
		String group = cellService.getgroup(cellname);
		map.addAttribute("group", group);
		return map;
	}
	/**
	 * 实时健康度
	 * @param request
	 * @return
	 */
	@RequestMapping("/rthealth")
	@ResponseBody
	public ModelMap rthealth(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String cellname = request.getParameter("cellname");
		List<TotalHealthInfoDto> list = cellService.generateCellRTHealth(cellname);
		map.addAttribute(Constraints.RESULT_ROW, list);
		return map;
	}
	/**
	 * 小区首页异常指标预警
	 * @param request
	 * @return
	 */
	@RequestMapping("/alarm_healthtrend")
	@ResponseBody
	public ModelMap alarm_healthtrend(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String cellname = request.getParameter("cellname");	
		List<TotalHealthInfoDto> list = cellService.getalarmhealthtrend(cellname);
		map.addAttribute(Constraints.RESULT_ROW, list);
		return map;
	}
	/**
	 * 当前时刻健康度
	 * @param request
	 * @return
	 */
	@RequestMapping("/rtratio")
	@ResponseBody
	public ModelMap rtratio(HttpServletRequest request){
		ModelMap map = new ModelMap();
		try {
			String cellname = request.getParameter("cellname");
			String count = request.getParameter("count");
			BaseCellHealth baseCellHealth = cellService.newestHealth(cellname);
			if (baseCellHealth!=null) {				
				Method[] methods = baseCellHealth.getClass().getMethods();
				for (int i=0;i<methods.length;i++) {
					if (methods[i].getName().startsWith("getRange")) {
						String rangestr = (String)methods[i].invoke(baseCellHealth, null);						
						int  moment = Integer.parseInt(methods[i].getName().substring(methods[i].getName().lastIndexOf("_")+1));
						if (rangestr!=null) {
							JSONArray array = JSON.parseArray(rangestr);
							if (array.size()<2) {
								String str_hour = (moment-1)>=10?moment+"":"0"+moment;
								String newrange  = (String)baseCellHealth.getClass().getMethod("getRange_"+str_hour, null).invoke(baseCellHealth, null);
								JSONArray newarray = JSON.parseArray(newrange);
								if (newarray!=null) {
									for (int j = 0; j < newarray.size(); j++) {
											JSONObject obj = newarray.getJSONObject(j);
											if ("Ratio".equals(obj.getString("Key"))) {
											    double ratio = Double.parseDouble(obj.get("Value").toString())*100;
											    
											    map.addAttribute("ratio", ratio);
											}
									}
									break;
								}
							}
							if(moment==23){
								String str_hour = (moment-1)>=10?(moment-1)+"":"0"+(moment-1);
								String newrange  = (String)baseCellHealth.getClass().getMethod("getRange_"+str_hour, null).invoke(baseCellHealth, null);
								JSONArray newarray = JSON.parseArray(newrange);
								if (newarray!=null) {
									for (int j = 0; j < newarray.size(); j++) {
											JSONObject obj = newarray.getJSONObject(j);
											if ("Ratio".equals(obj.getString("Key"))) {
											    double ratio = Double.parseDouble(obj.get("Value").toString())*100;
											    DecimalFormat  df = new DecimalFormat("######0.00");
											    ratio = Double.parseDouble(df.format(ratio));
											    map.addAttribute("ratio", ratio);
											}
									}
									break;
								}
							}
						}
					}	
					
				}
				
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}		
		return map;
	}
	/**
	 * 小区健康度判别结果
	 * @param request
	 * @param cellname
	 * @param type
	 * @return
	 */
	@RequestMapping("/cellResultHistroy")
	@ResponseBody
	public ModelMap cellResultHistroy(HttpServletRequest request,
			@RequestParam(value="cellname",required=true)String cellname,
			@RequestParam(value="type",defaultValue="day",required=true)String type){

		String start = request.getParameter("starttime");
		String end = request.getParameter("endtime");
		
		ModelMap map = new ModelMap();
		List<TotalResultInfoDto> list = cellService.generageCellResultHistroy(cellname, type, start, end);
		map.addAttribute(Constraints.RESULT_ROW, list);
		return map;
	}
	
	 /* 分组首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toTableData")
	public ModelAndView toTableData(HttpServletRequest request,HttpServletResponse response){
		String cellname=request.getParameter("cell_code");
		ModelAndView view = new ModelAndView("general/tabledatas");
		view.addObject("cellname", cellname);
		return view;
	 }
	
	
	
	/**
	 * 历史健康度表格导出
	 */
	@RequestMapping("/healthtrend/export")
    public  void healthtrendExport(HttpServletRequest request,
			@RequestParam(required=true,defaultValue="day",value="type")String type,
			HttpServletResponse response){
		String title = null;
        try {
        	String cellname = request.getParameter("cellname");
        	if (CommonUntils.isempty(cellname)) {
				cellname = null;
			}
        	String starttime = null,endtime = null;
    		if ("select".equals(type)) {
    			starttime = request.getParameter("starttime");
    			endtime = request.getParameter("endtime");
    		}
    		Map<String,String> headMap = new LinkedHashMap<>();
    		
    		if (!CommonUntils.isempty(cellname)) {
            	if ("day".equals(type)) {
    				title = cellname +"_最近一天_历史健康度数据";
    			}else if ("week".equals(type)) {
    				title = cellname +"_最近一周_历史健康度数据";
    			}else if ("month".equals(type)) {
    				title = cellname +"_最近一月_历史健康度数据";
    			}else if ("select".equals(type)) {
    				title = cellname +"_历史健康度数据_"+starttime+"_"+endtime;
    			}        		
			}else{
				if ("day".equals(type)) {
    				title =  "历史健康度全部数据_最近一天";
    			}else if ("week".equals(type)) {
    				title =  "历史健康度全部数据_最近一周";
    			}else if ("month".equals(type)) {
    				title = "历史健康度全部数据_最近一月";
    			}else if ("select".equals(type)) {
    				title = "历史健康度全部数据_"+starttime+"_"+endtime;
    			}
				headMap.put("cell_code", "小区名称");
			}	
    		List<TotalHealthInfoDto2> list = cellService.generateCellHealthTrend2(cellname,type,starttime,endtime);
    		headMap.put("date", "日期");
    		for (int i = 0; i < 24; i++) {
				String range = "range_";
				String count = i<10?"0"+i:i+"";
				String head = range + count;
				headMap.put(head, i+":00");
			}
    		JSONArray ja = null;
        	if (list!=null) {
				ja = JSONArray.parseArray(JSON.toJSONString(list));
			}    	
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            FileExport.exportExcelX(title, headMap, ja, null, 0, os);
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
	/**
	 * 判别结果导出
	 * @param request
	 * @param type
	 * @param title
	 * @param response
	 */
	@RequestMapping("/result/export")
    public  void resultExport(HttpServletRequest request,
			@RequestParam(required=true,defaultValue="day",value="type")String type,
			@RequestParam(required=true,defaultValue="hour",value="export_type")String exportType,HttpServletResponse response){
        try {
        	String cellname = request.getParameter("cellname");
        	if (CommonUntils.isempty(cellname)) {
				cellname = null;
			}
        	String title  = request.getParameter("title");
        	String starttime = null,endtime = null;
    		if ("select".equals(type)) {
    			starttime = request.getParameter("starttime");
    			endtime = request.getParameter("endtime");
    		}
       	
        	if (!CommonUntils.isempty(title)) {
        		String titlename = title;
        		title = cellname + "----" + title;
            	if ("day".equals(type)) {
    				title = cellname +"最近一天";
    			}else if ("week".equals(type)) {
    				title = cellname +"最近一周";
    			}else if ("month".equals(type)) {
    				title = cellname +"最近一月";
    			}else if ("select".equals(type)) {
    				title = cellname +"_"+starttime+"_"+endtime;
    			}
        		title += titlename;
			}else {
            	if ("day".equals(type)) {
    				title =  "判别结果_全部数据_最近一天";
    			}else if ("week".equals(type)) {
    				title = "判别结果_全部数据_最近一周";
    			}else if ("month".equals(type)) {
    				title = "判别结果_全部数据_最近一月";
    			}else if ("select".equals(type)) {
    				title = "判别结果_全部数据__"+starttime+"_"+endtime;
    			}

			}
        	
    		List<CellResultHistoryDto> list2 = cellService.cellResultHistroy(cellname, type, starttime, endtime);
    		Map<String,String> headMap = new LinkedHashMap<>();
    		JSONArray sourcesJson = null;
    		if ("hour".equals(exportType)) {
    			headMap.put("cellname", "小区名称");
        		headMap.put("date", "日期");
        		headMap.put("time", "时间");
        		headMap.put("status", "状态");
        		List<SignalCellResult> list = generateSingalValue(list2);	
            	if (list!=null) {
            		sourcesJson = JSONArray.parseArray(JSON.toJSONString(list));
    			} 
			}else if ("days".equals(exportType)) {
				headMap.put("cellname", "小区名称");
	    		headMap.put("date", "日期");
	    		headMap.put("status", "状态");
	    		List<SignalCellResult2> list = generateSingalValue2(list2);	
            	if (list!=null) {
            		sourcesJson = JSONArray.parseArray(JSON.toJSONString(list));
    			}
			}    		    		   		
    		   	
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
	
	private List<SignalCellResult> generateSingalValue(List<CellResultHistoryDto> list) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<SignalCellResult> results = new ArrayList<>();
		if (list!=null) {
			for (CellResultHistoryDto cellResultHistoryDto : list) {
				Method[] methods = cellResultHistoryDto.getClass().getMethods();
				for (Method method : methods) {
					if (method.getName().startsWith("getRange_")) {
						if (cellResultHistoryDto!=null) {
							Integer value =  (Integer)method.invoke(cellResultHistoryDto, null);
							if (value!=null) {
								if (value==0||value==1||value==3) {
									
									if (value==0) {
										SignalCellResult result = new SignalCellResult();
										result.setDate(cellResultHistoryDto.getYyyyMMdd());
										result.setStatus("不健康");
										result.setCellname(cellResultHistoryDto.getCell_code());
										result.setTime(method.getName().substring(9));
										results.add(result);
									}else if (value==1) {
										SignalCellResult result = new SignalCellResult();
										result.setDate(cellResultHistoryDto.getYyyyMMdd());
										result.setStatus("亚健康");
										result.setCellname(cellResultHistoryDto.getCell_code());
										result.setTime(method.getName().substring(9));
										results.add(result);
									}/*else if (value==3) {
										result.setStatus("计算无结果");
									}*/
									
								}
							}			
						}
						
					}
				}
			}
		}
		return results;
	}
	
	private List<SignalCellResult2> generateSingalValue2(List<CellResultHistoryDto> list){
		List<SignalCellResult2> results = new ArrayList<>();
		try {
			if (list!=null) {
				for (CellResultHistoryDto cellResultHistoryDto : list) {
					Method[] methods = cellResultHistoryDto.getClass().getMethods();
					int unhealths=0,lowhealths=0,noresult=0;
					SignalCellResult2 result = new SignalCellResult2();
					result.setDate(cellResultHistoryDto.getYyyyMMdd());
					result.setCellname(cellResultHistoryDto.getCell_code());
					for (Method method : methods) {
						if (method.getName().startsWith("getRange_")) {
							Integer value =  (Integer)method.invoke(cellResultHistoryDto, null);
							if (value!=null) {
								if (value==0||value==1||value==3) {
									if (value==0) {
										unhealths++;
									}
									if (value==1) {
										lowhealths++;
									}
									if (value==3) {
										noresult++;
									}																					
								}
							}						
						}
					}
					if (unhealths>0) {
						result.setStatus("不健康");
						results.add(result);
					}else if (lowhealths>0) {
						results.add(result);
						result.setStatus("亚健康");
					}/*else if (noresult>0) {
						result.setStatus("计算无结果");
						results.add(result);
					}*/
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
}
