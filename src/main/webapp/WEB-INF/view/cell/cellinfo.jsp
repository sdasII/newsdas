<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/include/common.jsp"%>
<link href="${context}/style/loader.css" rel="stylesheet"
	type="text/css">
<link
	href="${context}/lib/hplus/css/plugins/dataTables/dataTables.bootstrap.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${context}/lib/sweetAlert/sweetalert.min.js"></script>
<link href="${context}/lib/sweetAlert/sweetalert.css" rel="stylesheet">
<style type="text/css">
input[type="file"] {
	display: inline;
	opacity: 0;
	position: relative;
	width: 165px;
	z-index: 9
}

.upload_btn {
	width: 160px;
	margin-left: -160px;
	display: inline;
}

.upload_title {
	display: inline;
	margin-top: 10px;
}
.loading img {
	height: 20px;
	margin-right: 5px
}
.btn-primary{margin-left: 15px}
</style>
</head>
<script type="text/javascript">
	$(function() {
		var status = '${success}';//var status = 'success';
		$("#import_load").hide();
		if (status.indexOf("success") > -1) {
			showOnlyMessage(INFO, "上传成功");
			swal({
				title : "上传提示",
				text : "小区状态进行修改后，需要重新计算小区的月度模式。上传完成后将自动跳转到“存储分析”页面，请在“中兴指标全网数据（zip文件）”模块，按照月份重新进行模式计算！",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#1c84c6",
				confirmButtonText : "确定",
				cancelButtonText : "取消",
				closeOnConfirm : false
			}, function(isConfirm) {
				if (isConfirm) {
					iframeconvert("/newsdas/data/offline", "存储分析");
				}
			});
		} else if (status.indexOf("fail") >= 0) {
			showOnlyMessage(ERROR, status);
		}
	})
</script>
<body style="margin-left: 5px; margin-right: 5px;">
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row" style="text-align: center;">
			<h3>
				<span id="updateTime"></span>&nbsp;&nbsp;&nbsp;<span id="counts"></span>
			</h3>
		</div>
		<div class="row">
			<div class="col-sm-16">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>小区列表</h5>
						<div class="ibox-tools"></div>
					</div>
					<div class="ibox-content">
						<div class="form-group">
							<label>小区名称：</label>
							<input type="text"  placeholder="请输入小区名称" id="name" name="name">
							<label for="type">是否使用：</label>
							<select name="type" id="type" class="btn btn-white">
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                             </select>
							<my:btn type="search" onclick="searchInfo()"></my:btn>
							<!-- <button style="margin-left: 5px;" class="btn btn-success"
								onclick="searchInfo();">查询</button> -->
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<!-- <button id="setUsed"  class="btn btn-success" onclick="setUsed()">设为使用</button>
							<button id="clear" style="margin-left: 5px;" class="btn btn-white" onclick="clearUsed()">取消使用</button> -->

							<form action="${context}/cellinfo/import" method="post" style="display: inline;" id="cell_form" enctype="multipart/form-data">
								<input class="btn btn-white" type="file" name="file" id="comlainfile" accept=".xls,.xlsx" multiple="multiple">
								<button class="btn btn-white upload_btn">选择上传文件</button>
								<div class="upload_title">未选择任何文件</div>
								<!-- <input style="display: inline;" class="btn btn-white"
									type="file" id="comlainfile" name="file" accept=".xls,.xlsx"
									multiple="multiple"> -->
								<!-- <input style="display: inline;" class="btn btn-success type="reset" value="重选"> -->
								<my:btn type="import" onclick="import_Excel()"></my:btn>
								<!-- <input style="display: inline;" class="btn btn-success" type="submit" value="导入"> -->
							</form>
							<!-- <button id="setUsed"  class="btn btn-success" onclick="import_Excel()">导入</button> -->
							<my:btn type="export" onclick="export_Excel()"></my:btn>
							<!-- loading -->
							<div class="btn loading" id="import_load" style="display: none;">
								<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在导入...</span>
							</div>
							<!-- loading -->
						</div>
						<div>
							<div id="table_list"></div>
							<div id="toolbar"></div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${context}/js/cell/cellinfo.js"></script>
</body>
</html>
