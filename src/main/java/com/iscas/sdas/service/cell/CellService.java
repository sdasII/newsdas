package com.iscas.sdas.service.cell;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iscas.sdas.dao.StationInfoDtoMapper;
import com.iscas.sdas.dao.alarm.AlarmDao;
import com.iscas.sdas.dao.cell.CellDao;
import com.iscas.sdas.dao.cell.CellResultHistoryDao;
import com.iscas.sdas.dao.complain.ComplainDao;
import com.iscas.sdas.dto.BaseStationHealthRatio;
import com.iscas.sdas.dto.CellComplainDto;
import com.iscas.sdas.dto.GroupIndexMeatdata;
import com.iscas.sdas.dto.StationInfoDto;
import com.iscas.sdas.dto.TotalHealthInfoDto;
import com.iscas.sdas.dto.TotalHealthInfoDto2;
import com.iscas.sdas.dto.cell.BaseCellHealth;
import com.iscas.sdas.dto.cell.CellDto;
import com.iscas.sdas.dto.cell.CellHealthTableDto;
import com.iscas.sdas.dto.cell.CellInfoDto;
import com.iscas.sdas.dto.cell.CellResultHistoryDto;
import com.iscas.sdas.dto.cell.MomentDto;
import com.iscas.sdas.dto.result.TotalResultInfoDto;
import com.iscas.sdas.util.CommonUntils;

@Service
public class CellService{

	Logger logger = Logger.getLogger(CellService.class);
	@Autowired
	CellDao cellDao;
	@Autowired
	StationInfoDtoMapper stationDto;
	@Autowired
	ComplainDao complainDao;
	@Autowired
	CellResultHistoryDao cellResultHistoryDao;
	@Autowired
	AlarmDao alarmDao;
	
	public List<CellInfoDto> getCellInfoList(CellInfoDto dto){
		return cellDao.select(dto);
	}
	
	public List<CellDto> getCellList(CellDto cellDto){
		return cellDao.getcells(cellDto);
	}
	
	public List<String> getCellGroup(){
		return cellDao.getgroups();
	}
	
	public List<GroupIndexMeatdata> getGroupIndexs(String grouptype){
		return cellDao.getgroupindexs(grouptype);
	}
	
