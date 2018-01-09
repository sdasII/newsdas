package com.iscas.sdas.controller.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iscas.sdas.service.model.IndexModelService;
import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;
/**
 * 小区健康模型
 * @author dongqun
 * 2017年10月12日上午9:43:23
 */

@Controller
@RequestMapping("/model")
public class CellIndexController {
	@Autowired
	IndexModelService cellIndexService;
	/**
	 * 单个指标数据-K线图
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	@ResponseBody
	public ModelMap cellindex(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String indexid = request.getParameter("index");
		String cellname = request.getParameter("cellname");
		String month = request.getParameter("month");
		if (CommonUntils.isempty(month)) {
			month=null;
		}
		if (!CommonUntils.isempty(indexid)&&!CommonUntils.isempty(cellname)) {
			try {
				List<List<Double[]>> hosdata = cellIndexService.generateIndexData(cellname, indexid, month);
				if (hosdata!=null) {
					map.addAttribute(Constraints.RESULT_ROW,hosdata);	
					map.addAttribute(Constraints.RESULT_SUCCESS, true);	
				}else {
					map.addAttribute(Constraints.RESULT_SUCCESS, false);	
				}		
				return map;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.addAttribute(Constraints.RESULT_SUCCESS, false);
		return map;
	}
	/**
	 * 模型的时间列表
	 * @author dongqun
	 * 2018年1月9日上午10:09:13
	 * @param request
	 * @return
	 */
	@RequestMapping("/index/times")
	@ResponseBody
	public ModelMap indextimelist(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String indexid = request.getParameter("index");
		String cellname = request.getParameter("cellname");
		if (!CommonUntils.isempty(indexid) && !CommonUntils.isempty(cellname)) {
			List<String> times = cellIndexService.getIndexTimes(cellname, indexid);	
			if (times.size()>0) {
				map.addAttribute(Constraints.RESULT_ROW, times);
				map.addAttribute(Constraints.RESULT_SUCCESS, true);
				return map;
			}
		}
		map.addAttribute(Constraints.RESULT_SUCCESS, false);
		return map;
	}
	
}
