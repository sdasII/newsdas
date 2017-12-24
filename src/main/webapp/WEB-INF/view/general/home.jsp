<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<%@ include file="/include/common.jsp"%>
<link href="${context}/style/loader.css" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=EmXf0NLcNCvBO5hdDliGtvC9D5v6GA5K"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script>
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
</style>
</head>
<body>
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row" style="margin: 0px; padding: 0px;">
			<div class="col-sm-12">
				<div class="title">
					<h5 class="updatetime">最新发布更新时间：</h5>
				</div>
			</div>
			<div class="col-sm-12">
				<div id="leftmodel" class="col-sm-3">
					<div class="ibox-content">
						<div class="feed-activity-list"
							style="width: 100%; height: 100%; float: left; margin: 0px;">
							<div class="feed-element">
								<h5 class="updatetime">最新发布更新时间：</h5>
							</div>
							<div class="feed-element">
								<h5 id="all">共有n个小区被监控：</h5>
								<div class="media-body ">
									<small>其中处于事件状态<small id="event"></small>个
									</small><br> <small>其中处于亚健康状态<small id="critical"></small>个
									</small><br> <small>其中处于健康状态<small id="health"></small>个
									</small><br>
								</div>
							</div>
							<div class="feed-element" style="border-bottom: 0px">
								<div class="media-body ">
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
													<ul class="list-group" id="health_content">
													</ul>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="rightmodel" class="col-sm-9">
					<div class="feed-activity-list" id="allmap"
						style="text-align: center; width: 100%; height: 100%; float: left"></div>

				</div>
			</div>

		</div>
	</div>
	<script type="text/javascript">
		var ctx = ctx;
		// 百度地图API功能
		var map = new BMap.Map("allmap"); // 创建Map实例
		map.centerAndZoom(new BMap.Point(113.304979, 23.186708), 12); // 初始化地图,设置中心点坐标和地图级别
		map.addControl(new BMap.MapTypeControl()); //添加地图类型控件
		map.setCurrentCity("广州"); // 设置地图显示的城市 此项是必须设置的
		map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
	</script>
	<script type="text/javascript" src="${context}/js/general/heatMap.js"></script>
	<script type="text/javascript" src="${context}/js/home.js"></script>
</body>
</html>