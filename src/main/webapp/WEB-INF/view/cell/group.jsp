<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/include/common.jsp"%>
<%-- <script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script> --%>
<script src="${context}/lib/datapicker/bootstrap-datetimepicker.min.js"></script>
<script src="${context}/lib/datapicker/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="${context}/style/loader.css" rel="stylesheet" type="text/css">
<link href="${context}/lib/hplus/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
<link href="${context}/lib/datapicker/bootstrap-datetimepicker.min.css" rel="stylesheet">
<style type="text/css">
[class^="icon-"], [class*=" icon-"] {
    display: inline-block;
    width: 14px;
    height: 14px;
    margin-top: 1px;
    line-height: 14px;
    vertical-align: text-top;
    background-image: url(${context}/lib/datapicker/glyphicons-halflings.png);
    background-position: 14px 14px;
    background-repeat: no-repeat;
}
.icon-arrow-right {
    background-position: -264px -96px;
}
.icon-arrow-left {
    background-position: -240px -96px;
}
.form_datetime{
	margin: -10px;
	width: 125px; 
	height: 35px;
	border-radius:3px; 
	margin-right: 10px;
	margin-left: 10px;
	border: 1px solid #e7eaec;
	font-weight: normal;
}
</style>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="col-sm-16">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>小区列表</h5>
						<div class="ibox-tools"></div>
					</div>
					<div class="ibox-content">
						<div class="form-group">
							<label for="name" style="margin-left: 20px">小区名称<input type="text" style="margin: -10px;width: 225px; height: 35px; margin-right: 10px;margin-left: 10px"
								class="btn btn-white" placeholder="请输入小区名称" id="name" name="name"></label>		
							<button style="margin-left: 5px;" class="btn btn-success"
								onclick="javascript:select()">查询</button>
							<button id="clear" type="reset" class="btn btn-white">清空</button>
							<label for="time" style="margin-left: 20px">数据导出
							<input size="16" type="text" id="exporttime" placeholder="请选择导出月份" readonly class="form_datetime">
							<!-- <input id="exporttime" name="time"style="margin: -10px;width: 125px; height: 35px; margin-right: 10px;margin-left: 10px"
								class="btn btn-white layer-date" placeholder="请选择导出月份"
								onclick="laydate({istime: false, format: 'YYYYMM'})"> -->
							</label>		
							<button style="margin-left: 5px;" class="btn btn-success"
								onclick="javascript:exportExcel()">历史健康度导出</button>
							<div class="btn loading" id="load1" style="display: none">
								<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif" style="height: 20px;"><span>正在导出...</span>
							</div>
							<label for="time" style="margin-left: 20px">
							<input size="16" type="text"  id="resultexporttime" placeholder="请选择导出月份" readonly class="form_datetime">
							<!-- <input id="resultexporttime" name="time"style="margin: -10px;width: 125px; height: 35px; margin-right: 10px;margin-left: 10px"
								class="btn btn-white layer-date" placeholder="请选择导出月份"
								onclick="laydate({istime: false, format: 'YYYYMM'})"> -->
							</label>		
							<button style="margin-left: 5px;" class="btn btn-success"
								onclick="javascript:resultexportExcel()">健康判断结果导出</button>
							<div class="btn loading" id="load2" style="display: none">
								<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif" style="height: 20px;"><span>正在导出...</span>
							</div>
						</div>
						<div>
							<table id="table_list_1"></table>
						</div>	
					</div>

				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${context}/js/cell/group.js"></script>
</body>
</html>
