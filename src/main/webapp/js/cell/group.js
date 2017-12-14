/**
 * 小区列表
 */
var cellListUrl = ctx + '/cell/getcelllist';

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
            { field : "cell_code", title : "小区名称", align : "center", valign : "middle"},
            { field : "", title : "健康监控", align : "center", valign : "middle",formatter:function(value,row,index){
                    var url = ctx + "/alarm/todetail";
                    var params = "[{\"key\":\"cell_code\",\"value\":\""+row.cell_code+"\"}]";
                    var url = '<a href=javascript:iframeconvert("' + url + '","小区日常监控",' + params + ')>健康监控</a>';
                    return url;
              }
            }            
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
