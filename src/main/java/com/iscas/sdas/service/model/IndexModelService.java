package com.iscas.sdas.service.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iscas.sdas.dao.model.IndexModelDao;
import com.iscas.sdas.dto.common.Base;
import com.iscas.sdas.dto.model.IndexModel;
/**
 *小区的指标和权重；分组的指标和权重
 * @author Administrator
 *
 */
@Service
public class IndexModelService {
	@Autowired
	IndexModelDao cellIndexDao;
	
	public static String GROUP = "group";
	public static String CELL = "cell";
	
	
	/**
	 * 小组和小区的健康模型
	 * @param cellname  小区na
	 * @param indexcode 指标id
	 * @param type group或cell或月份
	 * @return
	 */
	public List<List<Double[]>> generateIndexData(String cellname,String indexcode,String month){
		List<List<Double[]>> map = new ArrayList<>();
		try {
			Base baseindex = null;
			baseindex = cellIndexDao.getCellIndexContentByMonth(cellname, indexcode, month);

			if (baseindex!=null) {
				Method[] methods = baseindex.getClass().getMethods();
				for (Method method : methods) {
					if (method.getName().contains("getRange")) {	
						String range = (String)method.invoke(baseindex, null);						
						String key = method.getName().substring(method.getName().lastIndexOf("_")+1);
						List<Double[]> value = getconv(key,range);
						if (value!=null) {
							map.add(value);						
						}
					}				
				}
				int max=0;
				List<List<Double[]>> result = new ArrayList<>();
				for (int i = 0; i < map.size(); i++) {
					List<Double[]> ratios = map.get(i);
					if (max<ratios.size()) {
						max=ratios.size();
					}
				}
				for (int i = 0; i < max; i++) {
					List<Double[]> ratiolist = new ArrayList<>();
					result.add(ratiolist);
				}
				for (int i = 0; i < map.size(); i++) {
					List<Double[]> ratios = map.get(i);
					for(int j=0; j<max; j++){
						if (j<ratios.size()) {
							result.get(j).add(ratios.get(j));
						}else {
							Double[] point = new Double[5];
							point[0] = ratios.get(0)[0];
							point[1] = 0.0;
							point[2] = 0.0;
							point[3] = 0.0;
							point[4] = 0.0;
							result.get(j).add(point);
						}
						
					}
				}
				return result;
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 生成多个簇心
	 * @param key
	 * @param content
	 * @return
	 */
	private List<Double[]> getconv(String key,String content) {
		List<Double[]> result = new ArrayList<>();
		
		double temp = Double.valueOf(key);
		double max,min,pre,last;
		try {
			JSONArray array = JSON.parseArray(content);
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);					
					if (obj.containsKey("is_valid")) {
						boolean is_valid = obj.getBoolean("is_valid");//判断是否为有效簇心
						if (is_valid) {
							Double[] point = new Double[5];
							point[0] =  temp;
							if (obj.containsKey("max")) {
								max = obj.getDouble("max");
								point[4] = max;
							}else {
								point[4] = 0.0;
							}
							if (obj.containsKey("min")) {
								min = obj.getDouble("min");
								point[3] = min;
							}else {
								point[3] = 0.0;
							}
							if (obj.containsKey("pre")) {
								pre = obj.getDouble("pre");
								point[1] = pre;
							}else{
								point[1] = 0.0;
							}
							if (obj.containsKey("last")) {
								last = obj.getDouble("last");
								point[2] = last;
							}else {
								point[2] = 0.0;
							}
							result.add(point);
						}
					}				
				}
				if (result.size()==0) {
					Double[] point = new Double[5];
					point[0] = temp;
					point[1] = 0.0;
					point[2] = 0.0;
					point[3] = 0.0;
					point[4] = 0.0;
					result.add(point);
				}		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	/**
	 * 模型的时间列表
	 * @author dongqun
	 * 2018年1月9日上午10:08:40
	 * @param cellname
	 * @param indexcode
	 * @return
	 */
	public List<String> getIndexTimes(String cellname,String indexcode){
		List<String> times = new ArrayList<>();
		List<IndexModel> indexs = cellIndexDao.getCellIndexContents(cellname, indexcode);
		if (indexs!=null) {
			for (IndexModel indexModel : indexs) {
				times.add(indexModel.getYyyyMM());
			}
		}
		return times;
	}

}
