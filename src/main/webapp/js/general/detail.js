var top_split = [];
var bottom_spli = [];
var middle_split = [];
var times=[];
var date_value = "day";// 曲线时间范围
var starttime;
var endtime;
var data=[];//testData
var dataUrl=ctx+"/cell/cellResultHistroy";
var tableUrl=ctx+"/work/allrtworks";
var historyurl = ctx + "/cell/healthtrend";
var historyCharts;
var histroy_trend = {
		tooltip : { // 提示框
			trigger : 'axis', // 触发类型：坐标轴触发
			axisPointer : { // 坐标轴指示器配置项
				type : 'cross' // 指示器类型，十字准星
			},
			formatter : function(params) {
				if (params.length > 3) {
					var res = params[0].seriesName + ': ' + (params[0].value[1])
							+ '<br/>';
					/*res += params[1].seriesName + '数量 : ' + (params[1].value[2])
							+ '<br/>';*/
					return res;
				}

			}
		},
		toolbox: {
	        show : true,
	        feature : {
	            saveAsImage : {show: true}
	        }
	    },
		xAxis : {
			type : 'category',
			scale : true,
			boundaryGap: false,
			data : []
		},
		yAxis : {
			splitLine : {
				show : false
			},
			max : 100
		},
		legend : {
			data : [{
						'name' : "历史健康度"
					}/*, {
						'name' : "投诉"
					},{
	                    'name' : "警戒区"
	                }*/]
		},
		dataZoom : [{
					type : 'slider',
					startValue : 0,
					endValue : 60
				}],
		series : [{
					name : '历史健康度',
					type : 'line',
					data : [],
					label : {
						emphasis : {
							show : true,
							formatter : function(param) {
								return "健康度";
							},
							position : 'top'
						}
					},
					itemStyle : {
						normal : {
							color : 'rgb(46,199,201)',
							lineStyle : {
								color : 'rgb(46,199,201)'
							}
						}
					}
				}, /*{
					name : '投诉',
					data : [],
					type : 'scatter',
					symbolSize : function(data) {
						return data[2] * 12;
					},
					label : {
						emphasis : {
							show : true,
							formatter : function(param) {
								return "投诉";
							},
							position : 'top'
						}
					},
					itemStyle : {
						normal : {
							shadowBlur : 10,
							shadowColor : 'rgba(166,136,224, 0.2)',
							shadowOffsetY : 5,
							color : new echarts.graphic.RadialGradient(0.4, 0.2, 1,
									[{
												offset : 0,
												color : 'rgb(166,136,224)'
											}, {
												offset : 1,
												color : 'rgb(166,136,224)'
											}])
						}
					}
				},{
	                name : '警戒区',
	                data : [],
	                type : 'scatter',
	                symbolSize : function(data) {
	                    return data[3] * 20;
	                },
	                label : {
	                    emphasis : {
	                        show : true,
	                        formatter : function(param) {
	                            return "警戒区";
	                        },
	                        position : 'top'
	                    }
	                },
	                itemStyle : {
	                    normal : {
	                        shadowBlur : 10,
	                        shadowColor : 'rgba(216,122,128, 0.2)',
	                        shadowOffsetY : 5,
	                        color : new echarts.graphic.RadialGradient(0.4, 0.2, 1,
	                                [{
	                                            offset : 0,
	                                            color : 'rgb(216,122,128)'
	                                        }, {
	                                            offset : 1,
	                                            color : 'rgb(216,122,128)'
	                                        }])
	                    }
	                }
	            },*/{

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
				}]
	}
