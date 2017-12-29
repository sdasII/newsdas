<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/include/common.jsp"%>
<script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript"
	src="${context}/js/plugin/jQuery.sdas.core.js"></script>
<script src="${context}/lib/datapicker/bootstrap-datetimepicker.js"></script>
<script
	src="${context}/lib/datapicker/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="${context}/lib/datapicker/bootstrap-datetimepicker.min.css"
	rel="stylesheet">
<style type="text/css">
.btn-circle {
	width: 25px;
	height: 25px;
	padding: 0px;
	margin-right: 5px;
	vertical-align: middle;
	float: right
}

#fileList {
	list-style: none;
	padding-left: 0px;
}

#fileList span {
	font-size: 15px;
	margin-top: -2px;
}

#fileList li {
	height: 40px;
	padding: 3px;
	border: 1px solid #ccc;
}

#fileList li button {
	float: right;
	margin-right: 5px;
	margin-top: -28px
}

#fileList input {
	margin-top: 5px;
}

.error_msg {
	display: none;
	color: red
}

.loading img {
	height: 20px;
	margin-right: 5px
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

.panel-body .ibox-tools {
	text-decoration: underline;
}

.panel-body .ibox-tools i {
	color: #337ab7;
	font-style: normal;
}

input[name="time"] {
	width: 160px;
	margin: -10px;
	height: 39px;
	display: inline;
	padding: -10px;
	margin-right: 10px;
	margin-left: 0px;
}

input[type="file"] {
	display: inline;
	opacity: 0;
	position: relative;
	width: 165px;
	z-index: 9
}

.upload_btn {
	width: 160px;
	margin-left: -160px;
	display: inline;
}

.upload_title {
	display: inline;
	margin-top: 10px;
}
</style>
</head>
<body>
	<script type="text/javascript">
		var status = 'unkown';
		if (status == 'success') {
			showOnlyMessage(INFO, "上传成功");
		} else if (status.indexOf("fail") >= 0) {
			showOnlyMessage(ERROR, status);
		}
	</script>
	<script type="text/javascript">
		var ws;
		if ('WebSocket' in window) {
			ws = new WebSocket("ws://49.4.6.146:9999/newsdas/websocket");
			//ws = new WebSocket("ws://localhost:8080/newsdas/websocket");
		} else {
			alert("当前浏览器不支持WebSocket");
		}
		//连接发生错误的回调方法
		ws.onerror = function() {
			//alert("WebSocket连接发生错误");
			//showOnlyMessage(ERROR, "WebSocket连接发生错误");
		};

		//连接成功建立的回调方法
		ws.onopen = function() {
			//alert("WebSocket连接成功");
			showOnlyMessage(INFO, "WebSocket连接成功");
		}

		//接收到消息的回调方法
		ws.onmessage = function(event) {
			var progress = event.data;
			$("#progress2").attr("value", progress);
			var value = progress + "%";
			$("#progressvalue2").text(value);
		}

		//连接关闭的回调方法
		ws.onclose = function() {
			//alert("WebSocket连接关闭");
			//showOnlyMessage(INFO, "WebSocket连接关闭");
		}

		// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
		ws.onbeforeunload = function() {
			closews();
		}
	</script>
	<script type="text/javascript">
		var select = $("#originfile").val();
		var time = $("#origintime").val();			
		
		function closews() {
			ws.close();
		}
		
		$("#soucefile").ajaxForm({
			beforeSend : function(formData, jqForm, options) {
				var percentVal = '0%';
				if (select != "" && time != "") {
					$("#originsubmit").attr("disabled", true);
					$("#progress2").attr("value", 0);
					$("#progressvalue2").text("0%");
					ws.send("start");
					var progress = "正在上传" + percentVal + "...";
					$("#upload_progress").text(progress);
					$("#upload_progress").css("display", "inline");
				} else if (select == "") {
					showOnlyMessage(ERROR, "请选择文件！");
					return false;
				} else if (time == "") {
					showOnlyMessage(ERROR, "请选择时间！");
					return false;
				}
			},
			uploadProgress : function(event, position, total, percentComplete) {
				var percentVal = percentComplete + '%';
				var progress = "正在上传" + percentVal + "...";
				if (percentComplete != 100) {
					$("#upload_progress").text(progress);
				} else {
					$("#span_progress").css("display", "inline");
					$("#upload_progress").css("display", "none");
				}
			},
			success : function(data, statusText) {
				var percentVal = '100%';

			},
			complete : function(xhr, b, c) {
				var fileStatus = xhr.responseText;
				$("#originsubmit").attr("disabled", false);
				if (fileStatus.indexOf("失败") >= 0) {
					showOnlyMessage(ERROR, fileStatus);
					$("#originsubmit").val("续传");
				} else if (fileStatus.indexOf("成功") >= 0) {
					showOnlyMessage(INFO, fileStatus);
					$("#originsubmit").val("上传");
				} else {
					showOnlyMessage("warning", fileStatus);
				}
				$("#span_progress").css("display", "none");
			},
			error : function(data) {
				showOnlyMessage(ERROR, data.message);
			}
		});
		
		
		$("#signalCSVFile").ajaxForm({
			beforeSend : function(formData, jqForm, options) {
				var percentVal = '0%';
				var select = $("#signalfile").val();
				var time = $("#nettest_time").val();
				if (select != "" && time != "") {
					$("#signalSubmit").attr("disabled", true);
					var progress = "正在上传" + percentVal + "...";
					$("#signal_upload_progress").text(progress);
				} else if (select == "") {
					showOnlyMessage(ERROR, "请选择文件！");
					return false;
				} else if (time == "") {
					showOnlyMessage(ERROR, "请选择时间！");
					return false;
				}
			},
			uploadProgress : function(event, position, total, percentComplete) {
				var percentVal = percentComplete + '%';
				var progress = "正在上传" + percentVal + "...";
				if (percentComplete != 100) {
					$("#signal_upload_progress").text(progress);
				} else {
					$("#csv_load").css("display", "inline");
					$("#signal_upload_progress").css("display", "none");
				}
			},
			success : function(data, statusText) {
				var percentVal = '100%';

			},
			complete : function(xhr, b, c) {
				var fileStatus = xhr.responseText;
				$("#signalSubmit").attr("disabled", false);
				if (fileStatus.indexOf("失败") >= 0) {
					showOnlyMessage(ERROR, fileStatus);
					$("#signalSubmit").val("续传");
				} else if (fileStatus.indexOf("成功") >= 0) {
					showOnlyMessage(INFO, fileStatus);
					$("#signalSubmit").val("上传");
				} else {
					showOnlyMessage("warning", fileStatus);
				}
				$("#csv_load").css("display", "none");
			},
			error : function(data) {
				showOnlyMessage(ERROR, data.message);
			}
		});
		
		/*
		 * 单个csv网管文件导入
		 */
		/* function signalCSVSumit(element) {
			var file = $("#file3").val();
			var times=$("#nettest_time").val();
			if (times != "" &&file != "") {
				$(element).ajaxSubmit(function(message) {
		            $("#csv_load").css("display", "none");
					var status = message.success;
					if (status.indexOf("成功") > 0) {
						showOnlyMessage(INFO, status);
					} else {
						showOnlyMessage(ERROR, status);
					}
				});
				$("#csv_load").css("display", "inline");
			}else if (times == "") {
				showOnlyMessage(ERROR, "请选择时间！");
			} else if (file == "") {
				showOnlyMessage(ERROR, "请选择文件！");
			}
			return false;
		} */
	</script>
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>投诉工单数据</h5>
				</div>
				<div class="ibox-content" id="offline">
					<div class="col-sm-6">
						<div class="panel panel-success">
							<div class="panel-heading">投诉工单数据</div>
							<div class="panel-body">
								<!-- <div>
									<span><i>备注：</i> </span> <span>请选择客户投诉情况导出表和客户投诉小区导出表！</span><br><br>
								</div> -->
								<form action="${context}/data/uploadcomplain" method="post"
									enctype="multipart/form-data"
									onsubmit="return complainSumit(this);">
									<label>时间选择：</label> <input id="complaintime" name="time"
										style="margin-left: 25px;"
										class="btn btn-white layer-date starttime"
										placeholder="请选择文件时间"
										onclick="laydate({istime: false, format: 'YYYYMMDD'})"><br>
									<label style="width: 80px; padding-right: 20px;">客户投诉常驻小区:</label>
									<input class="btn btn-white" type="file" id="comlainfile"
										name="file" accept=".xls,.xlsx" multiple="multiple">
									<button class="btn btn-white upload_btn">选择上传文件</button>
									<div class="upload_title">未选择任何文件</div>
									<br> <label>客户投诉情况:</label> <input class="btn btn-white"
										type="file" id="customerfile" name="file" accept=".xls,.xlsx"
										multiple="multiple">
									<button class="btn btn-white upload_btn">选择上传文件</button>
									<div class="upload_title">未选择任何文件</div>
									<br> <input class="btn btn-success" type="submit"
										value="上传">
									<div class="btn loading" id="complaint_load"
										style="display: none;">
										<img
											src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在上传...</span>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>性能工单数据</h5>
				</div>
				<div class="ibox-content" id="offline">
					<div class="col-sm-6">
						<div class="panel panel-success" style="height: 230px">
							<div class="panel-heading">性能工单数据</div>
							<div class="panel-body">
								<!-- <div>
									<span><i>备注：</i> </span> <span>请选择单个性能工单表格文件</span>
								</div> -->
								<form id="form2" action="/newsdas/data/upload/capacitywork"
									method="post" enctype="multipart/form-data">
									<div class="ibox-tools" style="margin-top: -10px;">
										<a href="javascript:;"
											onclick="openIframe('${context}/work/capacity','工单验证')"><i>查看详情</i></a>
									</div>
									<label>选择文件:</label> <input class="btn btn-white" type="file"
										name="file" id="file2" accept=".xls">
									<button class="btn btn-white upload_btn">选择上传文件</button>
									<div class="upload_title">未选择任何文件</div>
									<br>
									<!-- <input
										class="btn btn-white" type="reset" value="重选"> -->
									<input id="submit1" class="btn btn-success" type="button"
										value="上传" onclick="submit_upload('#file2','#form2')"
										style="margin-top: 30px;">
									<div class="btn loading" id="capacity_load"
										style="display: none;">
										<img
											src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在上传...</span>
									</div>

									<button class="btn btn-info search" type="button"
										onclick="workOrderValidate()"
										style="float: right; margin-top: 60px;">工单验证</button>
									<div class="btn loading" id="Validate_load"
										style="display: none; float: right; margin-top: 60px;">
										<img
											src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在验证...</span>
									</div>

								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>中兴指标数据csv测试文件</h5>
				</div>
				<div class="ibox-content">
					<div class="col-sm-6">
						<div class="panel panel-success" style="height: 230px">
							<div class="panel-heading">中兴指标数据csv测试文件</div>
							<div class="panel-body">
								<form id="signalCSVFile" action="${context}/data/uploadfile" method="post" enctype='multipart/form-data'>
									<div class="ibox-tools" style="margin-top: -10px;">
										<a href="javascript:;"
											onclick="openIframe('${context}/cell/celltable','健康评估')"><i>查看详情</i></a>
									</div>
									<div class="form-group">
										<!-- 待提交表单 -->
										<label>时间选择：</label> 
										<input id="nettest_time" name="time" class="btn btn-white layer-date starttime"
											placeholder="请选择文件时间" onclick="laydate({istime: false, format: 'YYYYMMDD'})">
										<span id="nettime_error" class="error_msg">月份不能为空！</span><br>
										<label>选择文件:</label> 
										<input class="btn btn-white" type="file" name="file" id="signalfile" accept=".csv">
										<button class="btn btn-white upload_btn">选择上传文件</button>
										<div class="upload_title">未选择任何文件</div>
										<input id="signalSubmit" class="btn btn-success" type="submit" value="上传" style="margin-left: 10px;"><br>
										<!-- 待提交表单 -->
										
										
										<!-- 复合loadding -->
										<div class="btn loading" id="csv_load" style="display: none;">
											<img
												src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在上传...</span>
										</div>									
										<span id="signal_upload_progress"></span> <br>
										<!-- 复合loading -->
										
										
										
										
										
										
										<!-- 分析部分 -->
										<label>计算日期:</label> <input size="16" type="text"
											name="cal_time" id="net_caltime"
											placeholder="请选择计算模式月份（默认上一个月）" readonly
											class="form_datetime" style="width: 220px; margin-top: -10px">
										<button class="btn btn-info search" type="button" style="margin-left: 35px;"
											onclick="submit_cal()">分析</button>
										<div class="btn loading" id="cal_load" style="display: none;">
											<img
												src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在分析...</span>
										</div>
										<!-- 分析部分结束 -->
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>中兴指标全网数据(zip文件)</h5>
				</div>
				<div class="ibox-content">
					<div class="col-sm-6">
						<div class="panel panel-success">
							<div class="panel-heading">中兴指标全网数据(zip文件)</div>
							<div class="panel-body">
								<!-- <div>
									<span><i>备注：</i> </span> <span>请选择小区一天的网管数据</span>
								</div> -->
								<!-- onsubmit="return netzipSumit(this);" -->
								<form id="soucefile" action="${context}/data/uploadfile" enctype='multipart/form-data'
									method="post">
									<div class="ibox-tools" style="margin-top: -10px;">
										<a href="javascript:;"
											onclick="openIframe('${context}/cell/celltable','健康评估')"><i>查看详情</i></a>
									</div>
									<div class="form-group">
										<label>时间选择：</label> <input id="origintime" name="time"
											class="btn btn-white layer-date starttime"
											placeholder="请选择文件时间"
											onclick="laydate({istime: false, format: 'YYYYMMDD'})"><br>
										<label>选择文件:</label> <input class="btn btn-white" type="file"
											name="file" id="originfile" />
										<button class="btn btn-white upload_btn">选择上传文件</button>
										<div class="upload_title">未选择任何文件</div>
										<button id="originsubmit" class="btn btn-success"
											type="submit" style="margin-left: 10px;">上传</button>
										<span id="span_progress" style="display: none;"> <progress
												id="progress2" max="100" value="0"></progress><em>上传进度：</em><span id="progressvalue2">0%</span>											
										</span> 										
										<span id="upload_progress"></span> <br>
										<label>计算日期:</label> <input size="16" type="text"
											name="cal_time" id="net_caltime2"
											placeholder="请选择计算模式月份（默认上一个月）" readonly
											class="timepicker form_datetime"
											style="width: 220px; margin-top: -10px">
										<button class="btn btn-info search" type="button" style="margin-left: 35px;"
											onclick="submit_calzip()">分析</button>
										<div class="btn loading" id="calzip_load"
											style="display: none;">
											<img
												src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在分析...</span>
										</div>
										<br>
										<button class="btn btn-info search" type="button"
											onclick="submit_modelzip()" style="float: right">模式计算</button>
										<div class="btn loading" id="modelzip_load"
											style="display: none; float: right;">
											<img
												src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在计算...</span>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>操作日志</h5>
					<div class="ibox-tools">
						<!-- <a href="#"><i>更多...</i></a> -->
					</div>
					<div class="ibox-content">
						<div class="form-group">
							<label for="type" style="margin-left: 20px">数据类型</label> <select
								id="type" name="type" class="btn btn-white">
								<option value="">全部</option>
								<option value="性能工单数据">性能工单数据</option>
								<option value="投诉工单数据">投诉工单数据</option>
								<option value="单个中兴网管指标数据">单个中兴网管指标数据</option>
								<option value="中兴网管指标原始数据">中兴网管指标原始数据</option>
							</select> <label for="status" style="margin-left: 20px">状态</label> <select
								id="status" name="status" class="btn btn-white">
								<option value="">全部</option>
								<option value="1">成功</option>
								<option value="0">失败</option>
							</select>
							<my:btn type="search" onclick="search_log()"></my:btn>
							<!-- <button class="btn btn-info search" type="button" onclick="search_log()">查询</button> -->
						</div>
						<div id="historyTable"></div>
						<div id="pager_Table"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${context}/js/data/offline.js"></script>
</body>
</html>