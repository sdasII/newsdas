/**
 * 小区列表
 */
var cellListUrl = ctx + '/alarm/celllist';
var searchurl="";
$(function(){
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
            { field : "calcultime", title : "计算时间", align : "center", valign : "middle"}            
        ],
        onPageChange : function(size, number) {
            searchInfo();
        },
        formatNoMatches : function() {
            return NOT_FOUND_DATAS;
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
    searchInfo();
});
//全局查询参数
var bsdata = {};
// 查询表格信息
function searchInfo() { 
	bsdata.cellname=$("#name").val();
	bsdata.result=$("#status").val();
    commonRowDatas("table_list_1", bsdata, cellListUrl, "commonCallback", true);
}


function select(){
	var name = $("#name").val();
	var scene = $("#scene").val();
    bsdata.name = name;
    bsdata.scene = scene;             
    commonRowDatas("table_list_1", bsdata, cellListUrl, "commonCallback", true);
   
}
var result_export_url = ctx + "/cell/result/all/export"
var history_export_url = ctx + "/cell/history/all/export";
//导出
function resultexportExcel(){
	$("#load2").show();
	var time=$("#resultexporttime").val();
	if(time==""){//默认为当前月份
		var myDate = new Date();
		time=myDate.getFullYear().toString()+(myDate.getMonth()+1).toString();        		
	}
    var form = $("<form></form>").attr("action", result_export_url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name","month").attr("value", time));     
    form.appendTo('body').submit().remove();
    /*$("#load2").css("display","inline");*/
    setTimeout(function(){ $("#load2").hide();}, 3000);
}
function exportExcel(){
	$("#load1").show();
    var time=$("#exporttime").val();
    if(time==""){//默认为当前月份
        var myDate = new Date();
        time=myDate.getFullYear().toString()+(myDate.getMonth()+1).toString();              
    }
    var form = $("<form></form>").attr("action", history_export_url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name","month").attr("value", time));     
    form.appendTo('body').submit().remove();
    /*$("#load1").css("display","inline");*/
    setTimeout(function(){ $("#load1").hide();}, 3000);
}



function clear(){
	$("#name").val("");
}
$("#clear").click(function() {
	clear();
});
function searchtimeselect() {
	$("#searchinselect").addClass("btn-info");
	$("#searchinselect").removeClass("btn-white");
	$("#searchelect").css("display", "inline");
	$("#searchinmonth").addClass("btn-white");
	$("#searchinmonth").removeClass("btn-info");
	$("#searchinweek").removeClass("btn-info");
	$("#searchinweek").addClass("btn-white");
}
function searchoneday() {
	$("#searchinweek").addClass("btn-info");
	$("#searchinweek").removeClass("btn-white");
	$("#searchinmonth").addClass("btn-white");
	$("#searchinmonth").removeClass("btn-info");
	$("#searchinselect").removeClass("btn-info");
	$("#searchinselect").addClass("btn-white");
	$("#searchtimeselect").css("display", "none");
	workQuery("day");
}
function searchoneweek() {
	$("#searchinweek").addClass("btn-info");
	$("#searchinweek").removeClass("btn-white");
	$("#searchinmonth").addClass("btn-white");
	$("#searchinmonth").removeClass("btn-info");
	$("#searchinselect").removeClass("btn-info");
	$("#searchinselect").addClass("btn-white");
	$("#searchtimeselect").css("display", "none");
	workQuery("week");
}
function searchonemonth() {
	$("#searchinmonth").addClass("btn-info");
	$("#searchinmonth").removeClass("btn-white");
	$("#searchinweek").removeClass("btn-info");
	$("#searchinweek").addClass("btn-white");
	$("#searchinselect").removeClass("btn-info");
	$("#searchinselect").addClass("btn-white");
	$("#searchtimeselect").css("display", "none");
	workQuery("month");
}
function workQuery(type, start, end) {
	if ("day" == type) {
        var data = {};
        data.type = "day";
        commonAjax(searchurl,data,"POST","table_list_1")			
	}else if ("week" == type) {
            var data = {};
            data.type = "week";
            commonAjax(searchurl,data,"POST","table_list_1")			
		} else if ("month" == type) {
            var data = {};
            data.type = "month";
            commonAjax(searchurl,data,"POST","table_list_1");			
		} else {
            var data = {};
            data.starttime = start;
            data.endtime = end;
            commonAjax(searchurl,data,"POST","table_list_1");
		}
}
function query() {
	var start = $("#start").val();
	var end = $("#end").val();
	workQuery("select", start, end);
}