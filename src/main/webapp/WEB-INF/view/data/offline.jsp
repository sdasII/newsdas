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
<script type="text/javascript" src="${context}/js/data/offline.js"></script>
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
.error_msg{display: none; color:red}
.loading img{height:20px;margin-right: 5px}
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
			ws = new WebSocket("ws://49.4.6.47:9999/newsdas/websocket");
			//ws = new WebSocket("ws://localhost:8080/newsdas/websocket");
		} else {
			alert("当前浏览器不支持WebSocket");
		}
		//连接发生错误的回调方法
		ws.onerror = function() {
			//alert("WebSocket连接发生错误");
			showOnlyMessage(ERROR, "WebSocket连接发生错误");
		};

		//连接成功建立的回调方法
		ws.onopen = function() {
			//alert("WebSocket连接成功");
			showOnlyMessage(INFO, "WebSocket连接成功");
		}

		//接收到消息的回调方法
		ws.onmessage = function(event) {
			var progress = event.data;
			$("#progress2").attr("value",progress);
            var value = progress+"%";
            $("#progressvalue2").text(value);
		}

		//连接关闭的回调方法
		ws.onclose = function() {
			//alert("WebSocket连接关闭");
			showOnlyMessage(INFO, "WebSocket连接关闭");
		}
						
		// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
		ws.onbeforeunload = function(){
			closews();
		}
	</script>
	<script type="text/javascript">
		var select = $("#originfile").val();
		var time = $("#origintime").val();
		
		function mySubmit(element) {
			
			if (select != "" && time !="") {
				$("#originsubmit").attr("disabled",true);
				$("#span_progress").css("display","inline");
				$("#progress2").attr("value",0);
				$("#progressvalue2").text("0%");
				
				$(element).ajaxSubmit(function(message) {
					var msg = message;
					var fileStatus = msg.status;
					$("#originsubmit").attr("disabled",false);
					if (fileStatus.indexOf("失败") >= 0) {
						showOnlyMessage(ERROR, fileStatus);
						$("#originsubmit").val("续传");
					} else if (fileStatus.indexOf("成功") >= 0) {
						showOnlyMessage(INFO, fileStatus);
						//$("#originfile").val("");
						$("#originsubmit").val("上传");
					} else {
						showOnlyMessage("warning", fileStatus);
					}
				});
				ws.send("start");
				/* setTimeout(function() {
					longPoling();
				}, 100); */			
			}else if(select == ""){
				showOnlyMessage(ERROR, "请选择文件！");
			}else if (time == "") {
				showOnlyMessage(ERROR, "请选择时间！");
			}

			//$("#myform").myform();
			return false;
		}
		
		function closews() {
			ws.close();
		}
		
		$('#soucefile').ajaxForm({
		    beforeSend: function(formData, jqForm, options) {
		        var percentVal = '0%';
		        var select = $("#originfile").val();
				var time = $("#origintime").val();
		        if (select != "" && time !="") {
					$("#originsubmit").attr("disabled",true);				
					$("#progress2").attr("value",0);
					$("#progressvalue2").text("0%");
					ws.send("start");		
					var progress = "正在上传"+percentVal+"...";
					$("#upload_progress").text(progress);
					$("#upload_progress").css("display","inline");
				}else if(select == ""){
					showOnlyMessage(ERROR, "请选择文件！");
					return false;
				}else if (time == "") {
					showOnlyMessage(ERROR, "请选择时间！");
					return false;
				}
		    },
		    uploadProgress: function(event, position, total, percentComplete) {
		        var percentVal = percentComplete + '%';
		        var progress = "正在上传"+percentVal+"...";
		        if(percentComplete!=100){
		        	$("#upload_progress").text(progress);
		        }else{
		        	$("#span_progress").css("display","inline");
		        	$("#upload_progress").css("display","none");
		        }
		    },
		    success: function(data,statusText) {
		        var percentVal = '100%';
		        
		    },
			complete: function(xhr,b,c) {
				var fileStatus = xhr.responseText;
				$("#originsubmit").attr("disabled",false);
				if (fileStatus.indexOf("失败") >= 0) {
					showOnlyMessage(ERROR, fileStatus);
					$("#originsubmit").val("续传");
				} else if (fileStatus.indexOf("成功") >= 0) {
					showOnlyMessage(INFO, fileStatus);
					$("#originsubmit").val("上传");
				} else {
					showOnlyMessage("warning", fileStatus);
				}
			},
			error: function(data){
				showOnlyMessage(ERROR, data.message);
			}
		}); 
	</script>
	<script type="text/javascript">
		function complainSumit(element){
			var select = $("#comlainfile").val();
			var time = $("#complaintime").val();
			var length = $("#comlainfile").files
			if (time!="" && select!="") {
				$(element).ajaxSubmit(function(message){
					$("#complaint_load").css("display","none");
					var status = message.success;
					if(status.indexOf("成功")>0){
						showOnlyMessage(INFO, status);
					}else {
						showOnlyMessage(ERROR, status);
					}
					
				});
				$("#complaint_load").css("display","inline");
			}else if(select == ""){
				showOnlyMessage(ERROR, "请选择文件！");
			}else if (time == "") {
				showOnlyMessage(ERROR, "请选择时间！");
			}	
			return false;
		}
	</script>
	<div class="ibox-content" id="offline">
		<div class="row">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">性能工单数据</div>
					<div class="panel-body">
						<div>
							<span><i>备注：</i> </span> <span>请选择单个性能工单表格文件</span>
						</div>
						<form id="form2" action="/newsdas/data/upload?type=capacity"
							method="post" enctype="multipart/form-data">
							<input class="btn btn-white" type="file" name="file" id="file2"
								accept=".xls"> <br> <input class="btn btn-white"
								type="reset" value="重选"> <input id="submit1"
								class="btn btn-success" type="button" value="上传"
								onclick="submit_upload('#file2','#form2')"> <input
								class="btn btn-inverse" type="button" value="查看上传记录"
								onclick="openIframe('性能工单数据')">
							<div class="btn loading" id="capacity_load" style="display: none;">
								<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在上传...</span>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">投诉工单数据</div>
					<div class="panel-body">
						<div>
							<span><i>备注：</i> </span> <span>请选择客户投诉情况导出表和客户投诉小区导出表！</span><br>
							<span style="color: red;"><em>特别提示：</em><span>可上传多个文件，待上传文件名需包含 "*客户投诉常驻小区*" 或 "*客户投诉情况*"，否则系统无法识别！</span></span>
						</div>
						<form action="${context}/data/uploadcomplain" method="post" 
								enctype="multipart/form-data" onsubmit="return complainSumit(this);">
							<input id="complaintime" name="time"
								style="display: inline; padding: -10px; margin: -10px; height: 39px; margin-right: 10px;"
								class="btn btn-white layer-date starttime" placeholder="请选择文件时间"
								onclick="laydate({istime: false, format: 'YYYYMMDD'})">
							<input class="btn btn-white" type="file" id="comlainfile" name="file" accept=".xls,.xlsx" style="display: inline;" multiple="multiple"> <br> <br>
							<input class="btn btn-white" type="reset" value="重选">
							<input class="btn btn-success" type="submit" value="上传"> 
							<input class="btn btn-inverse" type="button" value="查看上传记录" onclick="openIframe('投诉工单数据')">
							<div class="btn loading" id="complaint_load" style="display: none;">
							<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在上传...</span></div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			
			
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">中兴网管指标数据</div>
					<div class="panel-body">
						<div>
							<span><i>备注：</i> </span> <span>请选择小区一天的网管数据</span>
						</div>
						<%-- <form id="form1" action="${context}/data/upload?type=network"
							method="post" enctype="multipart/form-data">
							<input id="time" name="time"
								style="display: inline; padding: -10px; margin: -10px; height: 39px; margin-right: 10px;"
								class="btn btn-white layer-date starttime"
								placeholder="请选择计算模式年月"
								onclick="laydate({istime: true, format: 'YYYYMM'})"> <input
								class="btn btn-white" type="file" name="file" id="file1"
								style="display: inline;" accept=".csv"> <br> <br>
							<input class="btn btn-white" type="reset" value="重选"> 
							<input
								class="btn btn-success" type="button" value="上传"
								onclick="submit_upload('#file1','#form1')"> <input
								class="btn btn-inverse" type="button" value="查看上传记录"
								onclick="openIframe('中兴网管指标数据')">
							<div class="btn loading" id="network_load" style="display: none;">
								<img src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>正在上传...</span>
							</div>
						</form> --%>
						
						
						
						
						<form id="form1" action="${context}/data/upload?type=network" method="post">
 							<div class="form-group">
 							<tips style="font-style: italic;">注意：计算日期需要与HDFS文件地址中的数据相对应</tips>
 							<br><br>
 							<label for="time" style="width:65px">选择月份：</label>
 							<input name="time" id="net_time" class="btn btn-white layer-date" placeholder="请选择年月"
 										onclick="laydate({istime: false, format: 'YYYYMM'})" style="margin-top: -10px">
 							<span id="nettime_error" class="error_msg">月份不能为空！</span>
 							</div>
 							<div class="form-group">
 								<label for="cal_time">计算日期：</label>
 									<input name="cal_time" id="net_caltime" class="btn btn-white layer-date" placeholder="请选择计算日期"
 										onclick="laydate({istime: false, format: 'YYYYMMDD'})" style="margin-top: -10px">
 									<span id="netcaltime_error" class="error_msg">计算日期不能为空！</span>
 								</div>
 							<div class="form-group">
 								<label for="path">HDFS文件地址：</label>
 									<input type="text" id="net_path" class="form-control" name="path" placeholder="请输入NDFS文件地址" />
 									<span class="help-block m-b-none">格式：'hdfs://ip:port/data/NetManage/yyyy/MM/yyyyMMdd*.csv'</span>
 									<span id="netpath_error" class="error_msg">文件地址不能为空！</span>
 								</div>
 							<div class="form-group">
 								<div class="col-sm-4 col-sm-offset-3" style="margin-top: 10px">
 									<button class="btn btn-info search" type="button" onclick="submit_cal()">计算</button>
 									<button class="btn btn-white" type="reset" id="reset_btn">重置</button>
 								</div>
 							</div>
  						</form>
						
						
						
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">中兴网管指标原始数据</div>
					<div class="panel-body">
						<div>
							<span><i>备注：</i> </span> <span>每次请选择一个文件！</span>
						</div><!-- onsubmit="return mySubmit(this);" -->
						<form id ="soucefile" action='${context}/data/uploadfile' method='post'
							enctype='multipart/form-data'>
							<input id="origintime" name="time"
								style="display: inline; padding: -10px; margin: -10px; height: 39px; margin-right: 10px;"
								class="btn btn-white layer-date starttime" placeholder="请选择文件时间"
								onclick="laydate({istime: false, format: 'YYYYMMDD'})">
							<input class="btn btn-white" type="file" name="file"
								id="originfile" style="display: inline;" /><br> <br>
							<button class="btn btn-white" type="reset">清空</button>
							<button id="originsubmit" class="btn btn-success" type="submit">上传</button>
							<input class="btn btn-inverse" type="button" value="查看上传记录"
							onclick="openIframe('中兴网管指标原始数据')">
							<span id="span_progress" style="display: none;"><progress id="progress2" max="100" value="0"></progress><em>上传进度：</em><span id="progressvalue2">0%</span></span><br>
							<span id="upload_progress" style="display: none;"></span>
						</form>						
					</div>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>