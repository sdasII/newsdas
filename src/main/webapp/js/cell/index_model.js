/**
 * 指标模型
 * 表现为k线图
 */
///********************************************变量部分开始******************************************************\\\\
var name1 = '历史分析';
var name2 = '实时数据';
var belongGroupUrl = ctx + "/cell/belonggroup";
var groupIndexUrl = ctx + "/cell/groupindexs";
var indexUrl = ctx + "/cell/index";
var global_month;
var nullchart = [];
for (var i = 0; i < 24; i++) {
    nullchart.push(i);
}

//指标K线图变量
var echart_option = {
	tooltip : {
		trigger : 'axis',
		axisPointer : {
			type : 'cross'
		},
		formatter : function(params) {
			var res="";
			if(params[0].value!=undefined){
				res = params[0].seriesName + ' ' + params[0].name + ': '
				+ params[0].value;
			}
			for (var i = 1; i < params.length; i++) {
				if(params[i].value[2]!=undefined){
					res += '<br/>' + params[i].seriesName + '  前值 : '
					+ params[i].value[1] + '<br/>' + params[i].seriesName
					+ '  后值 : ' + params[i].value[2];
				}
				if(params[i].value[3]!=undefined){
					res += '<br/>' + params[i].seriesName + '  前值 : '
					+ params[i].value[1] + '<br/>' + params[i].seriesName
					+ '  后值 : ' + params[i].value[3];
				}
				if(params[i].value[4]!=undefined){
					res += '<br/>' + params[i].seriesName;
					+ '  最大 : ' + params[i].value[4];
				}
			}
			return res;
		}
	},
	legend : {
		data : [name1, name2]
	},
	grid : {
		left : '10%',
		right : '10%',
		bottom : '15%'
	},
	xAxis : {
		type : 'category',
		scale : true,
		boundaryGap : false,
		axisLine : {
			onZero : false
		},
		splitLine : {
			show : false
		},
		data : nullchart,
		axisLabel : {
			formatter : function(value) {
				return value;
			}
		}
	},
	yAxis : {
		scale : true,
		splitArea : {
			show : true
		}
	},
	dataZoom : [{
		type : 'inside',
		start : 0,
		end : 100
	}, {
		show : true,
		type : 'slider',
		y : '90%',
		start : 50,
		end : 100
	}],
	series : []
};
//k线图-上下边界颜色
var upColor = '#68C5CC';
var upBorderColor = '#19B7CF';
var downColor = '#68C5CC';
var downBorderColor = '#19B7CF';

//没有数据时k线图默认
var serie = {
    name : name1,
    type : 'candlestick',
    data : {},
    itemStyle : {
        normal : {
            color : upColor,
            color0 : downColor,
            borderColor : upBorderColor,
            borderColor0 : downBorderColor
        }
    },
    markLine : {
        symbol : ['none', 'none'],
        data : [[{
            name : 'from lowest to highest',
            type : 'min',
            valueDim : 'lowest',
            symbol : 'circle',
            symbolSize : 10,
            label : {
                normal : {
                    show : false
                },
                emphasis : {
                    show : false
                }
            }
        }, {
            type : 'max',
            valueDim : 'highest',
            symbol : 'circle',
            symbolSize : 10,
            label : {
                normal : {
                    show : false
                },
                emphasis : {
                    show : false
                }
            }
        }]]
    }
}
var line = {
    name : name2,
    type : 'line',
    data : {},
    smooth : true,
    lineStyle : {
        normal : {
            opacity : 0.2
        }
    }
}
//多簇心的颜色
var color = ['rgba(104, 197, 204, 0.73)', 'rgba(51,51,204, 0.23)','rgba(171, 226, 98, 0.62)'];
var borderColor = ['rgb(25, 183, 207)', 'rgb(51,51,204,)', 'rgb(163, 222, 84)'];

var global_cellname;
var global_indexid;

///********************************************变量部分结束******************************************************\\\\

///********************************************执行脚本部分开始******************************************************\\\\
var chart_mb = echarts.init($("#mb").get(0));
/*
 * 获取当前小区的指标列表
 */
$.ajax({
	url : belongGroupUrl,
	data : {
		'cellname' : cell_code
	},
	type : "POST",
	success : function(data, status) {
		var group = data.group;
		$.ajax({
			url : groupIndexUrl,
			type : "post",
			data : {
				'type' : group
			},
			success : function(data, status) {
                var list = data.rows;
				$("#group_index").children().remove();
				for (var i = 0; i < list.length; i++) {
					var item = list[i];
					var type = item.cell_code;
					var index = item.indeicator_code;
					var option;
                    var name = cellname;
					if (i == 0) {
						option = $('<li onclick=cellindex("'
								+ name
								+ '","'
								+ index
								+ '") class="active"><a data-toggle="tab" aria-expanded="true">'
								+ item.indeicator_name + '</a></li>');
					} else {
						option = $('<li onclick=cellindex("'
								+ name
								+ '","'
								+ index
								+ '") class=""><a data-toggle="tab" aria-expanded="false">'
								+ item.indeicator_name + '</a></li>');
					}

					$("#group_index").append(option);
				}
				$("li.active").trigger("click");
			}
		});
	}
});
/**
 * 生成某个指标的k线图
 * @param cellcode
 * @param indexcode
 * @returns
 */