$(function(){
	historyCharts = echarts.init($("#historyCharts").get(0));
	//曲线时间选择
	$(".datePicker").click(function() {
				starttime="";
				endtime="";
					$(this).parent().find(".btn-info").removeClass("btn-info");
					$(this).parent().find(".btn-white").removeClass("btn-white");
					$(this).parent().find("button").addClass("btn-white");
					$(".search").removeClass("btn-white");
					$(".search").addClass("btn-info");
					$(this).parent().children(":last").css("display", "none");
						if ($(this).html() == "日") {
							date_value = "day";
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						} else if ($(this).html() == "周") {
							date_value = "week";
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						} else if ($(this).html() == "月") {
							date_value = "month";
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						}else if ($(this).html() == "按时间选择") {
							date_value==null;
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
							$(this).parent().children(":last").css("display", "block");
							date_value = "";
						}
						if(date_value != ""){
							var title=$("#topTabs").find(".active").find("a").html()
							if (title == "健康诊断结果") {
								getcharts("#rtratio", "健康诊断结果","rgb(46,199,201)",date_value,"","");
								historyTrendQuery(date_value,"","");
							} else {
								getcharts("#ratiotrend", "专家指标集","#1c84c6",date_value,"","");
							}
						}
					});
	
	$.ajax({
		url:tableUrl,
		type:"post",
		data:{"cellname":cellname,"type":date_value},
		success:function(data){
			data=eval('(' + data + ')');
			$('#alarm_table').bootstrapTable({
		        cache : false,
		        striped : true,
		        pagination : true,
		        data:data.rows,
		        toolbar : '#pager_alarm_table',
		        pageSize : 10,
		        pageNumber : 1,
		        pageList : [ 5, 10, 20 ],
		        clickToSelect : true,
		        //sidePagination : 'server',// 设置为服务器端分页
		        columns : [
		            {  
                        title: '序号',align : "center", valign : "middle",
                        formatter: function (value, row, index) {  
                            return index+1;  
                        }  
                    },
		            { field : "occurrence_time", title : "发生时间", align : "center", valign : "middle",
		            	formatter:function(value,row,index){
			                  var jsDate = new Date(value);
			                  var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
			                   return UnixTimeToDate;
			                 }
		            },
		            { field : 'reasons', title : '工单原因', align : 'center', valign : 'middle' },
		            { field : 'monitor_content', title : '监控内容', align : 'center', valign : 'middle' }
		        ],
		       /* onPageChange : function(size, number) {
		        	 	var data = {};
		        	    data.cell_code = cell_code;
		        	    commonRowDatas("alarm_table", data, tableUrl, "commonCallback", true);
		        },*/
		        formatNoMatches : function() {
		            return "没有数据内容";
		        }
		    });
		}
	});
	getcharts("#rtratio", "健康诊断结果","rgb(46,199,201)",date_value);
	///
	historyTrendQuery(date_value,"","");
});
//按日期查询按钮
function query(){
	starttime=$("#starttime").val();
	endtime=$("#endtime").val();
	date_value="";
	var title=$("#topTabs").find(".active").find("a").html()
	if (title == "健康诊断结果") {
		getcharts("#rtratio", "健康诊断结果","rgb(46,199,201)","select",starttime,endtime);
		historyTrendQuery("select",starttime,endtime);
	} else {
		getcharts("#ratiotrend", "专家指标集","#1c84c6","select",starttime,endtime);
	}
}
/*
 * 历史曲线
 */
