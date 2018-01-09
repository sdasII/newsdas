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
.form-group select{width: 130px; height: 35px; margin-right: 10px; margin-left: 10px;}
/* .pull-left{display: none} */

input::-webkit-input-placeholder { /* WebKit browsers */ 
margin-left: 10px;
} 
input:-moz-placeholder { /* Mozilla Firefox 4 to 18 */ 
margin-left: 10px;
} 
input::-moz-placeholder { /* Mozilla Firefox 19+ */ 
margin-left: 10px;
} 
input:-ms-input-placeholder { /* Internet Explorer 10+ */ 
margin-left: 10px;
} 
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
										style="margin-left: 5px;" class="layer-date" placeholder="请输入开始时间"
										onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
									<input id="span" type="text" style="width:35px; display: inline !important;padding: 8px" value="到"
										class="btn-white"/> <input id="end" class="layer-date"
										placeholder="请输入结束时间"
										onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
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
							<my:btn type="search" onclick="globalSelect()"></my:btn>
							<button style="margin-left: 5px;float: right;background: transparent;" onclick="javascript:exportExcel()">
								<img src="${context}/style/export.png" title="历史健康度导出" style="height:20px;"/>
							</button>
							<div class="btn loading" id="load1" style="display: none;float: right">
								<img 
									src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"
									style="height: 20px;"><span>正在导出...</span>
							</div>
							<label style="margin-left: 20px">导出类型</label> 
							<select id="type" name="type" class="btn btn-white">
								<option value="hour">按小时</option>
								<option value="days">按天</option>
							</select>
							<my:btn type="export" onclick="javascript:resultexportExcel()"></my:btn>
							<div class="btn loading" id="load2" style="display: none">
								<img
									src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"
									style="height: 20px;"><span>正在导出...</span>
							</div>
						</div>
						<div>
							<!-- loading -->
							<div class="loading_bk" id="table_loadbk"></div>
							<div class="loading" id="table_load">
								<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
							</div>
							<!-- loading -->
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
