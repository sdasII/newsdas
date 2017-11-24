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
							<label for="type">状态筛选(是否在用)：
							<select name="type" id="type" class="form-control">
                                    <option value="">全部</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                             </select>
							</label>	
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
