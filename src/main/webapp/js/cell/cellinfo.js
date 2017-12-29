/**
 * 小区列表
 */
var cellListUrl = ctx + '/cellinfo/getlist';
var setUsedUrl = ctx + '/cellinfo/setUsed';
var clearUsedUrl = ctx + '/cellinfo/clearUsed';
var updatetimeUrl = ctx + "/alarm/updatetime"
var counts = ctx + "/alarm/lastHourClassCount";
var export_url = ctx + "/cellinfo/export"; 
$(function() {
	// 最新时间
	$.ajax({
		url : updatetimeUrl,
		type : "post",
		async : false,
		success : function(data) {
			$("#updateTime").html("最新发布时间： " + data);
		}
	});
	// 监控小区个数
	$.ajax({
		url : counts,
		type : "post",
		async : false,
		success : function(data) {
			$("#counts").html("共有" + data.rows.all + "个小区被监控");
		}
	});

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
		columns : [/* {
			checkbox : true
		}, */{
			field : "",
			title : "序号",
			align : "center",
			valign : "middle",
			width : '5%',
			formatter : function(value, row, index) {
				return index + 1;
			}
		},/* {
			field : "cell_code",
			title : "小区ID",
			align : "left",
			valign : "left",
			width : '35%'
		}, */{
			field : "cell_name",
			title : "小区名称",
			align : "left",
			valign : "left",
			width : '55%'
		}, {
			field : "station_code",
			title : "所属基站",
			align : "left",
			valign : "left",
			width : '55%'
		}, {
			field : "in_used",
			title : "是否使用",
			align : "center",
			valign : "middle",
			width : '5%',
			formatter : function(value, row, index) {
				if (value == "0") {
					return "否";
				} else if (value == "1") {
					return "是";
				}
			}
		} ],
		onPageChange : function(size, number) {
			searchInfo();
		},
		formatNoMatches : function() {
			return NOT_FOUND_DATAS;
		}
	});
	searchInfo();
});
// 全局查询参数
var bsdata = {};
// 查询表格信息
function searchInfo() {
	bsdata.type = $("#type").val();
	bsdata.cell_name = $("#name").val();
	commonRowDatas("table_list", bsdata, cellListUrl, "commonCallback", true);
}
//导入
function import_Excel(){
	
}
//导出
function export_Excel(){
	formSubmit(export_url,"post",[]);
}

function setUsed() {
	// 获取选中状态的总数,若大于350，提示客户，不能继续增加。
	/*
	 * $.ajax({ url:setUsedUrl, type:"post", success:function(data){
	 *  } })
	 */
	var selected = $("#table_list").bootstrapTable('getSelections');
	if (selected.length > 0) {
		if (selected.length > 350) {
			swal("修改失败", "一次性选中总数不能大于350个，修改失败",
			"error");
		} else {

			var selectedId = "";
			$.each(selected, function(i, e) {
				selectedId += e.cell_code + ",";
			});
			$
					.ajax({
						url : setUsedUrl,
						type : "post",
						data : {
							"ids" : selectedId
						},
						success : function(data) {
							swal(
									{
										title : "修改成功",
										text : "已经内容修改为“使用”状态。小区状态更换后，需要重新计算小区的月度模式，请到“数据存储”-->“中兴指标全网数据（zip文件）”——>“模式计算”，按照月份计算",
										type : "success",
										showCancelButton : true,
										confirmButtonColor : "#1a7bb9",
										confirmButtonText : "确定",
										cancelButtonText : "取消",
										closeOnConfirm : false
									}, function(isConfirm) {
										if (isConfirm) {// 进行页面跳转
											iframeconvert(
													"/newsdas/data/offline",
													"数据存储");
										} else {
											swal("修改成功", "小区月度模式未重新计算",
													"warning");
										}
									});
							commonRowDatas("table_list", bsdata, cellListUrl,
									"commonCallback", true);
						}
					});
		}
	}
}
function clearUsed() {
	var selected = $("#table_list").bootstrapTable('getSelections');
	if (selected.length > 0) {
		if (selected.length > 350) {
			swal("修改失败", "一次性选中总数不能大于350个，修改失败",
			"error");
		} else {
		var selectedId = "";
		$.each(selected, function(i, e) {
			selectedId += e.cell_code + ",";
		});
		$.ajax({
			url : clearUsedUrl,
			type : "post",
			data : {
				"ids" : selectedId
			},
			success : function(data) {
				swal({
					title : "修改成功",
					text : "已经取消“使用”状态",
					type : "success",
					showCancelButton : false,
					confirmButtonColor : "#1a7bb9",
					confirmButtonText : "确定",
					closeOnConfirm : false
				});
				commonRowDatas("table_list", bsdata, cellListUrl,
						"commonCallback", true);
			}
		});
		}
	}
}