	public String getgroup(String cellname){
		return cellDao.getgroup(cellname);
	}
	/**
	 * 历史曲线(全部信息)
	 * @param cellname
	 * @return
	 */
	public List<TotalHealthInfoDto> generateCellHealthTrend(String cellname,String type,String start,String end){
		List<TotalHealthInfoDto> result = null;
		try {
			List<BaseCellHealth> cellHealths;
			if ("day".equals(type)) {
				cellHealths = cellDao.cellhealthtrendDay(cellname);
			}else if ("week".equals(type)) {
				cellHealths = cellDao.cellhealthtrendWeek(cellname);
			}else if ("month".equals(type)) {
				cellHealths = cellDao.cellhealthtrendWithinOneMonth(cellname);
			}else {
				cellHealths = cellDao.cellhealthtrendWithinSelect(cellname, start, end);
			}
			if (cellHealths!=null && cellHealths.size()>0) {
				//List<String> complaints = complaintsWithinCurrenttime(cellname,type,start,end);//投诉工单
				String begintime = cellHealths.get(0).getYyyyMMdd();
				if ("day".equals(type)) {
					result = originData(1, begintime);
				}else if ("week".equals(type)) {
					result = originData(7, begintime);
				}else if ("month".equals(type)) {
					result = originData(30, begintime);
				}else {
					result = originDataSelect(start, end);
				}
				if (result!=null) {
					for (int i = 0; i < result.size(); i++) {
						for (int j=0;j<cellHealths.size(); j++) {
							BaseCellHealth cellHealth = cellHealths.get(j);
							Method[] methods = cellHealth.getClass().getMethods();
							for (Method method : methods) {
								if (method.getName().startsWith("getRange")) {	
									TotalHealthInfoDto infoDto  = result.get(i);
									String range = (String)method.invoke(cellHealth, null);						
									int  moment = Integer.parseInt(method.getName().substring(method.getName().lastIndexOf("_")+1));
									Double ratio = parseRatio(range);
									String time = formattime(cellHealth.getYyyyMMdd(), moment);
									if (result.get(i).getTime().equals(time)) {
										infoDto.setRatio(ratio);
										logger.info("-----------------坐标轴："+result.get(i).getTime()+"ratio："+time);
										String hour = moment>=10?moment+"":"0"+moment;
										Integer app_result = cellDao.getHealthRatio(cellname, cellHealth.getYyyyMMdd(), hour);
										if (app_result!=null) {
											if (app_result==0) {
												infoDto.setResult_fault(1);
											}else if (app_result==2) {
												infoDto.setResult_warnning(1);
											}
										}	
									}
								}		
							}
							
						}
						/*for (int z = 0; z < complaints.size(); z++) {
							if (complaints.get(z).equals(result.get(i).getTime())) {							
								int complaint = result.get(i).getComplaints()+1;
								result.get(i).setComplaints(complaint);
								logger.info("--投诉---"+complaints.get(z)+"--------"+result.get(i).getTime()+"总共"+complaint+"个");
							}
						}*/
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 历史曲线(只有历史健康度信息)
	 * @param cellname
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TotalHealthInfoDto2> generateCellHealthTrend2(String cellname,String type,String start,String end){
		List<TotalHealthInfoDto2> result = new ArrayList<>();
		try {
			List<BaseCellHealth> cellHealths;
			if ("day".equals(type)) {
				cellHealths = cellDao.cellhealthtrendDay(cellname);
			}else if ("week".equals(type)) {
				cellHealths = cellDao.cellhealthtrendWeek(cellname);
			}else if ("month".equals(type)) {
				cellHealths = cellDao.cellhealthtrendWithinOneMonth(cellname);
			}else {
				cellHealths = cellDao.cellhealthtrendWithinSelect(cellname, start, end);
			}
			if (cellHealths != null && cellHealths.size() > 0) {
				
				for (int j = 0; j < cellHealths.size(); j++) {
					
					BaseCellHealth cellHealth = cellHealths.get(j);
					String date = cellHealth.getYyyyMMdd();
					TotalHealthInfoDto2 infoDto = new TotalHealthInfoDto2();
					infoDto.setDate(date);
					infoDto.setCell_code(cellHealth.getCell_code());
					Method[] methods = cellHealth.getClass().getMethods();
					for (Method method : methods) {
						String methodName = method.getName();
						if (methodName.startsWith("getRange")) {

							String range = (String) method.invoke(cellHealth, null);
							Double ratio = parseRatio(range);
							Method setMethod =  infoDto.getClass().getMethod(methodName.replaceFirst("g", "s"), String.class);
							setMethod.invoke(infoDto, String.valueOf(ratio));
							}
						}
					result.add(infoDto);
					}

				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 历史表格
	 * @param cellname
	 * @return List<CellHealthTableDto>
	 */
	public List<CellHealthTableDto> generateCellHealthTable(String cellname) {
		
		List<CellHealthTableDto> list = new ArrayList<>();
		
		List<BaseCellHealth> cellHealths = cellDao.cellhealthtrendWithinOneMonth(cellname);
		if (cellHealths != null) {
			try {
				for (int j = 0; j < cellHealths.size(); j++) {
					BaseCellHealth cellHealth = cellHealths.get(j);
					CellHealthTableDto dto = new CellHealthTableDto();
					List<MomentDto> moments = new ArrayList<>();
					for (int i = 0; i < 24; i++) {
						MomentDto momentDto = new MomentDto();
						momentDto.setRatio(praseMoment(i, cellHealth));
						String hour = i>=10?i+"":"0"+i;
						Integer result = cellDao.getHealthRatio(cellname, cellHealth.getYyyyMMdd(), hour);
						if (result!=null) {
							momentDto.setResult(result);
						}else {
							momentDto.setResult(-1);
						}						
						moments.add(momentDto);
					}
					dto.setYyyyMMdd(cellHealth.getYyyyMMdd());
					dto.setMoments(moments);
					list.add(dto);
				}
				return list;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}
	
	
	private double praseMoment(int i,BaseCellHealth baseCellHealth){
		switch (i) {
		case 0:
			return parseRatio(baseCellHealth.getRange_00());
		case 1:
			return parseRatio(baseCellHealth.getRange_01());
		case 2:
			return parseRatio(baseCellHealth.getRange_02());
		case 3:
			return parseRatio(baseCellHealth.getRange_03());
		case 4:
			return parseRatio(baseCellHealth.getRange_04());
		case 5:
			return parseRatio(baseCellHealth.getRange_05());
		case 6:
			return parseRatio(baseCellHealth.getRange_06());
		case 7:
			return parseRatio(baseCellHealth.getRange_07());
		case 8:
			return parseRatio(baseCellHealth.getRange_08());
		case 9:
			return parseRatio(baseCellHealth.getRange_09());
		case 10:
			return parseRatio(baseCellHealth.getRange_10());
		case 11:
			return parseRatio(baseCellHealth.getRange_11());
		case 12:
			return parseRatio(baseCellHealth.getRange_12());
		case 13:
			return parseRatio(baseCellHealth.getRange_13());
		case 14:
			return parseRatio(baseCellHealth.getRange_14());
		case 15:
			return parseRatio(baseCellHealth.getRange_15());
		case 16:
			return parseRatio(baseCellHealth.getRange_16());
		case 17:
			return parseRatio(baseCellHealth.getRange_17());
		case 18:
			return parseRatio(baseCellHealth.getRange_18());
		case 19:
			return parseRatio(baseCellHealth.getRange_19());
		case 20:
			return parseRatio(baseCellHealth.getRange_20());
		case 21:
			return parseRatio(baseCellHealth.getRange_21());
		case 22:
			return parseRatio(baseCellHealth.getRange_22());
		case 23:
			return parseRatio(baseCellHealth.getRange_23());
		default:
			return 0;
		}
	}
	
	
	private String formattime(String time,int moment){
		String year  = time.substring(0, 4);
		String month  = time.substring(4, 6);
		String day  = time.substring(6);
		String result = year+month+day+" "+moment+":00";
		return result;
	}
	/**
	 * 初始化List<TotalHealthInfoDto> --一周或一月
	 * @param day
	 * @param begintime
	 * @return
	 */
	private List<TotalHealthInfoDto> originData(int day,String begintime){
		List<TotalHealthInfoDto> list = new ArrayList<>();
		for (int i = 0; i < day; i++) {
			int tempyear  = Integer.parseInt(begintime.substring(0, 4));
			int tempmonth  = Integer.parseInt(begintime.substring(4, 6));
			int tempday;
			int days = CommonUntils.daysInMonth(tempyear, tempmonth);//一月多少天
			tempday  =  Integer.parseInt(begintime.substring(6))+i<=days?Integer.parseInt(begintime.substring(6))+i:Integer.parseInt(begintime.substring(6))+i-days;

			if (Integer.parseInt(begintime.substring(6))+i>days) {
				if (tempmonth<12) {
					tempmonth = tempmonth + 1;
				}else {
					tempmonth = 1;
					tempyear += 1;
				}
				
			}
			for (int j = 0; j < 24; j++) {
				TotalHealthInfoDto infoDto  = new TotalHealthInfoDto();
				String str_tempmonth  = tempmonth >=10?tempmonth+"":"0"+tempmonth;
				String str_tempday = tempday >=10?tempday+"":"0"+tempday;
				String time = tempyear+str_tempmonth+str_tempday+" "+j+":00";
				infoDto.setTime(time);
				list.add(infoDto);
			}
		}
		return list;
	}
	/**
	 * 初始化List<TotalHealthInfoDto> 
	 * @param day
	 * @param begintime
	 * @return
	 */
	private List<TotalHealthInfoDto> originDataSelect(String starttime,String endtime){
		List<TotalHealthInfoDto> list = new ArrayList<>();
		int startmonth = Integer.parseInt(starttime.substring(4, 6))-1;
		int endmonth = Integer.parseInt(endtime.substring(4, 6))-1;
		Date start = new Date(Integer.parseInt(starttime.substring(0, 4)), startmonth, Integer.parseInt(starttime.substring(6)));
		Date end = new Date(Integer.parseInt(endtime.substring(0, 4)), endmonth, Integer.parseInt(endtime.substring(6)));
		int day = CommonUntils.differentDaysByMillisecond(start, end);
		for (int i = 0; i < day+1; i++) {
			int tempyear  = Integer.parseInt(starttime.substring(0, 4));
			int tempmonth  = Integer.parseInt(starttime.substring(4, 6));
			int tempday;
			int days = CommonUntils.daysInMonth(tempyear, tempmonth);//一月多少天
			tempday  =  Integer.parseInt(starttime.substring(6))+i<=days?Integer.parseInt(starttime.substring(6))+i:Integer.parseInt(starttime.substring(6))+i-days;

			if (Integer.parseInt(starttime.substring(6))+i>days) {
				if (tempmonth<12) {
					tempmonth = tempmonth + 1;
				}else {
					tempmonth = 1;
					tempyear += 1;
				}
				
			}
			for (int j = 0; j < 24; j++) {
				TotalHealthInfoDto infoDto  = new TotalHealthInfoDto();
				String str_tempmonth  = tempmonth >=10?tempmonth+"":"0"+tempmonth;
				String str_tempday = tempday >=10?tempday+"":"0"+tempday;
				String time = tempyear+str_tempmonth+str_tempday+" "+j+":00";
				infoDto.setTime(time);
				list.add(infoDto);
			}
		}
		return list;
	}
	
	/**
	 * 返回最近一天的小区健康度
	 * @param cellname
	 * @return
	 */
	public BaseCellHealth newestHealth(String cellname){
		return cellDao.cellcurrenthealth(cellname);
	}
	/**
	 * 小区实时健康度
	 * @param cellname
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TotalHealthInfoDto> generateCellRTHealth(String cellname){
		List<TotalHealthInfoDto> list = new ArrayList<>();
		try {
			BaseCellHealth cellHealth = cellDao.cellcurrenthealth(cellname);

			if (cellHealth!=null) {
				
				//List<String> perWorkCount = permanceWorkWithinCurrenttime(cellname); 
				//List<String> deviceWorkCount = deviceWorkWithinCurrenttime(cellname);
				//List<String> osWorkCount = osWorkWithinCurrenttime(cellname);
				//List<String> complaints = complaintsWithinCurrenttime(cellname,type,start,end);
			
					Method[] methods = cellHealth.getClass().getMethods();
					for (Method method : methods) {
						if (method.getName().startsWith("getRange")) {	
							TotalHealthInfoDto infoDto  = new TotalHealthInfoDto();
							String range = (String)method.invoke(cellHealth, null);						
							int  moment = Integer.parseInt(method.getName().substring(method.getName().lastIndexOf("_")+1));
							Double ratio = parseRatio(range);
							String year  = cellHealth.getYyyyMMdd().substring(0, 4);
							String month  = cellHealth.getYyyyMMdd().substring(4, 6);
							String day  = cellHealth.getYyyyMMdd().substring(6);
							String time = year+"-"+month+"-"+day+" "+moment+":00";
							infoDto.setTime(time);
							infoDto.setRatio(ratio);
							infoDto.setDeviceworks(0);
							infoDto.setOsworks(0);
							infoDto.setPerworks(0);
							infoDto.setComplaints(0);
							/*if (perWorkCount!=null) {
								for (int j = 0; j < perWorkCount.size(); j++) {
									
									if (perWorkCount.get(j).equals(infoDto.getTime())) {
										System.out.println("---性能--"+perWorkCount.get(j)+"--------"+infoDto.getTime());
										int perworks = infoDto.getPerworks()+1;
										infoDto.setPerworks(perworks);
									}
								}
							}
							if (deviceWorkCount!=null) {
								for (int j = 0; j < deviceWorkCount.size(); j++) {
									
									if (deviceWorkCount.get(j).equals(infoDto.getTime())) {
										System.out.println("--设备单---"+deviceWorkCount.get(j)+"--------"+infoDto.getTime());
										int devworks = infoDto.getPerworks()+1;
										infoDto.setPerworks(devworks);
									}
								}
							}
							if (osWorkCount!=null) {
								for (int j = 0; j < osWorkCount.size(); j++) {
									
									if (osWorkCount.get(j).equals(infoDto.getTime())) {
										System.out.println("--退服务---"+osWorkCount.get(j)+"--------"+infoDto.getTime());
										int osworks = infoDto.getPerworks()+1;
										infoDto.setPerworks(osworks);
									}
								}
							}*/
							/*if (complaints!=null) {
								for (int j = 0; j < complaints.size(); j++) {
									
									if (complaints.get(j).equals(infoDto.getTime())) {
										
										int complaint = infoDto.getComplaints()+1;
										infoDto.setComplaints(complaint);
										//System.out.println("--投诉---"+complaints.get(j)+"--------"+infoDto.getTime()+"总共"+complaint+"个");
									}
								}
							}*/
							list.add(infoDto);
						}		
					}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 投诉工单数量
	 * @param cellname
	 * @return
	 */
	private List<String> complaintsWithinCurrenttime(String cellname,String type,String starttime,String endtime){
		List<String> list = new ArrayList<>();
		List<CellComplainDto> works;
		if ("week".equals(type)) {
			works =  complainDao.complaintWithinOneWeek(cellname);
		}else if ("month".equals(type)) {
			works =  complainDao.complaintWithinOneMonth(cellname);
		}else {
			works =  complainDao.complaintWithinSelect(cellname, starttime, endtime);
		}
		if (works!=null) {
			
			for (CellComplainDto work : works) {
				if (work.getServicerequesttype()!=null) {
					if (work.getServicerequesttype().contains("4G")) {
						int year = work.getRecordtime().getYear()+1900;
						int month = work.getRecordtime().getMonth()+1;
						String monthstr = month>=10?""+month:"0"+month;
						int day = work.getRecordtime().getDate();
						String daystr = day>=10?""+day:"0"+day;
						int hour = work.getRecordtime().getHours();
						String occurtime = year +"-"+ monthstr+"-"+daystr+" "+hour+":00";
						list.add(occurtime);
					}
				}							
			}
		}
		return list;
	}	
	
	
	private double parseRatio(String range){
		double ratio = 0;
		if (range!=null) {
			int count = 1;
			JSONArray array = JSON.parseArray(range);
			if (array!=null) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					if ("Ratio".equals(obj.getString("Key"))) {
					    ratio = Double.parseDouble(obj.get("Value").toString())*100;
					    DecimalFormat  df = new DecimalFormat("######0.00");
					    ratio = Double.parseDouble(df.format(ratio));
						return ratio;
					}
					count++;
				}
				if (count==array.size()) {
					ratio = 0;
					return ratio;
				}
			}
		}
		return ratio;
	}
	
	
	/**
	 * 所有小区的最近健康度
	 * @return
	 */
	private List<BaseCellHealth> allcellhealth(){
		List<BaseCellHealth> list = new ArrayList<>();
		List<String> cells = cellDao.allcells();
		for (String cellname : cells) {
			BaseCellHealth cellHealth = cellDao.currenthealthratio(cellname);
			if (cellHealth!=null) {
				list.add(cellHealth);
			}
		}
		return list;
	}
	/**
	 * 当前所有小区基站的最小健康度集合
	 * @return
	 * @throws Exception
	 */
	public List<BaseStationHealthRatio> currentHealthGroup() throws Exception{
		int hour = Calendar.HOUR_OF_DAY;
		String str_hour = hour>10?hour+"":"0"+hour;		
		List<BaseStationHealthRatio> result = new ArrayList<>();
		List<String> stations = cellDao.allstations();//所有基站
		List<BaseCellHealth> baseCellHealths = allcellhealth();//所有小区的健康度	
		for (String station : stations) {
			double max=0;
			List<String> cells = cellDao.allcellsinstation(station);//属于这个基站的所有小区
			StationInfoDto stationInfoDto = stationDto.selectByStationName(station);
			for (int i = 0; i < cells.size(); i++) {
				for (int j = 0; j < baseCellHealths.size(); j++) {
					if (cells.get(i).equals(baseCellHealths.get(j).getCell_code())) {
						String range = (String)baseCellHealths.get(j).getClass().getMethod("getRange_"+str_hour, null).invoke(baseCellHealths.get(j), null);
						//String range = baseCellHealths.get(j).getRange_00();
						JSONArray array = JSON.parseArray(range);
						for (int k = 0; k < array.size(); k++) {
							if ("Ratio".equals(array.getJSONObject(k).getString("Key"))) {
								if (max<array.getJSONObject(k).getDoubleValue("Value")) {
									max = array.getJSONObject(k).getDoubleValue("Value");
									
								}
								break;
							}
						}
						break;
					}
				}
			}
			BaseStationHealthRatio baseStationHealthRatio = new BaseStationHealthRatio();
			/*if (max==0) {
				max = 100;
			}else {
				max = 1/max;
			}	*/	
			baseStationHealthRatio.setRatio(max);
			if (stationInfoDto!=null) {
				String longitude = stationInfoDto.getStationLongitude().trim();
				String latitude = stationInfoDto.getStationLatitude().trim();
				try {
					if (!CommonUntils.isempty(latitude)) {
						if (!"".equals(latitude)) {
							baseStationHealthRatio.setLatitude(Double.parseDouble(latitude));
						}						
					}
					if (!CommonUntils.isempty(longitude)) {
						if (!"".equals(longitude)) {
							baseStationHealthRatio.setLogitude(Double.parseDouble(longitude));
						}						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			result.add(baseStationHealthRatio);
		}
		logger.info("-------计算结束---------");
		return result;
	}
	/**
	 * 异常预警
	 * @param cellname
	 * @return
	 */
	public List<TotalHealthInfoDto> getalarmhealthtrend(String cellname){
		List<TotalHealthInfoDto> list = new ArrayList<>();
		try {
			List<BaseCellHealth> cellHealths = cellDao.alarmhealthtrend(cellname);
			if (cellHealths!=null && cellHealths.size()>0) {
					BaseCellHealth cellHealth = cellHealths.get(0);
					Method[] methods = cellHealth.getClass().getMethods();
					List<TotalHealthInfoDto> keyArr=new ArrayList<>();
					Boolean ifPushKey=true;
					for (int i=0;i<methods.length;i++) {
						if (methods[i].getName().startsWith("getRange")) {
							String range = (String)methods[i].invoke(cellHealth, null);						
							int  moment = Integer.parseInt(methods[i].getName().substring(methods[i].getName().lastIndexOf("_")+1));
							//获取异常预警的指标名称和数量 
							if(ifPushKey){
								JSONArray array = JSON.parseArray(range);
								if (array!=null&&array.size()>1) {
								for (int j = 0; j < array.size(); j++) {
									TotalHealthInfoDto info=new TotalHealthInfoDto();
									JSONObject obj = array.getJSONObject(j);
									String key=obj.getString("Key");
									if(!key.equals("Ratio")){
										int count=Integer.parseInt(obj.getString("Value"));
										String year  = cellHealth.getYyyyMMdd().substring(0, 4);
										String month  = cellHealth.getYyyyMMdd().substring(4, 6);
										String day  = cellHealth.getYyyyMMdd().substring(6);
										String time = year+"-"+month+"-"+day+" "+moment+":00:00";
										info.setTime(time);
										info.setAlarm_name(key);
										info.setAlarm_counts(0);
										keyArr.add(info);
									}
								}
								ifPushKey=false;
								}
							}
							keyArr=getAlarm(keyArr,range);
						}		
					}
					
					if(keyArr!=null&&keyArr.size()>1){
						for(int j=0;j<keyArr.size();j++){
							TotalHealthInfoDto info=new TotalHealthInfoDto();
							if(keyArr.get(j).getAlarm_counts()>1){
								info.setTime(keyArr.get(j).getTime());
								String key=keyArr.get(j).getAlarm_name().split("_")[1];
								info.setAlarm_name(cellDao.getalarmname(key));
								info.setAlarm_counts(keyArr.get(j).getAlarm_counts());
								list.add(info);
							}
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 获取异常预警
	 * @param 
	 * @return
	 */
	private List<TotalHealthInfoDto> getAlarm(List<TotalHealthInfoDto> keyArr,String range){
		if (keyArr!=null&&keyArr.size()>1) {
			JSONArray array = JSON.parseArray(range);
			if(array!=null&&array.size()>1){
				for(int i=0;i<keyArr.size();i++){
					JSONObject obj = array.getJSONObject(i);
					int count=Integer.parseInt(obj.getString("Value"));
					if(count==0){
						keyArr.get(i).setAlarm_counts(keyArr.get(i).getAlarm_counts()+1);
					}
				}
			}
		}
		return keyArr;
	}
	
	/**
	 * 小区健康度判别结果(原接口，不要删除！)
	 * @param cellname
	 * @param type
	 * @return
	 */
	public List<CellResultHistoryDto> cellResultHistroy(String cellname,String type,String start,String end){
		if ("day".equals(type)) {
			return alarmDao.cellListLastDay(cellname);
		}else if ("month".equals(type)) {
			return alarmDao.cellListLastMonth(cellname);
		}else if ("week".equals(type)) {
			return alarmDao.cellListLastWeek(cellname);
		}else if ("select".equals(type)) {
			return alarmDao.cellListBySelect(cellname, start, end);
		}else {
			return null;
		}
	}
	/**
	 * 小区健康度判别结果
	 * @author dongqun
	 * 2018年1月19日上午9:59:47
	 * @param cellname
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TotalResultInfoDto> generageCellResultHistroy(String cellname,String type,String start,String end){
		List<TotalResultInfoDto> result = null;
		try {
			List<CellResultHistoryDto> cellResults = null;
			if ("day".equals(type)) {
				cellResults =  alarmDao.cellListLastDay(cellname);
			}else if ("month".equals(type)) {
				cellResults =  alarmDao.cellListLastMonth(cellname);
			}else if ("week".equals(type)) {
				cellResults =  alarmDao.cellListLastWeek(cellname);
			}else if ("select".equals(type)) {
				cellResults =  alarmDao.cellListBySelect(cellname, start, end);
			}
			if (cellResults!=null && cellResults.size()>0) {
				String begintime = cellResults.get(0).getYyyyMMdd();
				if ("day".equals(type)) {
					result = originResultData(1, begintime);
				}else if ("week".equals(type)) {
					result = originResultData(7, begintime);
				}else if ("month".equals(type)) {
					result = originResultData(30, begintime);
				}else {
					result = originResultDataSelect(start, end);
				}
				if (result!=null) {
					for (int i = 0; i < result.size(); i++) {
						for (int j=0;j<cellResults.size(); j++) {
							CellResultHistoryDto cellResult = cellResults.get(j);
							Method[] methods = cellResult.getClass().getMethods();
							for (Method method : methods) {
								if (method.getName().startsWith("getRange")) {	
									TotalResultInfoDto infoDto  = result.get(i);
									Integer range = (Integer)method.invoke(cellResult, null);						
									int  moment = Integer.parseInt(method.getName().substring(method.getName().lastIndexOf("_")+1));
									String time = formattime(cellResult.getYyyyMMdd(), moment);
									if (result.get(i).getTime().equals(time)) {
										infoDto.setResult(range);
										logger.info("-----------------坐标轴："+result.get(i).getTime()+"ratio："+time);
											
									}
								}		
							}
							
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * 初始化List<TotalHealthInfoDto> --一周或一月
	 * @param day
	 * @param begintime
	 * @return
	 */
	private List<TotalResultInfoDto> originResultData(int day,String begintime){
		List<TotalResultInfoDto> list = new ArrayList<>();
		for (int i = 0; i < day; i++) {
			int tempyear  = Integer.parseInt(begintime.substring(0, 4));
			int tempmonth  = Integer.parseInt(begintime.substring(4, 6));
			int tempday;
			int days = CommonUntils.daysInMonth(tempyear, tempmonth);//一月多少天
			tempday  =  Integer.parseInt(begintime.substring(6))+i<=days?Integer.parseInt(begintime.substring(6))+i:Integer.parseInt(begintime.substring(6))+i-days;

			if (Integer.parseInt(begintime.substring(6))+i>days) {
				if (tempmonth<12) {
					tempmonth = tempmonth + 1;
				}else {
					tempmonth = 1;
					tempyear += 1;
				}
				
			}
			for (int j = 0; j < 24; j++) {
				TotalResultInfoDto infoDto  = new TotalResultInfoDto();
				String str_tempmonth  = tempmonth >=10?tempmonth+"":"0"+tempmonth;
				String str_tempday = tempday >=10?tempday+"":"0"+tempday;
				String time = tempyear+str_tempmonth+str_tempday+" "+j+":00";
				infoDto.setTime(time);
				list.add(infoDto);
			}
		}
		return list;
	}
	/**
	 * 初始化List<TotalHealthInfoDto> 
	 * @param day
	 * @param begintime
	 * @return
	 */
	private List<TotalResultInfoDto> originResultDataSelect(String starttime,String endtime){
		List<TotalResultInfoDto> list = new ArrayList<>();
		int startmonth = Integer.parseInt(starttime.substring(4, 6))-1;
		int endmonth = Integer.parseInt(endtime.substring(4, 6))-1;
		Date start = new Date(Integer.parseInt(starttime.substring(0, 4)), startmonth, Integer.parseInt(starttime.substring(6)));
		Date end = new Date(Integer.parseInt(endtime.substring(0, 4)), endmonth, Integer.parseInt(endtime.substring(6)));
		int day = CommonUntils.differentDaysByMillisecond(start, end);
		for (int i = 0; i < day+1; i++) {
			int tempyear  = Integer.parseInt(starttime.substring(0, 4));
			int tempmonth  = Integer.parseInt(starttime.substring(4, 6));
			int tempday;
			int days = CommonUntils.daysInMonth(tempyear, tempmonth);//一月多少天
			tempday  =  Integer.parseInt(starttime.substring(6))+i<=days?Integer.parseInt(starttime.substring(6))+i:Integer.parseInt(starttime.substring(6))+i-days;

			if (Integer.parseInt(starttime.substring(6))+i>days) {
				if (tempmonth<12) {
					tempmonth = tempmonth + 1;
				}else {
					tempmonth = 1;
					tempyear += 1;
				}
				
			}
			for (int j = 0; j < 24; j++) {
				TotalResultInfoDto infoDto  = new TotalResultInfoDto();
				String str_tempmonth  = tempmonth >=10?tempmonth+"":"0"+tempmonth;
				String str_tempday = tempday >=10?tempday+"":"0"+tempday;
				String time = tempyear+str_tempmonth+str_tempday+" "+j+":00";
				infoDto.setTime(time);
				list.add(infoDto);
			}
		}
		return list;
	}
}
