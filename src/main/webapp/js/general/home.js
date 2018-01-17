var updatetimeUrl = ctx + "/alarm/updatetime"
var counts = ctx + "/alarm/lastHourClassCount";
var currAlarm = ctx + "/alarm/lastHourAlarm";
var heatUrl=ctx + "/general/healthgroup";
var heatPoints=[];
var markArr=[];
var heatmapOverlay;
$(function(){
	//最新时间
	$.ajax({
		url:updatetimeUrl,
		type:"post",
		success:function(data){
			$(".updatetime").html("最新发布时间： "+data);
		}
	});
	//监控小区个数
	$.ajax({
		url:counts,
		type:"post",
		success:function(data){
			$("#all").html("共有"+data.rows.all+"个小区被监控：");
			$("#event").html(data.rows.event);
			$("#critical").html(data.rows.critical);
			$("#health").html(data.rows.health);
            $("#other").html(data.rows.others);
		}
	});
	search();
	$('#status').change(function(){ 
		search();
	});
});
function search(type){
	type=$("#status").val();
	map.clearOverlays();
	$.ajax({
		url:currAlarm,
		data:{"type":type},
		type:"post",
		success:function(data){
			$("#content").html("");
			if(data.rows.length>0){
				markArr=[];
				$.each(data.rows,function(i,e){
					var icons_url = ctx+"/lib/map/images/point-collection/"; //这个是你要显示坐标的图片的相对路径
					var obj={"cell_code":e.cell_code};
					var html='<li class="list-group-item"><a href="javascript:iframeconvert('+"'"+ctx+"/alarm/todetail"+"','小区信息',"+"[{'key':'cell_code','value':'"+e.cell_code+"'}]"+')">'+e.cell_code+'</a></li>';
					$("#content").append(html);
					var temp = {};
					//temp.lng = 113.27 + Math.random() * 0.1;
					//temp.lat = 23.14 + Math.random() * 0.1;
                    if(e.station_longitude!=null){
                        temp.lng = parseFloat(e.station_longitude);
                    }else{
                        temp.lng = 113.27 + Math.random() * 0.1;
                    }
                    if(e.station_latitude!=null){
                        temp.lat = parseFloat(e.station_latitude);
                    }else{
                        temp.lat = 23.14 + Math.random() * 0.1;
                    }  
                    //temp.count=0;
                    
					var color="";
					var status="";
					if(e.app_result==0){
						icons_url=icons_url+"red.png";
						status="事件";
						//color="red";
					}else if(e.app_result==1){
						icons_url=icons_url+"yellow.png";
						status="亚健康";
						//color="orange";
					}else if(e.app_result==2){
						icons_url=icons_url+"green.png";
						status="健康";
						//color="#25CB73";//"green";
					}else{
						icons_url=icons_url+"gray.png";
						status="计算无结果";
                        //color="grey";
                    }
					//var circle = new BMap.Circle(new BMap.Point(temp.lng, temp.lat),10,{strokeColor:color, strokeWeight:10, strokeOpacity:1}); //创建圆
					//map.addOverlay(circle);
					var cell_point=new BMap.Point(temp.lng, temp.lat);
					var marker = new BMap.Marker(cell_point); //lng为经度,lat为纬度
					var icon = new BMap.Icon(icons_url, new BMap.Size(20, 25)); //显示图标大小
					marker.setIcon(icon);//设置标签的图标为自定义图标
					marker.setShadow("");
			        map.addOverlay(marker);              // 将标注添加到地图中 
					var opts = {  
					        width : 200,     // 信息窗口宽度  
					        height: 25,     // 信息窗口高度  
					        //title : "小区信息" , // 信息窗口标题  
					        }  
					var infoWindow = new BMap.InfoWindow("小区名称："+e.cell_code+"<br>状态："+status, opts);  // 创建信息窗口对象   
					marker.addEventListener("mouseover", function(){            
					    map.openInfoWindow(infoWindow,cell_point); //开启信息窗口  
					});
					markArr.push(marker);
					/*marker.addEventListener("mouseout", function(){            
					    map.closeInfoWindow(infoWindow,cell_point); //关闭信息窗口  
					});*/
				});
			}else{
				//$(id).append("<li><a onclick='toDetail()'>暂无数据记录</a></li>");
				$("#content").append("&nbsp;&nbsp;&nbsp;暂无数据记录");
			}
		}
			
	});
	//热力图
	$.ajax({
		url:heatUrl,
		type:"post",
		success:function(data){
			if(data.rows.length>0){
				heatPoints=[];
				$.each(data.rows,function(i,e){
					var temp = {};
                    if(e.logitude!=0&&e.latitude!=0){
                        temp.lng = parseFloat(e.logitude);
                        temp.lat = parseFloat(e.latitude);
                        if(e.ratio>0&&e.ratio<=0.25){
    						temp.count=10;
    						heatPoints.push(temp);
    					}else if(e.ratio>0.25&&e.ratio<=0.8){
    						temp.count=5;
    						heatPoints.push(temp);
    					}else if(e.ratio>0.8){
    						temp.count=0;
    						heatPoints.push(temp);
    					}
                    }  
				});
					//热力图
					heatmapOverlay = new BMapLib.HeatmapOverlay({"opacity":0.6,"radius":15});
					map.addOverlay(heatmapOverlay);
					heatmapOverlay.setDataSet({data:heatPoints,max:10});
					setGradient();
			        heatmapOverlay.hide();
			}
		}
	});
}
function setGradient(){
 	//格式如下所示:
 	var gradient = {
      	0:'green',
 	 	0.5:'yellow',
	  	1:'red'
    };
    heatmapOverlay.setOptions({"gradient":gradient});
}
function changeHeatView(obj){
	if(heatmapOverlay==undefined){
		showOnlyMessage(ERROR, "数据加载中，请稍候再试");
	}else{
		$.each(markArr,function(i,e){
			map.removeOverlay(e);
		});
		 heatmapOverlay.show();
		 $(obj).attr("onclick","changeMarkerView(this)");
		 var html="<span class='glyphicon glyphicon-search' aria-hidden='true'></span>查看标注图";
		 $(obj).html(html);
	}
	
}
function changeMarkerView(obj){
	heatmapOverlay.hide();
	$.each(markArr,function(i,e){
		map.addOverlay(e);
	});
	 $(obj).attr("onclick","changeHeatView(this)");
	 var html="<span class='glyphicon glyphicon-search' aria-hidden='true'></span>查看热力图";
	 $(obj).html(html);
}
/*function toDetail(code){
	top.$("#iframe_home").attr('src',ctx +"/alarm/todetail?cell_code="+code);
}*/