function cellindex(cellcode, indexcode) {
	$("#tab2_loadbk").show();
	$("#tab2_load").show();
    global_cellname = cellcode;
    global_indexid = indexcode;
    $.ajax({
        url : indexUrl,
        type : "post",
        data : {
            'cellname' : cellcode,
            'index' : indexcode,
            'month':global_month
        },
        success : function(data, status) {
            updateEchart(data);
        }
    });
}
/**
 * 重新组合后台返回数据-使之适应k线图显示格式
 * @param {} rawData
 * @return {}
 */
function splitData(rawData) {
    var categoryData = [];
    var values = []
    for (var i = 0; i < rawData.length; i++) {
        categoryData.push(rawData[i].splice(0, 1)[0]);//获取一个数据--健康度
        values.push(rawData[i])//获取另外四个值
    }
    return {
        categoryData : categoryData,
        values : values
    };
}
/**
 * 解析数据-获取小区时刻健康度
 * @param {} data
 * @return {}
 */
function calculateRT(data) {
    var result = [];
    for (var i = 0, len = data.length; i < len; i++) {
        result.push(data[i][0]);
    }
    return result;
}
function updateEchart(data){
    if (data.success) {
                var scatter = data.rows;
                var temp0 = splitData(scatter[0]);
                var rtdata = calculateRT(temp0.values);
                line.data = rtdata;
                echart_option.series[0] = line;
                var k = scatter.length;
                for (var i = 0; i < k; i++) {
                    var serie1 = {
                        name : name1,
                        type : 'candlestick',
                        data : [],
                        itemStyle : {
                            normal : {
                                color : upColor,
                                color0 : downColor,
                                borderColor : upBorderColor,
                                borderColor0 : downBorderColor
                            }
                        },
                        markLine : {
                            symbol : ['none', 'none'],
                            data : [{
                                        name : 'from lowest to highest',
                                        type : 'min',
                                        valueDim : 'lowest',
                                        symbol : 'circle',
                                        symbolSize : 10,
                                        label : {
                                            normal : {
                                                show : false
                                            },
                                            emphasis : {
                                                show : false
                                            }
                                        }
                                    }, {
                                        type : 'max',
                                        valueDim : 'highest',
                                        symbol : 'circle',
                                        symbolSize : 10,
                                        label : {
                                            normal : {
                                                show : false
                                            },
                                            emphasis : {
                                                show : false
                                            }
                                        }
                                    }]
                        }
                    }
                    if (i == 0) {
                        var name = "簇心" + i;
                        serie1.name = name;
                        serie1.data = temp0.values;
                        serie1.itemStyle.normal.color = color[i];
                        serie1.itemStyle.normal.color0 = color[i];
                        serie1.itemStyle.normal.borderColor = borderColor[i];
                        serie1.itemStyle.normal.borderColor0 = borderColor[i];
                    } else {
                        var temp = splitData(scatter[i]);
                        var name = "簇心" + i;
                        serie1.name = name;
                        serie1.data = temp.values;
                        if (i < 3) {
                            serie1.itemStyle.normal.color = color[i];
                            serie1.itemStyle.normal.color0 = color[i];
                            serie1.itemStyle.normal.borderColor = borderColor[i];
                            serie1.itemStyle.normal.borderColor0 = borderColor[i];
                        }
                    }

                    echart_option.series[i + 1] = serie1;
                }
                echart_option.xAxis.data=nullchart;
                chart_mb.setOption(echart_option);
            } else {
            	echart_option.series=[];
            	echart_option.xAxis.data=[];
                echart_option.series.push(line);
                echart_option.series.push(serie);
                chart_mb.setOption(echart_option);
            }
    $("#tab2_loadbk").hide();
    $("#tab2_load").hide();
}
/**
 * 按月份查询指标
 */
function cellindex_search(){
	$("#tab2_loadbk").show();
	 $("#tab2_load").show();
	global_month = $("#time").val();
    var data = {};
    data.month = global_month;
    data.index = global_indexid;
    data.cellname = global_cellname;
    $.ajax({
        type:"post",
        url: indexUrl,
        data: data,
        success:function(data){
            updateEchart(data);
        }
    });
}
///********************************************执行脚本部分结束******************************************************\\\\