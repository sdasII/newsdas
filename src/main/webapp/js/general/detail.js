var top_split = [];
var bottom_spli = [];
var middle_split = [];
for (var i = 0; i < 1000; i++) {
	var b_arr = [];
	b_arr.push(i);
	b_arr.push(25);
	bottom_spli.push(b_arr);
	var s_arr = [];
	s_arr.push(i);
	s_arr.push(55);
	middle_split.push(s_arr);
	var t_arr = [];
	t_arr.push(i);
	t_arr.push(20);
	top_split.push(t_arr);
}
function drawEcharts(id, title, times, data,work_data,markPointData, color) {
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
			position: ['50%', '50%']
		},
		dataZoom : {
			start : 0,
			end : dataZoom
		},
		xAxis : {
			type : 'category',
			data : times
		},
		yAxis : {
			scale : true,
			splitArea : {
				show : true
			}
		},
		series : [ {
			name : title,
			type : 'line',
			itemStyle : {
				normal : {
					color : color
				}
			},
			data : data,
			markPoint : {
                data :markPointData
            },
		},{

			name : '',
			type : 'line',
			smooth : true,
			symbol : "none",
			stack : true,
			itemStyle : {
				normal : {
					opacity : 0.2,
					color : 'rgba(231,133,131,0.2)',
					lineStyle : {
						opacity : 0.2,
						color : 'rgba(231,133,131,0.2)'
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
					color : 'rgba(231,233,131,0.2)',
					lineStyle : {
						opacity : 0.2,
						color : 'rgba(231,233,131,0.2)'
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
					color : 'rgba(172,231,131,0.2)',
					lineStyle : {
						opacity : 0.2,
						color : 'rgba(172,231,131,0.2)'
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