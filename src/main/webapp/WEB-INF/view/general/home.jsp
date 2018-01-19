<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<%@ include file="/include/common.jsp"%>
<link href="${context}/style/loader.css" rel="stylesheet" type="text/css">
<!-- <script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=EmXf0NLcNCvBO5hdDliGtvC9D5v6GA5K"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script> -->
<link href="${context}/lib/map/css/baidu_map_v2.css" rel="stylesheet" type="text/css">	
<style type="text/css">
.nav a, h5 {
	font-size: 16px !important
}

.title {
	text-align: center;
	height: 30px;
	padding-bottom: 7px;
	padding-top: 2px;
	color: #fff;
	background-color: #1c84c6;
}

.tab-pane {
	height: 500px;
}

.panel-body {
	height: 100%;
	overflow: auto
}

.feed-element {
	padding: 5px
}

#leftmodel {
	height: 800px;
	padding: 0px;
	margin: 0px;
	border: 1px solid #ccc;
}

#rightmodel {
	height: 800px;
	padding: 0px;
	margin: 0px;
	border: 1px solid #ccc;
}
.media-body h5{font-weight: normal;}
.media-body small{
	font-weight: bold;
    font-size: 16px;
    padding: 0px 5px;
}
.anchorBL{display: none}
#searchBtn{
	color: white;
	font-size: 16px;
	text-decoration: underline;
	margin-right: 20px;
}
</style>
</head>
<body>
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row" style="margin: 0px; padding: 0px;">
			<div class="col-sm-12">
				<div class="title">
					<h5 class="updatetime">最新发布更新时间：</h5>
				</div>
				<div class="ibox-tools" style="margin-top: -25px;">
				<a href="javascript:;" onclick="changeHeatView(this)" id="searchBtn">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>查看热力图</a>
				</div>
			</div>
			<div class="col-sm-12">
				<div id="leftmodel" class="col-sm-3">
					<div class="ibox-content">
						<div class="feed-activity-list"
							style="width: 100%; height: 100%; float: left; margin: 0px;">
							<!-- <div class="feed-element">
								<h5 class="updatetime">最新发布更新时间：</h5>
							</div> -->
							<div class="feed-element">
								<h5 id="all">共有n个小区被监控：</h5>
								<div class="media-body" style="margin-left: 20px;margin-bottom: 10px;">
									<h5>事件状态<small id="event" style="color: red"></small>个</h5>
									<h5>亚健康状态<small id="critical" style="color: orange"></small>个</h5>
									<h5>健康状态<small id="health" style="color: #25CB73"></small>个</h5>
									<h5>无计算数据<small id="other"></small>个</h5>
								</div>
							</div>
							<div class="feed-element" style="border-bottom: 0px">
							<div class="form-group">
							<label>状态:</label> 
							<select id="status" name="status" class="btn btn-white" style="width: 180px;margin-left: 20px">
								<option value="">全部</option>
								<option value="2">健康</option>
								<option value="1">亚健康</option>
								<option value="0">事件</option>
								<option value="10">无计算数据</option>
							</select>
							<!-- <button id="search_btn" type="button" class="btn btn-primary" onclick="search()">确定</button> -->
						</div>
						<div class="ibox-content" style="height:500px;overflow: auto">
							<ul class="list-group" id="content">
							</ul>
						</div>
								<!-- <div class="media-body ">
									<div class="tabs-container">
										<ul class="nav nav-tabs">
											<li class="" onclick="switchTab(0)"><a data-toggle="tab"
												href="#tab-1" aria-expanded="true">事件</a></li>
											<li class="active" onclick="switchTab(1)"><a
												data-toggle="tab" href="#tab-2" aria-expanded="false">亚健康</a></li>
											<li class="" onclick="switchTab(2)"><a data-toggle="tab"
												href="#tab-3" aria-expanded="false">健康</a></li>
										</ul>
										<div class="tab-content">
											<div id="tab-1" class="tab-pane">
												<div class="panel-body">
													<ul class="list-group" id="event_content">
													</ul>
												</div>
											</div>
											<div id="tab-2" class="tab-pane active">
												<div class="panel-body">
													<ul class="list-group" id="critical_content">
													</ul>
												</div>
											</div>
											<div id="tab-3" class="tab-pane">
												<div class="panel-body">
													
												</div>
											</div>
										</div>
									</div>
								</div> -->
							</div>
						</div>
					</div>
				</div>
				<div id="rightmodel" class="col-sm-9">
				<div style="left:0;top:0;width:100%;height:100%;position:absolute;" id="container"></div>
					<!-- <div class="feed-activity-list" id="allmap"
						style="text-align: center; width: 100%; height: 100%; float: left"></div> -->
						
				</div>
			</div>

		</div>
	</div>
	<script type="text/javascript" src="${context}/lib/map/baidumap_offline_load.js"></script>
	<script type="text/javascript" src="${context}/lib/map/tools/Heatmap_min.js"></script>
	<script type="text/javascript" src="${context}/js/general/heatMap.js"></script>
	<script type="text/javascript">
		var ctx = ctx;
		// 百度地图API功能
		var map = new BMap.Map("container");
		var point = new BMap.Point(113.304979, 23.186708);
		map.centerAndZoom(point, 13); // 初始化地图,设置中心点坐标和地图级别
		//map.addControl(new BMap.MapTypeControl());//地图卫星三维模式切换
		map.setCurrentCity("广州");
		//map.addControl(new BMap.NavigationControl({offset: new BMap.Size(10, 90)}));
		map.enableScrollWheelZoom();                  // 启用滚轮放大缩小。
		//map.disableDoubleClickZoom();
		var marker = new BMap.Marker(point);// 创建标注
		map.addOverlay(marker);// 加载标注
	</script>
	<script type="text/javascript" src="${context}/js/general/home.js"></script>
</body>
</html>