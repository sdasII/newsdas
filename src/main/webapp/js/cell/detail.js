var times=[];
var global_type = "day";// 全局日期选择
var starttime;
var endtime;
//var data=[];//testData
var updateTime='';//最新更新时间

var updateTimeUrl = ctx + "/alarm/cellupdatetime";
var imgUrl='image://../style/export.png';
var rtratioCharts = echarts.init($("#rtratio").get(0));
var rtratioOption;
var historyCharts = echarts.init($("#historyCharts").get(0));
var cellListUrl = ctx + '/cellinfo/getlist';
$(function(){
	//时间选择窗口
	$(".datePicker").click(function() {
				starttime="";
				endtime="";
					$(this).parent().find(".btn-info").removeClass("btn-info");
					$(this).parent().find(".btn-white").removeClass("btn-white");
					$(this).parent().find("button").addClass("btn-white");
					$(".search").removeClass("btn-white");
					$(".search").addClass("btn-info");
					$(this).parent().children(":last").css("display", "none");
						if ($(this).html() == "最近一日") {
							global_type = "day";
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						} else if ($(this).html() == "周") {
							global_type = "week";
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						} else if ($(this).html() == "月") {
							global_type = "month";
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
						}else if ($(this).html() == "按时间选择") {
							$(this).removeClass("btn-white");
							$(this).addClass("btn-info");
							$(this).parent().children(":last").css("display", "block");
							global_type = "select";
						}
						if(global_type != "select"){
                            global_page_query();
						}
					});	
	//小区位置
	$.ajax({
		url:cellListUrl,
		data:{"cellname":cell_code},
		type:"post",
		success:function(data){
			if(data.rows.rows.length>0){
				data=data.rows.rows[0];
				if(data.station_latitude!=null&&data.station_latitude!=""&&data.station_longitude!=""&&data.station_longitude!=null){
					var point=new BMap.Point(data.station_longitude, data.station_latitude);
					map.centerAndZoom(point, 12); // 初始化地图,设置中心点坐标和地图级别
					var marker = new BMap.Marker(point);
					map.addOverlay(marker);
				}
			}
		}
	});
	//更新时间
	$.ajax({
		url : updateTimeUrl,
		type : "post",
		data : {
			'cellname' : cell_code
		},
		success : function(data, status) {
			$("#updateTime").html("最新发布时间 : "+data);
			updateTime=data;
		}
	});
	//datapicker
    $(".form_datetime").datetimepicker({
    	 format: 'yyyymm',  
    	 startView: 'year',
         minView:'year',
         maxView:'decade',
         language:  'zh-CN' 
    });
    rtRatio();
});


/*
 * 全页面查询
 */
function global_page_query(){
	starttime=$("#starttime").val();
	endtime=$("#endtime").val();
	var title=$("#topTabs").find(".active").find("a").html()
    searchReultInfo();
	if (title == "健康诊断结果") {
		getcharts("#rtratio", "健康诊断结果","rgb(46,199,201)",global_type,starttime,endtime);
		historyTrendQuery(global_type,starttime,endtime);
	} else {
		getcharts("#ratiotrend", "专家指标集","#1c84c6",global_type,starttime,endtime);
	}   
    searchCapacityInfo();
    searchComplaintInfo()
}

