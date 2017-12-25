/**
 * 小区健康图表
 * 1、健康诊断结果
 * 2、历史健康度
 */
///********************************************变量部分开始******************************************************\\\\

//小区健康度历史曲线配置吧变量
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
				return res;
			}

		}
	},
	toolbox : {
		show : true,
		top : 20,
		right : 150,
		feature : {
			saveAsImage : {
				show : true,
				name : cellname + "-历史健康度"
			},
			myTool2 : {
				show : true,
				title : '数据导出',
				icon : imgUrl, //图标
				onclick : function(obj) {
					exportExcel_history(obj.option.series[0].name);
				}
			}
		}
	},
	xAxis : {
		type : 'category',
		scale : true,
		boundaryGap : false,
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
		}]
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
	}, {

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
var historyCharts;
///********************************************变量部分结束******************************************************\\\\

///********************************************执行脚本部分开始******************************************************\\\\
(function() {
	historyCharts = echarts.init($("#historyCharts").get(0));
	historyTrendQuery(date_value,"","");
})

