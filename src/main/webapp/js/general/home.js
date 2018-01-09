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
					var obj={"cell_code":e.cell_code};
					var html='<li class="list-group-item"><a href="javascript:iframeconvert('+"'"+ctx+"/alarm/todetail"+"','小区信息',"+"[{'key':'cell_code','value':'"+e.cell_code+"'}]"+')">'+e.cell_code+'</a></li>';
					$("#content").append(html);
					var temp = {};
					temp.lng = 113.27 + Math.random() * 0.1;
					temp.lat = 23.14 + Math.random() * 0.1;
					var color="";
					if(e.app_result==0){
						color="red";
					}else if(e.app_result==1){
						color="orange";
					}else if(e.app_result==2){
						color="#25CB73";//"green";
					}else{
                        color="grey";
                    }
					var circle = new BMap.Circle(new BMap.Point(temp.lng, temp.lat),10,{strokeColor:color, strokeWeight:10, strokeOpacity:1}); //创建圆
					map.addOverlay(circle);
					//var marker = new BMap.Marker(new BMap.Point(temp.lng, temp.lat));
					//map.addOverlay(marker);
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
