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
<script type="text/javascript" src="${context}/lib/map/baidumap_offline_load.js"></script>
<link href="${context}/lib/map/css/baidu_map_v2.css" rel="stylesheet" type="text/css">
<!-- <script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=EmXf0NLcNCvBO5hdDliGtvC9D5v6GA5K"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script> -->
<script src="${context}/lib/datapicker/bootstrap-datetimepicker.js"></script>
<script
	src="${context}/lib/datapicker/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="${context}/lib/datapicker/bootstrap-datetimepicker.min.css"
	rel="stylesheet">
<script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script>
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
	/* display: none; */
	height: 85%;
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
	/* display: none; */
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

/* #alarm .pull-left {
	display: none
} */

.loading_bk{
	/* display:none; */
    height: 80%;
    width: 100%;
    min-height:310px;
    background-color: #777;
    position: absolute;
    z-index: 999;
    opacity: 0.6;
    text-align: center;
    }
.loading{
	/* display:none; */
	color:#fff;
    margin-left: 40%;
    margin-top: 10%;
    position: absolute;
    z-index: 9999;
    text-align: center;
    }
.loading span{font-size: 16px; margin-left: 10px;}
.loading img{height:30px}
.anchorBL{display: none}
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
			<!-- <button class="btn btn-info search" type="button" onclick="back()" style="float: left;margin-left: 20px;">返回</button> -->
			<h1 style="margin: 0 auto; margin-bottom: 10px">
				<b>${cellname}</b>小区健康详情
			</h1>
			<h3>
				<span id="updateTime">最新发布时间</span>&nbsp;&nbsp;&nbsp;<span
					id="h_ratio"></span>
			</h3>
			<div class="ibox-tools" style="margin-bottom: 10px">
				<div class="btn-group">
					<button class="btn btn-info datePicker" id="workday" type="button">最近一日</button>
					<button class="btn btn-white datePicker" id="workinweek"
						type="button">周</button>
					<button class="btn btn-white datePicker" id="workinmonth"
						type="button">月</button>
					<button class="btn btn-white datePicker" type="button">按时间选择</button>
					<div id="timeselect" style="display: none; float: left;">
						<input style="margin-left: 5px; margin-top: -7px !important;"
							id="starttime" class="layer-date starttime" placeholder="请输入开始时间"
							onclick="laydate({istime: false, format: 'YYYYMMDD'})"> <span
							id="span" style="margin-top: -10px; display: inline !important;"
							class="input-group-addon">到</span> <input
							style="margin-top: -7px !important;" class="layer-date endtime"
							id="endtime" placeholder="请输入结束时间"
							onclick="laydate({istime: false, format: 'YYYYMMDD'})">

						<button class="btn btn-info search" type="button"
							onclick="global_page_query()">确定</button>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-6">
				<div class="ibox-title">
					<h5>健康预警</h5>
				</div>
				<div class="ibox-content">
					<div class="jqGrid_wrapper" id="alarm"
						style="margin: 0; padding: 0; width: 100%; overflow: auto;">
						<!-- loading -->
							<div class="loading_bk" id="table_loadbk"></div>
							<div class="loading" id="table_load">
								<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
							</div>
						<!-- loading -->
						<table class="table" id="alarm_table"></table>
						<div id="pager_alarm_table"></div>
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
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>详细信息</h5>
					</div>
					<div class="ibox-content" style="overflow: auto">
						<!-- loading -->
						<div class="loading_bk" id="ratiotrend_loadbk"></div>
						<div class="loading" id="ratiotrend_load">
							<img
								src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
						</div>
						<!-- loading -->
						<div class="tabs-container">
							<ul class="nav nav-tabs" id="topTabs">
								<li onclick="switchTab('#rtratio','健康诊断结果','rgb(46,199,201)')"
									class="active"><a data-toggle="tab" href="#tab-1"
									aria-expanded="true">健康诊断结果</a></li>
								<!-- <li onclick="switchTab('#ratiotrend','专家指标集','#1c84c6')"
									class=""><a data-toggle="tab" aria-expanded="false">专家指标集</a> href="#tab-2"</li> -->
							</ul>
						</div>
						<div class="tab-content">
							<div id="tab-1" class="tab-pane active">
								<div class="panel-body">
									<div id="rtratio" style="height: 350px; margin-bottom: 30px"></div>
									<div id="historyCharts" style="height: 300px;"></div>
								</div>
							</div>
							<div id="tab-2" class="tab-pane">
								<div class="panel-body">
									<div id="ratiotrend" style="width: 1250px; height: 350px;"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="ibox-title">
				<h5>工单信息</h5>
			</div>
			<div class="ibox-content">
				<div class="col-sm-12">
					<div class="tabs-container">
						<ul class="nav nav-tabs">
							<li onclick="searchCapacityInfo()" class="active"><a
								data-toggle="tab" href="#tab-3" aria-expanded="true">性能工单</a></li>
							<li onclick="searchComplaintInfo()" class=""><a
								data-toggle="tab" href="#tab-4" aria-expanded="false">投诉信息</a></li>
						</ul>
						<div class="tab-content">
							<div id="tab-3" class="tab-pane active">
								<div class="panel-body" style="min-height: 150px;">
									<div class="jqGrid_wrapper">
										<table class="table" id="table_list_work"></table>
										<div id="pager_list_work"></div>
									</div>
								</div>
							</div>
							<div id="tab-4" class="tab-pane">
								<div class="panel-body" style="min-height: 150px;">
									<table class="table" id="table_list_work2"></table>
									<div id="pager_list_work2"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="ibox-title">
				<h5>指标分析</h5>
				<div class="ibox-tools">
					<label for="time" style="margin-left: 20px">指标查询 <input
						size="16" type="text" id="time" placeholder="请选择月份" readonly class="form_datetime">
					</label>
					<my:btn type="search" onclick="cellindex_search()"></my:btn>
				</div>
			</div>
			<div class="ibox-content">
				<div class="col-sm-12">
					<div class="tabs-container">
						<ul id="group_index" class="nav nav-tabs">
						</ul>
						<!-- loading -->
						<div class="loading_bk" id="tab2_loadbk"></div>
						<div class="loading" id="tab2_load">
							<img
								src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
						</div>
						<!-- loading -->
						<div class="tab-content">
							<div class="tab-pane active">
								<div class="panel-body">
									<div id="mb" style="height: 350px"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var ctx = ctx;
		var cell_code = "${cellname}";
		var context = "${context}";
		// 百度地图API功能
		var map = new BMap.Map("allmap"); // 创建Map实例
		map.centerAndZoom(new BMap.Point(113.270856, 23.137463), 12); // 初始化地图,设置中心点坐标和地图级别
		map.setCurrentCity("广州"); // 设置地图显示的城市 此项是必须设置的
		map.disableDoubleClickZoom();
	</script>
	<script type="text/javascript" src="${context}/js/general/heatMap.js"></script>
	<!-- 指标模型 -->
	<script type="text/javascript" src="${context}/js/cell/index_model.js"></script>
	<script type="text/javascript" src="${context}/js/cell/detail.js"></script>
</body>
</html>