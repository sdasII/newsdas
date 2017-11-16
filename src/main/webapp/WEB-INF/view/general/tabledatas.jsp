<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>小区综合页面</title>
<%@ include file="/include/common.jsp"%>
<link href="${context}/style/loader.css" rel="stylesheet" type="text/css">
<link href="${context}/lib/hplus/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
<style type="text/css">
td {
	margin-left: 10px;
	margin-right: 10px
}

input {
	padding-top: 5px;
	padding-bottom: 5px;
	margin-top: 0px;
	margin-bottom: 0px
}

.loading_bk {
	display: none;
	height: 80%;
	width: 95%;
	min-height: 310px; background-color : #777;
	position: absolute;
	z-index: 999;
	opacity: 0.6;
	text-align: center;
	background-color: #777;
}

.loading {
	display: none;
	color: #fff;
	margin-left: 40%;
	margin-top: 10%;
	position: absolute;
	z-index: 9999;
	text-align: center;
}

.loading span {
	font-size: 16px;
	margin-left: 10px;
}

.loading img {
	height: 30px
}

#table_list_healthtable th, #table_list_healthtable td {
	border: 1px solid #ddd
}

.ibox-content .gray {
	background-color: gray
}

.ibox-content .red {
	background-color: red
}

.ibox-content .yellow {
	background-color: yellow
}
</style>
</head>
<body>

	<script type="text/javascript">
		
	</script>
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${cellname}小区健康判断结果</h5>
						<div class="ibox-tools">
							<button class="btn btn-info search" type="button" onclick="backDetail('${cellname}')" style="float: left;margin-left: 20px;">返回</button>
						</div>
					</div>
					<div class="ibox-content">
						<!-- loading -->
						<div class="loading_bk" id="healthtable_loadbk"></div>
						<div class="loading" id="healthtable_load">
							<img
								src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
						</div>
						<!-- loading -->
						<div class="table" style="margin-left: 20px; width: 80%;">
							<table id="table_list_healthtable" class="table"
								style="width: 100%"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	var ctx=ctx;
	var cellname = '${cellname}';
	</script>
	<script type="text/javascript" src="${context}/js/general/tabledatas.js"></script>
</body>
</html>