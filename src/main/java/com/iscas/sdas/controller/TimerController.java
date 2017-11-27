package com.iscas.sdas.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tasks.BGTask;
import tasks.cell.degree.HealthDegreeHistoryTask;
import tasks.cell.model.OffLineHealthModelBDTask;
import tasks.cell.model.OffLineHealthModelOfExpertDBTask;
import tasks.cell.ratio.CellHealthPercentageWangGuanAllTask;
import tasks.cell.ratio.CellHealthRatioHistoryTask;
import tasks.sheet.IndexWarningTask;
import tasks.split.FileSpliteTask;

import com.iscas.sdas.util.CommonUntils;
import com.iscas.sdas.util.Constraints;

@Controller
@RequestMapping("/timer")
public class TimerController {
	@RequestMapping("/task")
	public ModelAndView getdispplay(){
		return new ModelAndView("/task/task");
	}
	/**
	 * @author ly
	 *
	 */
	@RequestMapping("/section")
	public ModelMap section(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String  source=request.getParameter("source");
		String  files=request.getParameter("files");
		String  ifdel=request.getParameter("ifdel");
		Boolean ifdeleteOut=false;
		if(!CommonUntils.isempty(ifdel)){
			if(ifdel.equals("1")){
				ifdeleteOut=true;
			}else{
				ifdeleteOut=false;
			}
		}
		if(!CommonUntils.isempty(source)&&!CommonUntils.isempty(files)&&!CommonUntils.isempty(ifdel)){
			FileSpliteTask fileSpliteTask=new FileSpliteTask();
			String[] str=new String[]{source,files,ifdel};
			fileSpliteTask.runTask(str);
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		}else{
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}
	
	/**
	 * 专家模式库
	 * @param request
	 * @return
	 */
	@RequestMapping("/OffLineNormalStateExpert")
	public ModelMap OffLineNormalStateExpert(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String  time=request.getParameter("time");
		if(!CommonUntils.isempty(time)){
			BGTask task=new OffLineHealthModelOfExpertDBTask();// OffLineNormalStateOfExpertDBTask();//XXX 专家模式计算簇心
			task.runTask(new String[]{time});
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		}else{
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}
	@RequestMapping("/OffLineNormalState")
	public ModelMap OffLineNormalState(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String  time=request.getParameter("time");
		if(!CommonUntils.isempty(time)){
			BGTask task=new OffLineHealthModelBDTask();
			task.runTask(new String[]{time});
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		}else{
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}
	@RequestMapping("/HealthDegreeHistory")
	public ModelMap HealthDegreeHistory(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String  time=request.getParameter("time");
		if(!CommonUntils.isempty(time)){
			BGTask task=new HealthDegreeHistoryTask();
			task.runTask(new String[]{time});
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		}else{
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}
	@RequestMapping("/CellHealthPercent")
	public ModelMap CellHealthPercent(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String  time=request.getParameter("time");
		if(!CommonUntils.isempty(time)){
			BGTask task=new CellHealthRatioHistoryTask();
			task.runTask(new String[]{time});
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		}else{
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}
	@RequestMapping("/IndexWarning")
	public ModelMap IndexWarning(HttpServletRequest request){
		ModelMap map = new ModelMap();
		String  time=request.getParameter("time");
		if(!CommonUntils.isempty(time)){
			BGTask task=new IndexWarningTask();
			task.runTask(new String[]{time});
			map.addAttribute(Constraints.RESULT_SUCCESS, true);
		}else{
			map.addAttribute(Constraints.RESULT_SUCCESS, false);
		}
		return map;
	}
}
