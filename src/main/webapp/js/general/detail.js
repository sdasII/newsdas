var top_split = [];
var bottom_spli = [];
var middle_split = [];
var times=[];
var date_value = 0;// 曲线时间范围
var data=[];//testData
$(function(){
	//曲线时间选择
	$(".datePicker").click(function() {
					$(this).parent().find(".btn-info").removeClass("btn-info");
					$(this).parent().find(".btn-white").removeClass("btn-white");
					$(this).parent().find("button").addClass("btn-white");
						if ($(this).html() == "日") {
							date_value = 0;
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						} else if ($(this).html() == "周") {
							date_value = 7;
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						} else if ($(this).html() == "月") {
							date_value = 30;
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						}
						var title=$("#topTabs").find(".active").find("a").html()
								if (title == "健康指标集") {
									drawEcharts("#rtratio", "健康指标集",times, data,"#4cb117",date_value);
								} else {
									switchTab('#ratiotrend','专家指标集',times, data,'#1c84c6',date_value);
								}
							
					});
	
	for (var i = 0; i < 20; i++) {
		var arr = [];
		arr.push(i);
		arr.push(1);
		bottom_spli.push(arr);
		middle_split.push(arr);
		top_split.push(arr);
		//testData
		times.push(i);
		data.push(i%2);
	}
	drawEcharts("#rtratio", "健康指标集", times, data,"#4cb117",0);
	//
	$('#alarm_table').bootstrapTable({
        cache : false,
        striped : true,
        pagination : true,
        toolbar : '#pager_alarm_table',
        pageSize : 10,
        pageNumber : 1,
        pageList : [ 5, 10, 20 ],
        clickToSelect : true,
        sidePagination : 'server',// 设置为服务器端分页
        columns : [
            { field : "id", title : "序号", align : "center", valign : "middle"},
            { field : "yyyyMMdd", title : "发生时间", align : "center", valign : "middle"},
            { field : 'message', title : '风险提示', align : 'center', valign : 'middle' }
        ],
        onPageChange : function(size, number) {
        	 	var data = {};
        	    data.cell_code = cell_code;
        	    commonRowDatas("alarm_table", data, alarmurl, "commonCallback", true);
        },
        formatNoMatches : function() {
            return "没有数据内容";
        }
    });
});
function switchTab(id,title,color,date_value){
	drawEcharts(id, title, times, data,color,date_value);
}
function drawEcharts(id, title, times, data,color,date_value) {
	var dataZoom=100;
	if(data.length>200){
		dataZoom=30;
	}
	var mycharts = echarts.init($(id).get(0));
	var option = {
		legend : {
			left : 'top',
			data : [title]
		},
		tooltip : {
			trigger : 'axis', // 触发类型：坐标轴触发
			position: ['50%', '50%'],
			formatter : function(params) {
				
					var res = params[0].seriesName + ': ' + (params[0].value)
							+ '<br/>';
					return res;
				

			}
		},
		dataZoom : {
			start : 0,
			end : dataZoom
		},
		xAxis : {
			type : 'category',
			scale : true,
			boundaryGap: false,
			data : times
		},
		yAxis : {
			splitLine : {
				show : false
			},
			max : 3
		},
		series : [ {
			name : title,
			type : 'line',
			smooth : true,
			itemStyle : {
				normal : {
					color : color
				}
			},
			data : data
		},{

			name : '',
			type : 'line',
			smooth : true,
			symbol : "none",
			stack : true,
			itemStyle : {
				normal : {
					opacity : 0.2,
					color : 'rgba(231,133,131,0.4)',
					lineStyle : {
						opacity : 0.2,
						color : 'rgba(231,133,131,0.4)'
					},
					areaStyle : {
						type : 'default'
					}
				}
			},
			data : bottom_spli
		}, {
			name : '',
			type : 'line',
			smooth : true,
			symbol : "none",
			stack : true,
			itemStyle : {
				normal : {
					opacity : 0.2,
					color : 'rgba(231,233,131,0.4)',
					lineStyle : {
						opacity : 0.2,
						color : 'rgba(231,233,131,0.4)'
					},
					areaStyle : {
						type : 'default'
					}
				}
			},
			data : middle_split
		}, {
			name : '',
			type : 'line',
			smooth : true,
			symbol : "none",
			stack : true,
			itemStyle : {
				normal : {
					opacity : 0.2,
					color : 'rgba(172,231,131,0.4)',
					lineStyle : {
						opacity : 0.2,
						color : 'rgba(172,231,131,0.4)'
					},
					areaStyle : {
						type : 'default'
					}
				}
			},
			data : top_split
		} ]
	};
	mycharts.setOption(option);
	mycharts.resize();
}
function back(){
	top.$("#iframe_home").attr('src',ctx +"/general/home");
}
function toTableData(code){
	top.$("#iframe_home").attr('src',ctx +"/cell/toTableData?cell_code="+code);
}