function switchTab(id, title,color){
	$(".datePicker").parent().find(".btn-info").removeClass("btn-info");
	$(".datePicker").parent().find(".btn-white").removeClass("btn-white");
	$(".datePicker").parent().find("button").addClass("btn-white");
	$(".datePicker").parent().find("button:first").removeClass("btn-white");
	$(".datePicker").parent().find("button:first").addClass("btn-info");
	getcharts(id, title,color,"day","","");
}
//————————————————————————————————————小区健康判别结果————————————————————————————————-\\
//小区健康判别结果
var tableUrl=ctx+"/alarm/celllist";
var tableDetail=ctx+"/alarm/celllist/incell";
$(function(){
    $('#alarm_table').bootstrapTable({
                cache : false,
                striped : true,
                pagination : true,
                toolbar : '#pager_alarm_table',
                pageSize : 10,
                pageNumber : 1,
                pageList : [ 5, 10, 20 ],
                clickToSelect : true,
                //detailView: true,//父子表
                sidePagination : 'server',// 设置为服务器端分页
                columns : [
                    {  
                        title: '序号',align : "center", valign : "middle",
                        formatter: function (value, row, index) {  
                            return index+1;  
                        }  
                    },
                    { field : "yyyymmdd", title : "日期", align : "center", valign : "middle"},
                    { field : "hour", title : "时刻", align : "center", valign : "middle"},
                    { field : 'result', title : '风险提示', align : 'center', valign : 'middle',
                        formatter:function(value,row,index){
                              var str="";
                              if(value==0){
                                  str="事件"
                              }else if(value==1){
                                  str="亚健康"
                              }else if(value==2){
                                  str="健康"
                              }
                              var time=row.yyyymmdd+" "+row.hour+":00";
                              return '<a href="#rtratio" onclick="changeZoom('+"'"+time+"'"+')">'+str+"</a>";
                             }
                    },
                    { field : 'calcultime', title : '发布时间', align : 'center', valign : 'middle',
                        formatter:function(value,row,index){
                              var jsDate = new Date(value);
                              var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
                               return UnixTimeToDate;
                             }
                    }
                ],
                onExpandRow: function (index, row, $detail) {
                	//detail_table(index, row, $detail);
                },
                onPageChange : function(size, number) {
                        searchReultInfo();
                },
                formatNoMatches : function() {
                    return "无查询结果";
                }
            });
  
      searchReultInfo();
})
/*//初始化子表格
function  detail_table(index, row, $detail){
    var cur_table = $detail.html('<table></table>').find('table');
    $(cur_table).bootstrapTable({
        url: tableDetail,
        method: 'get',
        queryParams: {
        	"name": cell_code,
        	"yyyymmdd": row.yyyyMMdd,
        	"type":10
        },
        //ajaxOptions: {"yyyymmdd": parentid},
        //uniqueId: "yyyymmdd",
        pageSize: 10,
        pageList: [10, 25],
        pagination : false,
        columns : [
            {  
                title: '序号',align : "center", valign : "middle",
                formatter: function (value, row, index) {  
                    return index+1;  
                }  
            },
            { field : "hour", title : "时间", align : "center", valign : "middle"},
            { field : 'result', title : '风险提示', align : 'center', valign : 'middle',
                formatter:function(value,row,index){
                      var str="";
                      if(value==0){
                          str="事件"
                      }else if(value==1){
                          str="亚健康"
                      }else if(value==2){
                          str="健康"
                      }
                      var time=row.yyyymmdd+" "+row.hour+":00";
                      return '<a href="#rtratio" onclick="changeZoom('+"'"+time+"'"+')">'+str+"</a>";
                     }
            },
            { field : 'calcultime', title : '发布时间', align : 'center', valign : 'middle',
                formatter:function(value,row,index){
                      var jsDate = new Date(value);
                      var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
                       return UnixTimeToDate;
                     }
            }
        ],
        onLoadSuccess: function(data){ //加载成功时执行 
    	    data= eval(data); 
    	    $(cur_table).bootstrapTable('load', data.rows);
    	   }
    });
}*/