function historyTrendQuery(type,starttime,endtime) {
	$.ajax({
				url : historyurl,
				data : {
					'cellname' : cellname,
					'type' : type,
					"starttime":starttime,
					"endtime":endtime
				},
				type : "POST",
				async:false,
				success : function(data, status) {
                    var data = eval('(' + data + ')');
					var list = data.rows;
					var axis = [];
					var data2 = [];
					bottom_spli=[];
					middle_split=[];
					top_split=[];
					if(list!=null){
						for (var z = 0; z < list.length; z++) {
							var timer = list[z].time;
							var ratio = list[z].ratio;
							var complaints = list[z].complaints;
	                        var fault = list[z].result_fault;
	                        var warn = list[z].result_warnning;
							var temp = []
							axis.push(timer);
							temp.push(timer);
							temp.push(ratio);
							temp.push(complaints);
	                        temp.push(fault);
	                        temp.push(warn);
							data2.push(temp);
							//
							//
							var b_arr = [];
							b_arr.push(timer);
							b_arr.push(25);
							bottom_spli.push(b_arr);
							var s_arr = [];
							s_arr.push(timer);
							s_arr.push(55);
							middle_split.push(s_arr);
							var t_arr = [];
							t_arr.push(timer);
							t_arr.push(20);
							top_split.push(t_arr);
						}
						histroy_trend.xAxis.data = axis;
						histroy_trend.series[0].data = data2;
						//
						 histroy_trend.series[1].data = bottom_spli;
		                 histroy_trend.series[2].data = middle_split;
		                 histroy_trend.series[3].data = top_split;
						//
						/*histroy_trend.series[1].data = data2;
	                    histroy_trend.series[2].data = data2;
	                    histroy_trend.series[3].data = bottom_spli;
	                    histroy_trend.series[4].data = middle_split;
	                    histroy_trend.series[5].data = top_split;*/
					}else{
						var lasttime=starttime;
						for (var i = 0; i < 24; i++) {
							axis.push(lasttime+" "+i+":00");
							
							var b_arr = [];
							b_arr.push(lasttime);
							b_arr.push(25);
							bottom_spli.push(b_arr);
							var s_arr = [];
							s_arr.push(lasttime);
							s_arr.push(55);
							middle_split.push(s_arr);
							var t_arr = [];
							t_arr.push(lasttime);
							t_arr.push(20);
							top_split.push(t_arr);
							
							data2.push("");
						}
						histroy_trend.xAxis.data = axis;
						histroy_trend.series[0].data = data2;
						//
						 histroy_trend.series[1].data = bottom_spli;
		                 histroy_trend.series[2].data = middle_split;
		                 histroy_trend.series[3].data = top_split;
					}
					historyCharts.setOption(histroy_trend);
                    historyCharts.resize();
					//loading隐藏
                	/*$("#ratiotrend_loadbk").hide();
                	$("#ratiotrend_load").hide();*/
				}
			});
}
function switchTab(id, title,color){
	$(".datePicker").parent().find(".btn-info").removeClass("btn-info");
	$(".datePicker").parent().find(".btn-white").removeClass("btn-white");
	$(".datePicker").parent().find("button").addClass("btn-white");
	$(".datePicker").parent().find("button:first").removeClass("btn-white");
	$(".datePicker").parent().find("button:first").addClass("btn-info");
	getcharts(id, title,color,"day","","");
}
function getcharts(id, title,color,date_value,starttime,endtime){
	//loading显示
	/*$("#ratiotrend_loadbk").show();
	$("#ratiotrend_load").show();*/
	$.ajax({
		url:dataUrl,
		type:"post",
		data:{
			"cellname":cellname,
			"type":date_value,
			"starttime":starttime,
			"endtime":endtime,
		},
		success:function(data){
			data=eval('(' + data + ')');
			
			times=[];
			bottom_spli=[];
			middle_split=[];
			top_split=[];
			dataArr=[];
			if(data.rows.length>0){
				if(data.rows.length>1){
					$.each(data.rows,function(i,e){
						var lasttime=e.yyyymmdd;
						for (var j = 0; j < 24; j++) {
							var arr = [];
							arr.push(lasttime+" "+j+":00");
							arr.push(1);
							bottom_spli.push(arr);
							middle_split.push(arr);
							top_split.push(arr);
							times.push(lasttime+" "+j+":00");
							if(j<10){
								if(e["range_0"+j]==0){
									dataArr.push(0.5);
								}else if(e["range_0"+j]==1){
									dataArr.push(1.5);
								}else if(e["range_0"+j]==2){
									dataArr.push(2.5);
								}
							}else{
								if(e["range_"+j]==0){
									dataArr.push(0.5);
								}else if(e["range_"+j]==1){
									dataArr.push(1.5);
								}else if(e["range_"+j]==2){
									dataArr.push(2.5);
								}
							}
							
						}
					});
				}else{
					var lasttime=data.rows[0].yyyymmdd;
					for (var i = 0; i < 24; i++) {
						var arr = [];
						arr.push(lasttime+" "+i+":00");
						arr.push(1);
						bottom_spli.push(arr);
						middle_split.push(arr);
						top_split.push(arr);
						times.push(lasttime+" "+i+":00");
						if(i<10){
							if(data.rows[0]["range_0"+i]==0){
								dataArr.push(0.5);
							}else if(data.rows[0]["range_0"+i]==1){
								dataArr.push(1.5);
							}else if(data.rows[0]["range_0"+i]==2){
								dataArr.push(2.5);
							}
						}else{
							if(data.rows[0]["range_"+i]==0){
								dataArr.push(0.5);
							}else if(data.rows[0]["range_"+i]==1){
								dataArr.push(1.5);
							}else if(data.rows[0]["range_"+i]==2){
								dataArr.push(2.5);
							}
						}
						
					}
				}
			}else{
				var lasttime=starttime;
				for (var i = 0; i < 24; i++) {
					var arr = [];
					arr.push(lasttime+" "+i+":00");
					arr.push(1);
					bottom_spli.push(arr);
					middle_split.push(arr);
					top_split.push(arr);
					times.push(lasttime+" "+i+":00");
					dataArr.push("");
				}
			}
			
			drawEcharts(id, title, times, dataArr,color);
		}
	});
}
function drawEcharts(id, title, times, data,color) {
	var dataZoom=100;
	if(data.length>100){
		dataZoom=30;
	}
	var mycharts = echarts.init($(id).get(0));
	var option = {
		legend : {
			data : [title]
		},
		tooltip : {
			trigger : 'axis', // 触发类型：坐标轴触发
			position: ['50%', '50%'],
			formatter : function(params) {
				var status="";
				if(params[0].value==0.5){
					status="危险";
				}else if(params[0].value==1.5){
					status="警告";
				}else if(params[0].value==2.5){
					status="正常";
				}else{
					status="无";
				}
					var res = "时间： "+params[0].name+'<br/>'+params[0].seriesName + ': ' + status
							+ '<br/>';
					return res;
				

			}
		},
		toolbox: {
	        show : true,
	        feature : {
	            saveAsImage : {show: true}
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
/*function back(){
	top.$("#iframe_home").attr('src',ctx +"/general/home");
}*/
function toTableData(code){
	top.$("#iframe_detail").attr('src',ctx +"/cell/toTableData?cell_code="+code);
}