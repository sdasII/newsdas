/**
 * 小区列表
 */
var cellListUrl = ctx + '/alarm/celllist';
var updatetimeUrl = ctx + "/alarm/updatetime"
var cellDetailUrl=ctx+"/alarm/celllist/incell";
/*
 * 全局查询字段
 */
//全局查询参数
var date_Type = "day";//数据时间类型
var start_Time;
var end_Time;
var cell_Name;
var result_Status;
/*
 * 全就查询字段完 
 */

$(function(){
	//最新时间
	$.ajax({
		url:updatetimeUrl,
		type:"post",
		success:function(data){
			$("#updateTime").html("最新发布时间： "+data);
		}
	});
    $('#table_list_1').bootstrapTable({
        cache : false,
        striped : true,
        pagination : true,
        toolbar : '#toolbar',
        pageSize : 10,
        pageNumber : 1,  
        pageList : [ 10, 20, 30 ],
        clickToSelect : true,
        sidePagination : 'server',// 设置为服务器端分页
        columns : [
            { field : "cellname", title : "小区名称", align : "center", valign : "middle",
            	formatter:function(value,row,index){
                    var url = ctx + "/alarm/todetail";
                    var params = "[{\"key\":\"cell_code\",\"value\":\""+value+"\"}]";
                    var url = '<a href=javascript:iframeconvert("' + url + '","小区日常监控",' + params + ')>'+value+'</a>';
                    return url;
              }},
            { field : "yyyymmdd", title : "日期", align : "center", valign : "middle"},
            { field : "hour", title : "时间", align : "center", valign : "middle"},
                   { field : "result", title : "状态", align : "center", valign : "middle",
                    formatter:function(value,row,index){
                          var str="";
                          if(value==0){
                           str="事件"
                          }else if(value==1){
                           str="亚健康"
                          }else if(value==2){
                           str="健康"
                          }else if(value==3){
                           str="计算无结果"
                          }
                           return str;
                     }},
             { field : "calcultime", title : "发布时间", align : "center", valign : "middle",
                  formatter:function(value,row,index){
                      var jsDate = new Date(value);
                      var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
                       return UnixTimeToDate;
                     }
            }
            
        ],
        onPageChange : function(size, number) {
            globalSelect();
        },
        formatNoMatches : function() {
            return "查询无结果";
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
    //年月默认值
    var date=new Date;
    var year=date.getFullYear(); 
    var month=date.getMonth()+1;
    month =(month<10 ? "0"+month:month); 
    var mydate = (year.toString()+month.toString());
    $(".form_datetime").val(mydate);
    
   
    globalSelect();
});

//初始化子表格
/*function  detail_table(index, row, $detail){
    var cur_table = $detail.html('<table></table>').find('table');
    $(cur_table).bootstrapTable({
        url: cellDetailUrl,
        method: 'get',
        queryParams: {
        	"name": row.cell_code,
        	"yyyymmdd": row.yyyyMMdd,
        	"type":$("#status").val()
        },
        pagination : false,
        pageSize: 10,
        pageList: [10, 25],
        columns : [{  
					    title: '序号',align : "center", valign : "middle",
					    formatter: function (value, row, index) {  
					        return index+1;  
					    }  
					},
                   { field : "cellname", title : "小区名称", align : "center", valign : "middle",
                   	formatter:function(value,row,index){
                           var url = ctx + "/alarm/todetail";
                           var params = "[{\"key\":\"cell_code\",\"value\":\""+value+"\"}]";
                           var url = '<a href=javascript:iframeconvert("' + url + '","小区日常监控",' + params + ')>'+value+'</a>';
                           return url;
                     }},
                   { field : "hour", title : "时间", align : "center", valign : "middle"},
                   { field : "result", title : "状态", align : "center", valign : "middle",
                   	formatter:function(value,row,index){
                          var str="";
                          if(value==0){
                       	   str="事件"
                          }else if(value==1){
                       	   str="亚健康"
                          }else if(value==2){
                       	   str="健康"
                          }else if(value==3){
                       	   str="计算无结果"
                          }
                           return str;
                     }},
                   { field : "calcultime", title : "发布时间", align : "center", valign : "middle",
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
/*// 查询表格信息
function searchInfo() { 
    var bsdata = {}; 
	bsdata.cellname=$("#name").val();
	bsdata.type = date_Type;
    
    commonRowDatas("table_list_1", bsdata, cellListUrl,"commonCallback", true);
}*/

/**
 * 查询按钮响应
 * 请注意全局查询条件的赋值！！！
 */
function globalSelect(){
    $("#table_loadbk").show();
    $("#table_load").show();
    var bsdata = {}; 
	cell_Name = $("#name").val();
    bsdata.cellname = cell_Name;
    bsdata.type = date_Type;
    if(date_Type=="select"){    	 
        start_Time = $("#start").val();
        bsdata.start = start_Time
    	end_Time = $("#end").val();
        bsdata.end = end_Time;
    }
    result_Status = $("#status").val();
    bsdata.result = result_Status;
    commonRowDatas("table_list_1", bsdata, cellListUrl, "commonCallback", "hide");
}
var result_export_url = ctx + "/cell/result/export"
var history_export_url = ctx + "/cell/healthtrend/export";
//导出
function resultexportExcel(){
	$("#load2").show();
    var export_type = $("#type").val();
    if(date_Type=="select"){         
        start_Time = $("#start").val();
        end_Time = $("#end").val();
    }
    var form = $("<form></form>").attr("action", result_export_url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name","type").attr("value", date_Type));
    form.append($("<input></input>").attr("type", "hidden").attr("name","starttime").attr("value", start_Time));
    form.append($("<input></input>").attr("type", "hidden").attr("name","endtime").attr("value", end_Time));
    form.append($("<input></input>").attr("type", "hidden").attr("name","cellname").attr("value", cell_Name));
    form.append($("<input></input>").attr("type", "hidden").attr("name","export_type").attr("value", export_type));
    form.appendTo('body').submit().remove();
    setTimeout(function(){ $("#load2").hide();}, 3000);
}
function exportExcel(){
	$("#load1").show();
    if(date_Type=="select"){         
        start_Time = $("#start").val();
        end_Time = $("#end").val();
    }
    var form = $("<form></form>").attr("action", history_export_url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name","type").attr("value", date_Type));
    form.append($("<input></input>").attr("type", "hidden").attr("name","starttime").attr("value", start_Time));
    form.append($("<input></input>").attr("type", "hidden").attr("name","endtime").attr("value", end_Time));
    form.append($("<input></input>").attr("type", "hidden").attr("name","cellname").attr("value", cell_Name));   
    form.appendTo('body').submit().remove();
    setTimeout(function(){ $("#load1").hide();}, 3000);
}



function clear(){
	$("#name").val("");
    $("#status").val("全部");
    cell_Name="";
    result_Status="-1"; 
}
$("#clear").click(function() {
	clear();
});
function searchtimeselect(obj) {
	$(obj).parent().find(".btn").removeClass("btn-info");
	$(obj).parent().find(".btn").removeClass("btn-white");
	$(obj).parent().find(".btn").addClass("btn-white");
	$("#searchinselect").removeClass("btn-white");
	$("#searchinselect").addClass("btn-info");
	$("#searchimeselect").css("display", "inline");
	date_Type="select";
}
function searchoneday(obj) {
	$(obj).parent().find(".btn").removeClass("btn-info");
	$(obj).parent().find(".btn").removeClass("btn-white");
	$(obj).parent().find(".btn").addClass("btn-white");
	$("#searchinday").removeClass("btn-white");
	$("#searchinday").addClass("btn-info");
	$("#searchimeselect").css("display", "none");
    date_Type = "day";
    globalSelect();
}
function searchoneweek(obj) {
	$(obj).parent().find(".btn").removeClass("btn-info");
	$(obj).parent().find(".btn").removeClass("btn-white");
	$(obj).parent().find(".btn").addClass("btn-white");
	$("#searchinweek").removeClass("btn-white");
	$("#searchinweek").addClass("btn-info");
	$("#searchimeselect").css("display", "none");
	date_Type = "week";
    globalSelect();
}
function searchonemonth(obj) {
	$(obj).parent().find(".btn").removeClass("btn-info");
	$(obj).parent().find(".btn").removeClass("btn-white");
	$(obj).parent().find(".btn").addClass("btn-white");
	$("#searchinmonth").removeClass("btn-white");
	$("#searchinmonth").addClass("btn-info");
	$("#searchimeselect").css("display", "none");
	date_Type = "month";
    globalSelect();
}
/*function workQuery(type, start, end) {
	if ("day" == type) {
		var data = {};
		data.type = "day";
        date_Type = "day";
	} else if ("week" == type) {
		var data = {};
		data.type = "week";
        date_Type = "week";
	} else if ("month" == type) {
		var data = {};
		data.type = "month";
        date_Type = "month";
	} else {
		var data = {};
		data.type = "select";
        date_Type = "select";
		data.starttime = start;
        start_Time = start;
		data.endtime = end;
        end_Time = end;
	}
    
	commonRowDatas("table_list_1", data, cellListUrl, "commonCallback", true)
}*/
/*function query() {
	var start = $("#start").val();
	var end = $("#end").val();
	workQuery("select", start, end);
}*/