function searchReultInfo(){
    var data = {};
    data.cellname = cell_code;
    data.type = global_type;
    data.starttime = starttime;
    data.endtime = endtime;   
    data.cell = "cell"; 
    $(".loading_bk").show();
    $(".loading").show();
    commonRowDatas("alarm_table", data, tableUrl, "commonCallback", "hide");
}
//————————————————————————————————————小区健康判别结果end————————————————————————————————-\\
//————————————————————————————————————导出————————————————————————————————-\\
var history_export_url = ctx + "/cell/healthtrend/export";
var real_export_url = ctx + "/cell/result/export";
function exportExcel_history(title){
    /*
     * 以模拟表单方式发送请求
     */
    var form = $("<form></form>").attr("action", history_export_url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name","cellname").attr("value", cell_code));
    form.append($("<input></input>").attr("type", "hidden").attr("name","title").attr("value", title));
    form.append($("<input></input>").attr("type", "hidden").attr("name","type").attr("value", global_type));
    if(global_type=="select"){
        form.append($("<input></input>").attr("type", "hidden").attr("name","starttime").attr("value", starttime));
        form.append($("<input></input>").attr("type", "hidden").attr("name","endtime").attr("value", endtime));
    }       
    form.appendTo('body').submit().remove();
    
}
function exportExcel_real(title){
    var form = $("<form></form>").attr("action", real_export_url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name","cellname").attr("value", cell_code));
    form.append($("<input></input>").attr("type", "hidden").attr("name","title").attr("value", title));
    form.append($("<input></input>").attr("type", "hidden").attr("name","type").attr("value", global_type));
    if(global_type=="select"){
        form.append($("<input></input>").attr("type", "hidden").attr("name","starttime").attr("value", starttime));
        form.append($("<input></input>").attr("type", "hidden").attr("name","endtime").attr("value", endtime));
    }       
    form.appendTo('body').submit().remove();
}
//————————————————————————————————————导出end——————————————————————————————-\\
//————————————————————————————————————健康度————————————————————————————————-\\
var rtratioUrl = ctx + "/cell/rtratio";
setInterval(function() {
	rtRatio()
}, 5 * 60 * 1000);

var refreshcount = 0;
function rtRatio() {
if (refreshcount < 23) {
refreshcount++;
} else {
refreshcount = 0;
}
$.ajax({
		url : rtratioUrl,
		type : "post",
		data : {
			'cellname' : cell_code,
			'count' : refreshcount
		},
		success : function(data, status) {
			var ratio = data.ratio;
			if (ratio != undefined && ratio != "" && ratio != null) {
				$("#h_ratio").text("健康度： "+ratio);
				if (ratio > 80) {
					$("#h_ratio").css("color", "green");
				} else if (ratio > 25 && ratio <= 80) {
					$("#h_ratio").css("color", "#B9C83F");
				} else {
					$("#h_ratio").css("color", "red");
				}
			} else {
				$("#h_ratio").css("display", "none");
			}

		}
	});
}
//————————————————————————————————————健康度end————————————————————————————————-\\
//————————————————————————————————————小区内的工单————————————————————————————————-\\
//性能工单
var capacityurl = ctx + "/capacitywork/rt/list";
//投诉工单
var compliainurl = ctx + "/complain/getcomplist"; 
$(function(){ 
    $('#table_list_work').bootstrapTable({
        cache : false,
        striped : true,
        pagination : true,
        toolbar : '#pager_list_work',
        pageSize : 10,
        pageNumber : 1,
        pageList : [ 5, 10, 20 ],
        clickToSelect : true,
        sidePagination : 'server',// 设置为服务器端分页
        columns : [
            { field : "occurrence_time", title : "发生时间", align : "center", valign : "middle",
                formatter:function(value,row,index){
                      var jsDate = new Date(value);
                      var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
                       return UnixTimeToDate;
                     }
            },
            { field : 'monitor_content', title : '监控内容', align : 'center', valign : 'middle' },
            { field : 'reasons', title : '原因', align : 'center', valign : 'middle' },
            { field : 'monitor_value', title : '监控时值', align : 'center', valign : 'middle' }
        ],
        onPageChange : function(size, number) {
            searchCapacityInfo();
        },
        formatNoMatches : function() {
            return "无查询结果";
        }
    });
    $('#table_list_work2').bootstrapTable({
        cache : false,
        striped : true,
        pagination : true,
        toolbar : '#pager_list_work2',
        pageSize : 10,
        pageNumber : 1,
        pageList : [ 5, 10, 20 ],
        clickToSelect : true,
        sidePagination : 'server',// 设置为服务器端分页
        columns : [
            { field : "recordtime", title : "受理时间", align : "center", valign : "middle",
                formatter:function(value,row,index){
                    if(value!=""||value!=null){
                         var jsDate = new Date(value);
                      var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
                       return UnixTimeToDate;
                    }
               }
            },
            { field : 'phonenumber', title : '受理电话', align : 'center', valign : 'middle'},
            { field : 'servicerequesttype', title : '服务请求类别', align : 'center', valign : 'middle'},
            { field : 'complaintdetailinfo', title : '问题细项', align : 'center', valign : 'middle'},
            { field : 'live_cellname1', title : '常住小区1', align : 'left', valign : 'left',
                formatter:function(value,row,index){
                    var link = ctx + "/alarm/todetail";
                    var url='<a href="javascript:iframeconvert('
                        +"'"+link+"','小区信息',"+"[{'key':'cell_code','value':'"+value+"'}]"+')">'
                        +value+'</a>';
                    return url;
                }
             },
            { field : 'live_cellname2', title : '常住小区2', align : 'left', valign : 'left',
                formatter:function(value,row,index){
                    var link = ctx + "/alarm/todetail";
                    var url='<a href="javascript:iframeconvert('
                        +"'"+link+"','小区信息',"+"[{'key':'cell_code','value':'"+value+"'}]"+')">'
                        +value+'</a>';
                    return url;
                }
             },
            { field : 'live_cellname3', title : '常住小区3', align : 'left', valign : 'left',
                formatter:function(value,row,index){
                    var link = ctx + "/alarm/todetail";
                    var url='<a href="javascript:iframeconvert('
                        +"'"+link+"','小区信息',"+"[{'key':'cell_code','value':'"+value+"'}]"+')">'
                        +value+'</a>';
                    return url;
                }
             }
        ],
        onPageChange : function(size, number) {
            searchComplaintInfo();
        },
        formatNoMatches : function() {
            return "无查询结果";
        }
    });
    searchCapacityInfo();
});
function searchCapacityInfo(){
    var data = {};
    data.cellname = cell_code;
    data.type = global_type;
    data.starttime = starttime;
    data.endtime = endtime;    
    commonRowDatas("table_list_work", data, capacityurl, "commonCallback", true);
}
function searchComplaintInfo(){
    var data = {};
    data.cellname = cell_code;
    data.type = global_type;
    data.starttime = starttime;
    data.endtime = endtime;    
    commonRowDatas("table_list_work2", data, compliainurl, "commonCallback", true);
}

//————————————————————————————————————小区内的工单end————————————————————————————————-\\
//————————————————————————————————————历史健康度————————————————————————————————-\\
var top_split = [];
var bottom_spli = [];
var middle_split = [];
var bottom_bott=[];
var nodata_split=[];
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
    //
}
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
        toolbox: {
            show : true,
            top:20,
            right:150,
            feature : {
                saveAsImage:{
                    show:true,
                    name:cellname+"-历史健康度"
                },
                myTool2 : {
                    show: true,
                    title:'数据导出',
                    icon:imgUrl, //图标
                    onclick:function(obj) {
                        exportExcel_history(obj.option.series[0].name);
                       }
                    }  
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
                    }]
        },
        dataZoom : {
                    start : 0,
                    end : 100
                },
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
var historyurl = ctx + "/cell/healthtrend";
$(function(){
    historyTrendQuery(global_type,"","");
});

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
                success : function(data, status) {
                    //var data = eval('(' + data + ')');
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
                         histroy_trend.series[1].data = bottom_spli;
                         histroy_trend.series[2].data = middle_split;
                         histroy_trend.series[3].data = top_split;

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
                    //histroy_trend.dataZoom.startValue=axis[0];
                    //histroy_trend.dataZoom.endValue=axis[parseInt(axis.length/2)];
                    historyCharts.setOption(histroy_trend);
                    historyCharts.resize();
                    $("#ratiotrend_loadbk").hide();
                    $("#ratiotrend_load").hide();
                }
            });
}
//————————————————————————————————————历史健康度end————————————————————————————————-\\
//————————————————————————————————————历史判别结果————————————————————————————————-\\
var dataUrl=ctx+"/cell/cellResultHistroy";
/*$(function(){
    getcharts("#rtratio", "健康诊断结果","rgb(46,199,201)",global_type);  
})*/
function getcharts(id, title,color,date_value,starttime,endtime){
    //loading显示
    $("#ratiotrend_loadbk").show();
    $("#ratiotrend_load").show();
    
    $.ajax({
        url:dataUrl,
        type:"post",
        data:{
            "cellname":cellname,
            "type":date_value,
            "starttime":starttime,
            "endtime":endtime
        },
        success:function(data){
            //data=eval('(' + data + ')');
            times=[];
            bottom_spli=[];
            middle_split=[];
            top_split=[];
            bottom_bott=[];
            nodata_split=[];
            dataArr=[];
            var markPointData_0=[];//事件
            var markPointData_1=[];//亚健康
            var markPointData_3=[];//无结果
            if(data.rows.length>0){
                if(data.rows.length>0){
                    $.each(data.rows,function(i,e){
                    	 var point=[];
                         var arr = [];
                         arr.push(e.time);
                         arr.push(1);
                         bottom_spli.push(arr);
                         middle_split.push(arr);
                         top_split.push(arr);
                         var b_arr = [];
                         b_arr.push(e.time);
                         b_arr.push(1.5);
                         bottom_bott.push(b_arr);
                         var no_arr = [];
                         no_arr.push(e.time);
                         no_arr.push(2);
                         nodata_split.push(no_arr);
                         times.push(e.time);
                        if(e.result==0){
                            dataArr.push(0.5);
                            var point={};
                            point.name="事件";
                            point.value="事件";
                            point.xAxis=e.time;
                            point.yAxis=0.5;
                            markPointData_0.push(point);
                        }else if(e.result==1){
                            dataArr.push(1.5);
                            var point={};
                            point.name="亚健康";
                            point.value="亚健康";
                            point.xAxis=e.time;
                            point.yAxis=1.5;
                            markPointData_1.push(point);
                        }else if(e.result==2){
                            dataArr.push(2.5);
                        }else{
                            dataArr.push(2);
                            var point={};
                            point.name="无结果";
                            point.value="无结果";
                            point.xAxis=e.time;
                            point.yAxis=2;
                            markPointData_3.push(point);
                        }
                    });
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
            drawEcharts(id, title, times, dataArr,markPointData_0,markPointData_1,markPointData_3,color);
        }
    });
}
function drawEcharts(id, title, times, data,markPointData_0,markPointData_1,markPointData_3,color) {
    var dataZoom=100;
    if(data.length>100){
        dataZoom=30;
    }
    rtratioCharts = echarts.init($(id).get(0));
    rtratioOption = {
        legend : {
            data : [title]
        },
        tooltip : {
            trigger : 'axis', // 触发类型：坐标轴触发
            //position: ['50%', '50%'],
            formatter : function(params) {
                var status="";
                if(params[0].value==0.5){
                    status="事件";
                }else if(params[0].value==1.5){
                    status="亚健康";
                }else if(params[0].value==2.5){
                    status="健康";
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
            top:20,
            right:150,
            feature : {
                saveAsImage:{
                    show:true,
                    name:cellname+"-健康诊断结果"
                },
                mytool1 : {
                    show: true,
                    title:'数据导出',
                    icon:imgUrl,
                    option:{},    
                    onclick:function(obj) {
                        exportExcel_real(obj.option.series[0].name);
                       }
                    }  
            }  
        },
        dataZoom : {
        	start : 0,
            end : 100
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
             type : 'value',
                axisLabel : {
                    formatter: function(value, index) {
                        if(value==0.5){
                            return "事件";
                        }else if(value==1.5){
                            return "亚健康";
                        }else if(value==2.5){
                            return "健康";
                        }else{
                            return "";
                        }
                    }
                }
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
            data : data,
            markPoint : {
                symbol:'pin',
                symbolSize:30,
                label:{
                    normal:{
                        show:false
                    }
                },
                data :markPointData_0,
                itemStyle:{
                    normal:{
                        color:'red'//'#C1232B'
                    }
                }
            }
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
        },{//亚健康标记所用曲线
            name : '',
            type : 'line',
            smooth : true,
            symbol : "none",
            itemStyle : {
                normal : {
                    opacity : 0.2,
                    color : 'rgba(231,233,131,0.4)',
                    lineStyle : {
                        opacity : 0.2,
                        color : 'rgba(231,233,131,0.4)'
                    }
                }
            },
            data : bottom_bott,
            markPoint : {
                symbol:'pin',
                symbolSize:30,
                label:{
                    normal:{
                        show:false
                    }
                },
                data :markPointData_1,
                itemStyle:{
                    normal:{
                        color:'#F3A43B'
                    }
                }
            }
            
        }/*, {//无结果标记所用曲线
            name : '',
            type : 'line',
            smooth : true,
            symbol : "none",
            itemStyle : {
            	normal : {
                    opacity : 0.2,
                    color : 'rgba(231,233,131,0.4)',
                    lineStyle : {
                        opacity : 0.2,
                        color : 'rgba(231,233,131,0.4)'
                    }
                }
            },
            data : nodata_split,
            markPoint : {
                symbol:'pin',
                symbolSize:30,
                label:{
                    normal:{
                        show:false
                    }
                },
                data :markPointData_3,
                itemStyle:{
                    normal:{
                        color:'gray'
                    }
                }
            }
        }*/]
    };
    rtratioCharts.setOption(rtratioOption);
    rtratioCharts.resize();
}
function toTableData(code){
    top.$("#iframe_detail").attr('src',ctx +"/cell/toTableData?cell_code="+code);
}
//修改图表dataZoom起始位置
function changeZoom(time){
	if(global_type == "day"){
		histroy_trend.dataZoom.start=0;
		rtratioOption.dataZoom.start=0;
		histroy_trend.dataZoom.end=100;
		rtratioOption.dataZoom.end=100;
		rtratioCharts.setOption(rtratioOption);
		historyCharts.setOption(histroy_trend);
	}else{
		 var rtratio_index=isHasElementTwo(rtratioOption.xAxis.data,time);
		 var rtratio_perc=parseInt((rtratio_index/rtratioOption.xAxis.data.length)*100);
		 var histroy_index=isHasElementTwo(histroy_trend.xAxis.data,time);
		 var histroy_perc=parseInt((histroy_index/histroy_trend.xAxis.data.length)*100);
		 if(rtratio_perc>50){
				rtratioOption.dataZoom.start=rtratio_perc-5;
				rtratioOption.dataZoom.end=rtratio_perc+5;
		 }else{
				rtratioOption.dataZoom.start=rtratio_perc;
				rtratioOption.dataZoom.end=rtratio_perc+5;
		 }
		 if(histroy_perc>50){
			 histroy_trend.dataZoom.start=histroy_perc-5;
			 histroy_trend.dataZoom.end=rtratio_perc+5;
		 }else{
			 histroy_trend.dataZoom.start=histroy_perc;
			 histroy_trend.dataZoom.end=histroy_perc+5;
		 }
		 rtratioCharts.setOption(rtratioOption);
		 historyCharts.setOption(histroy_trend);
	}
}
function isHasElementTwo(arr,value){ 
	var str = arr.toString(); 
	var index = str.indexOf(value); 
	if(index >= 0){ 
	//存在返回索引 
	var reg1 = new RegExp("((^|,)"+value+"(,|$))","gi"); 
	return str.replace(reg1,"$2@$3").replace(/[^,@]/g,"").indexOf("@"); 
	}else{
	return -1;//不存在此项 
} 
}
//————————————————————————————————————历史判别结果end————————————————————————————————-\\
