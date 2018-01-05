package com.iscas.sdas.controller.work;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iscas.sdas.common.PageDto;
import com.iscas.sdas.dto.work.CapacityWorkDto;
import com.iscas.sdas.service.log.FileLogService;
import com.iscas.sdas.service.work.CapacityWorkService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
import com.iscas.sdas.util.FileExport;
/**
 * 验证工单表
 * @author dongqun
 * 2017年12月27日下午1:42:59
 */
@Controller
@RequestMapping("/capacitywork")
public class CapacityWorkController {

	@Autowired
	CapacityWorkService capacityWorkService;
	@Autowired
	FileLogService fileLogService;
	/**
	 * 获取验证工单
	 * @author dongqun
	 * 2017年12月27日下午1:54:52
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/rt/list")
	@ResponseBody
	public ModelMap validatedOrders(@RequestParam(value = "currpage", required = true, defaultValue = "1") String num,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") String size,HttpServletRequest request){
		ModelMap map = new ModelMap();
		String type = request.getParameter("type");
		String starttime = null,endtime = null;
		if (Constraints.SELECT.equals(type)) {
			starttime = request.getParameter("starttime");
			endtime = request.getParameter("endtime");
		}
		String cellname = request.getParameter("cellname");		
		String area = request.getParameter("area");
		String content = request.getParameter("content");
		String result = request.getParameter("result");
		CapacityWorkDto capacityWorkDto = new CapacityWorkDto();
		if (!CommonUntils.isempty(cellname)) {
			capacityWorkDto.setCellid(cellname);
		}
		if (!CommonUntils.isempty(area) && !"全部".equals(area)) {
			capacityWorkDto.setBelong_area(area);
		}
		if (!CommonUntils.isempty(content)&&!"全部".equals(content)) {
			capacityWorkDto.setMonitor_content(content);
		}
		if (!"全部工单".equals(result)) {
			if (!CommonUntils.isempty(result)) {
				capacityWorkDto.setQuestionflag(Integer.valueOf(result));
			}
		}
		PageDto<CapacityWorkDto> works = capacityWorkService.getPageList(capacityWorkDto, type, starttime, endtime,num,size);
		map.addAttribute(Constraints.RESULT_ROW, works);
		return map;
	}
	/**
	 * 性能工单小区的所属区域
	 * @author dongqun
	 * 2017年12月27日下午1:37:26
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/belongare")
	@ResponseBody
	public ModelMap getbelongare(HttpServletRequest request,HttpServletResponse response){
		ModelMap map = new ModelMap();
		List<String> areas = capacityWorkService.getbelongaera();
		map.addAttribute(Constraints.RESULT_ROW, areas);
		return map;
	}
	/**
	 * 性能工单导出
	 * @author dongqun
	 * 2017年12月27日上午11:04:00
	 * @param request
	 * @param type
	 * @param exportType
	 * @param response
	 */
	@RequestMapping("/export")
    public  void export(HttpServletRequest request,
			@RequestParam(required=true,defaultValue="day",value="type")String type,HttpServletResponse response){
        try {
    		String starttime = null,endtime = null;
    		if (Constraints.SELECT.equals(type)) {
    			starttime = request.getParameter("starttime");
    			endtime = request.getParameter("endtime");
    		}
    		String cellname = request.getParameter("cellname");		
    		String area = request.getParameter("area");
    		String monitor = request.getParameter("content");
    		String result = request.getParameter("result");
    		CapacityWorkDto capacityWorkDto = new CapacityWorkDto();
    		if (!CommonUntils.isempty(cellname)) {
    			capacityWorkDto.setCellid(cellname);
    		}
    		if (!CommonUntils.isempty(area)&&!"全部".equals(area)) {
    			capacityWorkDto.setBelong_area(area);
    		}
    		if (!CommonUntils.isempty(monitor)&&!"全部".equals(monitor)) {
    			capacityWorkDto.setMonitor_content(monitor);
    		}
    		if (!"全部工单".equals(result)) {
    			if (!CommonUntils.isempty(result)) {
        			capacityWorkDto.setQuestionflag(Integer.valueOf(result));
        		}
			}
			String title = null;
			if ("day".equals(type)) {
				title = "工单验证_最近一天数据";
			} else if ("week".equals(type)) {
				title = "工单验证_最近一周数据";
			} else if ("month".equals(type)) {
				title = "工单验证_最近一月数据";
			} else if ("select".equals(type)) {
				title = "判别结果导出_全部数据__" + starttime + "_" + endtime;
			}
			List<CapacityWorkDto> list = capacityWorkService.getAllList(capacityWorkDto, type, starttime,endtime);		
			Map<String, String> headMap = new LinkedHashMap<>();
			headMap.put("occurrence_time", "发生时间");
			headMap.put("cellid", "小区名称");
			headMap.put("belong_area", "所属区域");
			headMap.put("monitor_content", "监控内荣");
			headMap.put("monitor_value", "监控时值");
			headMap.put("alerm_level", "告警级别");
			headMap.put("reasons", "原因");
			headMap.put("boutique_level", "精品级别");
			headMap.put("limit_times", "越限次数");
			headMap.put("complete_time", "完成时间");
			headMap.put("questionflag", "工单验证状态");
			JSONArray sourcesJson = null;
			if (list != null) {

				sourcesJson = JSONArray.parseArray(JSON.toJSONString(list));

				for(int z=0;z<sourcesJson.size();z++){
					JSONObject obj = sourcesJson.getJSONObject(z);
					Timestamp startstamp = obj.getTimestamp("occurrence_time");
					Date startdate = new Date();
					startdate = startstamp;
					obj.put("occurrence_time", startdate);
					Timestamp endstamp = obj.getTimestamp("complete_time");
					Date enddate = new Date();
					enddate = endstamp;
					obj.put("complete_time", enddate);
					Integer questionflag = obj.getInteger("questionflag");
					if (questionflag==0) {
						obj.put("questionflag", "高度可疑");
					}else if (questionflag==1) {
						obj.put("questionflag", "可疑工单");
					}else if (questionflag==2) {
						obj.put("questionflag", "正常工单");
					}else {
						obj.put("questionflag", "未知或错误");
					}
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
	/**
	 * 工单验证最新发布时间
	 * @author dongqun
	 * 2018年1月2日下午4:58:56
	 * @return
	 */
	@RequestMapping("updatetime")
	@ResponseBody
	public ModelMap updatetime(){
		ModelMap map = new ModelMap();
		map.addAttribute("updatetime", fileLogService.lastUpdateTime("性能工单验证"));
		return map;
	}
	
	
	
}
