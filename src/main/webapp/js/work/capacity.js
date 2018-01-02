/**
 * by dq 
 * 2017年9月20日下午9:10:31
 * TODO
 */
var capacityworkurl = ctx + "/capacitywork/rt/list";

var aeraurl = ctx + "/capacitywork/belongare";

var updatetimeUrl = ctx + "/capacitywork/updatetime"

var capacityworkexporturl = ctx + "/capacitywork/export"
/*
 * 全局变量
 */
var global_starttime;
var global_endtime;
var global_type = "day";
var global_cellname,global_area,global_content,global_result;


$(function(){
    //列表时间选择
    $(".datePicker").click(function() {
        $("#starttime").val("");
        $("#endtime").val("");
        $(this).parent().find(".btn-info").removeClass("btn-info");
        $(this).parent().find(".btn-white").removeClass("btn-white");
        $(this).parent().find("button").addClass("btn-white");
        $(".search").removeClass("btn-white");
        $(".search").addClass("btn-info");
        $(this).parent().children(":last").css("display", "none");
                global_starttime="";
                global_endtime=""
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
            } else if ($(this).html() == "按时间选择") {
                global_type = "select";
                $(this).removeClass("btn-white");
                $(this).addClass("btn-info");
                $(this).parent().children(":last").css("display", "block");
            }
            if($("#timeselect").is(":hidden")){
                select(global_type);
            }
        });
	//最新时间
	/*$.ajax({
		url:updatetimeUrl,
		type:"post",
		success:function(data){
			$("#updateTime").html("最新发布时间： "+data);
		}
	});*/
    //地区列表
    $.ajax({
        url: aeraurl,
        type:"GET",
        success:function(data,status){
            var areas = data.rows;
            for(var i=0;i<areas.length;i++){
                if (areas[i]!=null) {
                    var option = $('<option>'+areas[i]+'</option>');
                    $("#area").append(option);
                }
            }
        }
    });
    
    
	$('#table_list_1').bootstrapTable({
        cache : false,
        striped : true,
        pagination : true,
        toolbar : '#toolbar',
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
            { field : "cellid", title : "小区名称", align : "left", valign : "left",
                formatter:function(value,row,index){
                    var url = ctx + "/alarm/todetail/";
                    var params = "[{\"key\":\"cell_code\",\"value\":\""+value+"\"}]";
                    var url = '<a href=javascript:iframeconvert("' + url + '","小区信息",' + params + ')>' + value + '</a>';
                     return url;
                }},
            { field : "belong_area", title : "所属区域", align : "center", valign : "middle"},
            { field : 'monitor_content', title : '监控内容', align : 'left', valign : 'middle' },
            { field : 'monitor_value', title : '监控时值', align : 'left', valign : 'middle' },
            { field : 'alerm_level', title : '告警级别', align : 'left', valign : 'middle' },
            { field : 'reasions', title : '原因', align : 'left', valign : 'middle' },
            { field : 'boutique_level', title : '精品级别', align : 'left', valign : 'middle' },
            { field : "limit_times", title : "越限次数", align : "center", valign : "middle"},
            { field : 'complete_time', title : '完成时间', align : 'left', valign : 'middle',
            	formatter:function(value,row,index){
            		if(value!=""&&value!=null){
            			var jsDate = new Date(value);
  	                  var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
  	                   return UnixTimeToDate;
            		}
	              } 
            },
            { field : 'questionflag', title : '状态', align : 'left', valign : 'middle' ,
            	formatter:function(value,row,index){
	                  if(value==0){
	                	  return "高度可疑";
	                  }else if(value==1){
	                	  return "可疑工单";
	                  }else if(value==2){
	                	  return "正常";
	                  }else{
	                	  return "待验证";
	                  }
	                 }
            }
        ],
        rowStyle: function (row, index) {
        	
        	 var style = {};
        	 if(row.questionflag==0){
        		 style={css:{'color':'red'}};
             }else if(row.questionflag==1){
            	 style={css:{'color':'yellow'}};
             }else if(row.questionflag==2){
            	 style={css:{'color':'green'}};
             }else{
            	 //style={css:{'color':'black'}};
             }
             return style;
        },
        onPageChange : function(size, number) {
            searchInfo();
        },
        formatNoMatches : function() {
            return NOT_FOUND_DATAS;
        }
    });
    
    searchInfo();
	
    
	
});
// 查询表格信息
function searchInfo() {
    var data = {};
    data.type = global_type;
    data.starttime = global_starttime;
    data.endtime = global_endtime;
    data.cellname = global_cellname;
    data.area = global_area;
    data.content = global_content;
    data.result = global_result; 
    commonRowDatas("table_list_1", data, capacityworkurl, "commonCallback", true);
}

function select(type){
	global_cellname = $("#name").val();
	global_area = $("#area").val();
	global_content = $("#content").val();
	if(global_type=="select"&&!$("#timeselect").is(":hidden")){
    	global_starttime = $("#starttime").val();
        global_endtime = $("#endtime").val();
    }
    global_result = $("#result").val();
    if(global_result=="正常工单"){
        global_result = 2;
    }else if(global_result=="高度可疑"){
        global_result = 0;
    }else if(global_result=="可疑工单"){
        global_result = 1;
    }
	searchInfo();
	
}
function exportExcel() {
	var data = [];
    var data1 = {};
    data1.name = "type";
    data1.value = global_type;
    var data2 = {};
    data2.name = "starttime";
    data2.value = global_starttime;
    var data3 = {};
    data3.name = "endttime";
    data3.value = global_endtime;
    var data4 = {};
    data4.name = "global_cellname";
    data4.value = global_cellname;
    var data5 = {};
    data5.name = "global_area";
    data5.value = global_area;
    var data6 = {};
    data6.name = "global_content";
    data6.value = global_content;
    var data7 = {};
    data7.name = "global_result";
    data7.value = global_result;
    data.push(data1);
    data.push(data2);
    data.push(data3);
    data.push(data4);
    data.push(data5);
    data.push(data6);
    data.push(data7);
    formSubmit(capacityworkexporturl,"post",data);
}