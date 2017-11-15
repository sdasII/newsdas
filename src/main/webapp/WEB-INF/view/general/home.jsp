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

.nav a,h5{font-size: 16px !important}
.title{
	text-align: center;
    height: 30px;
    padding-bottom: 7px;
    padding-top: 2px;
	color: #fff;
    background-color: #1c84c6;
    }
    .tab-pane{height: 300px;}
    .panel-body{height:100%;overflow: auto}
    .feed-element{padding: 5px}
#leftmodel {
	width: 320px !important;
	padding: 0 !important;
	border: 2px solid #ccc;
}

#rightmodel {
	height:558px;
	width: 70% !important;
	padding: 0 !important;
	overflow: auto;
	border: 2px solid #ccc;
}
</style>
</head>
<body>
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="col-sm-12">
				<div class="title">
					<h5>最新发布更新时间：</h5>
				</div>
				<div class="">

					<div class="col-sm-6" id="leftmodel">
						<div class="ibox-content">
							<div class="feed-activity-list"
								style="height: 100%; width: 300px; float: left">
								<div class="feed-element">
									<h5>最新发布更新时间：</h5>
								</div>
								<div class="feed-element">
									<h5>共有n个小区被监控：</h5>
									<div class="media-body ">
										<small>其中处于事件状态n个</small><br> <small>其中处于事件状态n个</small><br>
										<small>其中处于事件状态n个</small><br> <small>其中处于事件状态n个</small><br>
									</div>
								</div>
								<div class="feed-element" style="border-bottom: 0px">
									<div class="media-body ">
										<div class="tabs-container">
											<ul class="nav nav-tabs">
												<li class=""><a data-toggle="tab" href="#tab-1"
													aria-expanded="false">事件</a></li>
												<li class="active"><a data-toggle="tab" href="#tab-2"
													aria-expanded="true">临界</a></li>
													<li class=""><a data-toggle="tab" href="#tab-3"
													aria-expanded="false">健康</a></li>
											</ul>
											<div class="tab-content">
												<div id="tab-1" class="tab-pane">
													<div class="panel-body">
														<ol>
															<li><a>东风小区</a></li>
															<li><a>东风小区</a></li>
															<li><a>东风小区</a></li>
															<li><a>东风小区</a></li>
															<li>……</li>
														</ol>
													</div>
												</div>
												<div id="tab-2" class="tab-pane active">
													<div class="panel-body">
														<ol>
															<li><a>解放小区</a></li>
															<li><a>解放小区</a></li>
															<li><a>解放小区</a></li>
															<li><a>解放小区</a></li>
															<li>……</li>
														</ol>
													</div>
												</div>
												<div id="tab-3" class="tab-pane">
													<div class="panel-body">
														<ol>
															<li><a>中山小区</a></li>
															<li><a>中山小区</a></li>
															<li><a>中山小区</a></li>
															<li><a>中山小区</a></li>
															<li>……</li>
														</ol>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-sm-6" id="rightmodel">
						<div class="feed-activity-list" id="allmap"
							style="text-align: center; height: 550px; width: 100%; float: left"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var ctx=ctx;
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