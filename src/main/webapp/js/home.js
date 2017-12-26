var updatetimeUrl = ctx + "/alarm/updatetime"
var counts = ctx + "/alarm/lastHourClassCount";
var currAlarm = ctx + "/alarm/lastHourAlarm";
$(function(){
	//最新时间
	/*$.ajax({
		url:updatetimeUrl,
		type:"post",
		success:function(data){
			$(".updatetime").html("最新发布时间： "+data);
		}
	});*/
	//监控小区个数
	$.ajax({
		url:counts,
		type:"post",
		success:function(data){
			$("#all").html("共有"+data.rows.all+"个小区被监控：");
			$("#event").html(data.rows.event);
			$("#critical").html(data.rows.critical);
			$("#health").html(data.rows.health);
		}
	});
	switchTab(1);
});
function switchTab(type){
	map.clearOverlays();
	$.ajax({
		url:currAlarm,
		data:{"type":type},
		type:"post",
		success:function(data){
			//data=eval('(' + data + ')');
			if(type==0){
				id="#event_content";
			}else if(type==1){
				id="#critical_content";
			}else{
				id="#health_content";
			}
			$(id).html("");
			if(data.rows.length>0){
				//var id="";
				$.each(data.rows,function(i,e){
					var obj={"cell_code":e.cell_code};
					var html='<li class="list-group-item"><a href="javascript:iframeconvert('+"'"+ctx+"/alarm/todetail"+"','小区信息',"+"[{'key':'cell_code','value':'"+e.cell_code+"'}]"+')">'+e.cell_code+'</a></li>';
					$(id).append(html);
					//
					var temp = {};
					temp.lng = 113.27 + Math.random() * 0.1;
					temp.lat = 23.14 + Math.random() * 0.1;
					var marker = new BMap.Marker(new BMap.Point(temp.lng, temp.lat));
					map.addOverlay(marker);
				});
			}else{
				//$(id).append("<li><a onclick='toDetail()'>暂无数据记录</a></li>");
				$(id).append("&nbsp;&nbsp;&nbsp;暂无数据记录");
			}
		}
			
	});
}
/*function toDetail(code){
	top.$("#iframe_home").attr('src',ctx +"/alarm/todetail?cell_code="+code);
}*/
