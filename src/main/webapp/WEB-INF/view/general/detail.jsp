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
<title>详细信息</title>
<%@ include file="/include/common.jsp"%>
<%-- <script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script> --%>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=EmXf0NLcNCvBO5hdDliGtvC9D5v6GA5K"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script>
<link href="${context}/style/loader.css" rel="stylesheet"
	type="text/css">
<link
	href="${context}/lib/hplus/css/plugins/dataTables/dataTables.bootstrap.css"
	rel="stylesheet">
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
	min-height: 310px;
	background-color: #777;
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
		var cellname = '${cellname}';
		var stationname = '${stationname}';
		var coverscene = '${coverscene}';
		var usedband = '${usedband}';
	</script>
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row" style="text-align: center;">
			<h1 style="margin: 0 auto;">
				<b>${cellname}</b>小区健康详情查看
			</h1>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>详细信息</h5>
						<div class="ibox-tools">
							<div class="btn-group">
								<button class="btn btn-info" id="workinweek" type="button"
									onclick="javascript:workoneweek()">一周</button>
								<button class="btn btn-white" id="workinmonth" type="button"
									onclick="javascript:workonemonth()">一月</button>
								<button class="btn btn-white" id="workinselect" type="button"
									onclick="javascript:worktimeselect()">按时间选择</button>
								<div id="worktimeselect" style="display: none;">
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
										onclick="javascript:query2()">确定</button>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<!-- loading -->
						<div class="loading_bk" id="ratiotrend_loadbk"></div>
						<div class="loading" id="ratiotrend_load">
							<img
								src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
						</div>
						<!-- loading -->
						<div class="tabs-container">
						<ul class="nav nav-tabs">
							<li onclick="switch()" class="active"><a data-toggle="tab"
								href="#tab-1" aria-expanded="true">健康指标集</a></li>
							<li onclick="switch()" class=""><a data-toggle="tab"
								href="#tab-2" aria-expanded="false">专家指标集</a></li>
						</ul>
					</div>
					<div class="tab-content">
						<div id="tab-1" class="tab-pane active">
							<div class="panel-body">
								<div id="rtratio" style="height: 300px;"></div>
							</div>
						</div>
						<div id="tab-2" class="tab-pane">
							<div class="panel-body">
								<div id="ratiotrend" style="height: 300px;"></div>
							</div>
						</div>
					</div>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>小区基本情况</h5>
					</div>
					<div class="ibox-content">
						<div style="height: 350px;">
							<div id="allmap"
								style="text-align: center; height: 85%; width: 100%"></div>
							<table style="text-align: center; margin: 0 auto;">
								<tr>
									<td><span style="text-align: center;"><h4>小区名称：</h4></span></td>
									<td><span style="text-align: center;"><h5>${cellname}</h5></span></td>
									<td><span style="text-align: center;"><h4>&nbsp;&nbsp;覆盖场景：</h4></span></td>
									<td><span style="text-align: center;"><h5>${coverscene}</h5></span></td>
								</tr>
								<tr>
									<td><span style="text-align: center;"><h4>基站名称：</h4></span></td>
									<td><span style="text-align: center;"><h5>${stationname}</h5></span></td>
									<td><span style="text-align: center;"><h4>&nbsp;&nbsp;用户频段：</h4></span></td>
									<td><span style="text-align: center;"><h5>${usedband}</h5></span></td>

								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox-title">
					<h5>当前时段风险预警</h5>
				</div>
				<div class="ibox-content">
					<!-- loading -->
					<div class="loading_bk" id="alarm_loadbk"></div>
					<div class="loading" style="margin-top: 20%" id="alarm_load">
						<img
							src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
					</div>
					<!-- loading -->
					<div class="jqGrid_wrapper"
						style="margin: 0; padding: 0; width: 100%; overflow: auto;">
						<table class="table" id="alarm_table"></table>
						<div id="pager_alarm_table"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var indexurl = ctx;
		// 百度地图API功能
		var map = new BMap.Map("allmap"); // 创建Map实例
		map.centerAndZoom(new BMap.Point(113.270856, 23.137463), 15); // 初始化地图,设置中心点坐标和地图级别
		map.addControl(new BMap.MapTypeControl()); //添加地图类型控件
		map.setCurrentCity("广州"); // 设置地图显示的城市 此项是必须设置的
		map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
		var marker = new BMap.Marker(new BMap.Point(113.270856, 23.137463));
		map.addOverlay(marker);
	</script>
	<script type="text/javascript" src="${context}/js/general/heatMap.js"></script>
	<script type="text/javascript" src="${context}/js/general/detail.js"></script>
</body>
</html>