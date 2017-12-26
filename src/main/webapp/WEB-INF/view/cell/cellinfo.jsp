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
<link href="${context}/lib/hplus/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="${context}/lib/sweetAlert/sweetalert.min.js"></script>
<link href="${context}/lib/sweetAlert/sweetalert.css" rel="stylesheet">
<style type="text/css">
#name {
    margin: -10px;
    width: 180px;
    height: 35px;
    margin-right: 10px;
    margin-left: 10px;
    border-radius: 3px;
    border: 1px solid #e7eaec;
}
</style>
</head>

<body style="margin-left: 5px;margin-right: 5px;">
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row" style="text-align: center;">
			<h3><span id="updateTime"></span>&nbsp;&nbsp;&nbsp;<span id="counts"></span></h3>
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
							<label for="type">状态筛选(是否在用)：</label>
							<select name="type" id="type" class="btn btn-white">
                                    <option value="">全部</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                             </select>
								
							<button style="margin-left: 5px;" class="btn btn-success"
								onclick="searchInfo();">查询</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<button id="setUsed"  class="btn btn-success" onclick="setUsed()">设为使用状态</button>
							<button id="clear" style="margin-left: 5px;" class="btn btn-white" onclick="clearUsed()">取消使用</button>
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
