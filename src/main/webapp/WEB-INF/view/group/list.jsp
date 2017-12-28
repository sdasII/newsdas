<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/include/common.jsp"%>
<%-- <script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script> --%>
<script src="${context}/lib/datapicker/bootstrap-datetimepicker.js"></script>
<script
	src="${context}/lib/datapicker/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script>
<link href="${context}/style/loader.css" rel="stylesheet"
	type="text/css">
<link
	href="${context}/lib/hplus/css/plugins/dataTables/dataTables.bootstrap.css"
	rel="stylesheet">
<link href="${context}/lib/datapicker/bootstrap-datetimepicker.min.css"
	rel="stylesheet">
<style type="text/css">
[class^="icon-"], [class*=" icon-"] {
	display: inline-block;
	width: 14px;
	height: 14px;
	margin-top: 1px;
	line-height: 14px;
	vertical-align: text-top;
	background-image:
		url(${context}/lib/datapicker/glyphicons-halflings.png);
	background-position: 14px 14px;
	background-repeat: no-repeat;
}

.icon-arrow-right {
	background-position: -264px -96px;
}

.icon-arrow-left {
	background-position: -240px -96px;
}

.form_datetime {
	margin: -10px;
	width: 125px;
	height: 35px;
	border-radius: 3px;
	margin-right: 10px;
	margin-left: 10px;
	border: 1px solid #e7eaec;
	font-weight: normal;
}
#name{margin: -10px; width: 130px; height: 35px; margin-right: 10px; margin-left: 10px;border-radius: 3px;border: 1px solid #e7eaec;}
.form-group select{width: 130px; height: 35px; margin-right: 10px; margin-left: 10px;}
.pull-left{display: none}
</style>
</head>

<body style="margin-left: 5px;margin-right: 5px;">
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row" style="text-align: center;">
			<h3><span id="updateTime">最新发布时间</span></h3>
		</div>
		<div class="row">
			<div class="col-sm-16">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>健康评估</h5>
						<div class="ibox-tools">
							<div class="btn-group">
								<button class="btn btn-info" id="searchinday" type="button"
									onclick="javascript:searchoneday(this)">最近一日</button>
								<button class="btn btn-white" id="searchinweek" type="button"
									onclick="javascript:searchoneweek(this)">周</button>
								<button class="btn btn-white" id="searchinmonth" type="button"
									onclick="javascript:searchonemonth(this)">月</button>
								<button class="btn btn-white" id="searchinselect" type="button"
									onclick="javascript:searchtimeselect(this)">按时间选择</button>
								<div id="searchimeselect" style="display: none;">
									<input id="start"
										style="margin-left: 5px; margin-top: -7px !important;"
										class="layer-date" placeholder="请输入开始时间"
										onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
									<span id="span"
										style="margin-top: -10px; display: inline !important;"
										class="input-group-addon">到</span> <input id="end"
										style="margin-top: -7px !important;" class="layer-date"
										placeholder="请输入结束时间"
										onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
									<button class="btn btn-info" type="button"
										onclick="javascript:query()">确定</button>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<div class="form-group">
							<label for="name" style="margin-left: 20px">小区名称</label>
							<input type="text"  placeholder="请输入小区名称" id="name"
								name="name">
							<label>状态</label> 
							<select id="status" name="status" class="btn btn-white">
								<option value="-1">全部</option>
								<option value="2">健康</option>
								<option value="1">亚健康</option>
								<option value="0">事件</option>
								<option value="3">计算无结果</option>
							</select>
							<my:btn type="search" onclick="select()"></my:btn>
							<button style="margin-left: 5px;float: right" onclick="javascript:exportExcel()">
								<img src="${context}/style/export.png" title="历史健康度导出" style="height:20px;"/>
							</button>
							<div class="btn loading" id="load1" style="display: none;float: right">
								<img 
									src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"
									style="height: 20px;"><span>正在导出...</span>
							</div>
								<br><br>
							<!-- <label for="time" style="margin-left: 20px">数据导出 <input
								size="16" type="text" id="exporttime" placeholder="请选择导出月份"
								readonly class="form_datetime"> 
							</label> -->
							<label style="margin-left: 20px">导出类型</label> 
							<select id="type" name="type" class="btn btn-white">
								<option value="hour">按小时</option>
								<option value="days">按天</option>
							</select>
							<!-- <label for="time" style="margin-left: 20px"> <input
								size="16" type="text" id="resultexporttime"
								placeholder="请选择导出月份" readonly class="form_datetime">
							</label> -->
							<button style="margin-left: 5px;" class="btn btn-success" title="健康判断结果导出"
								onclick="javascript:resultexportExcel()">导出</button>
							<div class="btn loading" id="load2" style="display: none">
								<img
									src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"
									style="height: 20px;"><span>正在导出...</span>
							</div>
						</div>
						<div>
							<table id="table_list_1"></table>
							<div id="toolbar"></div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${context}/js/group/list.js"></script>
</body>
</html>
