/**
 * 小区列表
 */
var cellListUrl = ctx + '/cellinfo/getlist';
var setUsedUrl= ctx + '/cellinfo/setUsed';
var clearUsedUrl= ctx + '/cellinfo/clearUsed';
$(function(){
    $('#table_list').bootstrapTable({
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
            { checkbox: true },
            { field : "", title : "序号", align : "center", valign : "middle",
            	formatter: function (value, row, index) {  
                    return index+1;  
                }
            },
            { field : "cell_code", title : "小区ID", align : "center", valign : "middle",width:'35%'},
            { field : "cell_name", title : "小区名称", align : "center", valign : "middle",width:'35%'},
            { field : "station_code", title : "所属基站", align : "center", valign : "middle",width:'15%'},
            { field : "in_used", title : "是否在用", align : "center", valign : "middle",width:'5%',
            	formatter:function(value,row,index){
                    if(value=="0"){
                    	return "否";
                    }else if(value=="1"){
                    	return "是";
                    }
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
    searchInfo();
});
//全局查询参数
var bsdata = {};
// 查询表格信息
function searchInfo() {
	bsdata.type=$("#type").val();
    commonRowDatas("table_list", bsdata, cellListUrl, "commonCallback", true);
}
function setUsed(){
	var selected= $("#table_list").bootstrapTable('getSelections');
	if(selected.length>0){
		var selectedId="";
		$.each(selected,function(i,e){
			selectedId+=e.cell_code+",";
		});
		$.ajax({
			url:setUsedUrl,
			type:"post",
			data:{"ids":selectedId},
			success:function(data){
				 swal({
				        title: "修改成功",
				        text: "已经内容修改为“使用”状态",
				        type: "success",
				        showCancelButton: false,
				        confirmButtonColor: "#1a7bb9",
				        confirmButtonText: "确定",
				        closeOnConfirm: false
				    });
				commonRowDatas("table_list", bsdata, cellListUrl, "commonCallback", true);
			}
		});
	}
}
function clearUsed(){
	var selected= $("#table_list").bootstrapTable('getSelections');
	if(selected.length>0){
		var selectedId="";
		$.each(selected,function(i,e){
			selectedId+=e.cell_code+",";
		});
		$.ajax({
			url:clearUsedUrl,
			type:"post",
			data:{"ids":selectedId},
			success:function(data){
				swal({
			        title: "修改成功",
			        text: "已经取消“使用”状态",
			        type: "success",
			        showCancelButton: false,
			        confirmButtonColor: "#1a7bb9",
			        confirmButtonText: "确定",
			        closeOnConfirm: false
			    });
				commonRowDatas("table_list", bsdata, cellListUrl, "commonCallback", true);
			}
		});
	}
}
