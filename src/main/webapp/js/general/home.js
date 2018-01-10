var updatetimeUrl = ctx + "/alarm/updatetime"
var counts = ctx + "/alarm/lastHourClassCount";
var currAlarm = ctx + "/alarm/lastHourAlarm";
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
				$.each(data.rows,function(i,e){
					var icons_url = "../lib/map/images/point-collection/"; //这个是你要显示坐标的图片的相对路径
					
					var obj={"cell_code":e.cell_code};
					var html='<li class="list-group-item"><a href="javascript:iframeconvert('+"'"+ctx+"/alarm/todetail"+"','小区信息',"+"[{'key':'cell_code','value':'"+e.cell_code+"'}]"+')">'+e.cell_code+'</a></li>';
					$("#content").append(html);
					var temp = {};
					temp.lng = 113.27 + Math.random() * 0.1;
					temp.lat = 23.14 + Math.random() * 0.1;
					var color="";
					if(e.app_result==0){
						icons_url=icons_url+"red.png";
						//color="red";
					}else if(e.app_result==1){
						icons_url=icons_url+"yellow.png";
						//color="orange";
					}else if(e.app_result==2){
						icons_url=icons_url+"green.png";
						//color="#25CB73";//"green";
					}else{
						icons_url=icons_url+"gray.png";
                        //color="grey";
                    }
					//var circle = new BMap.Circle(new BMap.Point(temp.lng, temp.lat),10,{strokeColor:color, strokeWeight:10, strokeOpacity:1}); //创建圆
					//map.addOverlay(circle);
					var markers = new BMap.Marker(new BMap.Point(temp.lng, temp.lat)); //lng为经度,lat为纬度
					var icon = new BMap.Icon(icons_url, new BMap.Size(10, 10)); //显示图标大小
					markers.setIcon(icon);//设置标签的图标为自定义图标
			        map.addOverlay(marker);              // 将标注添加到地图中 
					var opts = {  
					        width : 200,     // 信息窗口宽度  
					        height: 30,     // 信息窗口高度  
					        title : "小区信息" , // 信息窗口标题  
					        }  
					var infoWindow = new BMap.InfoWindow("小区名称："+e.cell_code, opts);  // 创建信息窗口对象   
					marker.addEventListener("mouseover", function(){            
					    map.openInfoWindow(infoWindow,point); //开启信息窗口  
					});
					marker.addEventListener("mouseout", function(){            
					    map.closeInfoWindow(infoWindow,point); //关闭信息窗口  
					});
				});
			}else{
				//$(id).append("<li><a onclick='toDetail()'>暂无数据记录</a></li>");
				$("#content").append("&nbsp;&nbsp;&nbsp;暂无数据记录");
			}
		}
			
	});
}
/*function toDetail(code){
	top.$("#iframe_home").attr('src',ctx +"/alarm/todetail?cell_code="+code);
}*/
