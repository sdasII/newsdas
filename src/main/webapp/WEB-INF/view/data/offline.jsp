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
</style>
</head>
<body>
	<script type="text/javascript">
		var status = 'unkown';
		if (status == 'success') {
			showOnlyMessage(INFO, "å¯¼å¥æ°æ®æåï¼");
		} else if (status.indexOf("fail") >= 0) {
			showOnlyMessage(ERROR, status);
		}
	</script>
	<script type="text/javascript">
		function mySubmit(element) {
			var select = $("#originfile").val();
			var time = $("#origintime").val();
			if (select != "" && time !="") {
				$("#originsubmit").attr("disabled",true);
				$(element).ajaxSubmit(function(message) {
					var msg = eval("(" + message + ")");
					var fileStatus = msg.status;
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
				setTimeout(() => {
					longPoling();
				}, 6000);
				$("#originsubmit").attr("disabled",false);
			}else if(select == ""){
				showOnlyMessage(ERROR, "请选择文件！");
			}else if (time == "") {
				showOnlyMessage(ERROR, "请选择时间！");
			}

			//$("#myform").myform();
			return false;
		}
	</script>
	<div class="ibox-content" id="offline">
		<div class="row">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">中兴网管指标数据</div>
					<div class="panel-body">
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
						<!-- <input class="btn btn-white" type="button" value="查看上传记录"
								onclick="openIframe('中兴网管指标数据')"> -->
					</div>
				</div>
			</div>
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
								class="btn btn-white" type="button" value="上传"
								onclick="submit_upload('#file2','#form2')"> <input
								class="btn btn-white" type="button" value="查看上传记录"
								onclick="openIframe('性能工单数据')">
							<progress id="progress1" max="200" style="display: none">正在上传...</progress>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">中兴网管指标原始数据</div>
					<div class="panel-body">
						<div>
							<span><i>备注：</i> </span> <span>每次请选择一个文件！</span>
						</div>
						<form action='${context}/data/uploadfile' method='post'
							enctype='multipart/form-data' onsubmit="return mySubmit(this);">
							<input id="origintime" name="time"
								style="display: inline; padding: -10px; margin: -10px; height: 39px; margin-right: 10px;"
								class="btn btn-white layer-date starttime" placeholder="请选择文件时间"
								onclick="laydate({istime: false, format: 'YYYYMMDD'})">
							<input class="btn btn-white" type="file" name="file"
								id="originfile" style="display: inline;" /><br> <br>
							<button class="btn btn-white" type="reset">清空</button>
							<button id="originsubmit" class="btn btn-success" type="submit">上传</button>
							<input class="btn btn-white" type="button" value="查看上传记录"
								onclick="openIframe('中兴网管指标原始数据')">
							<progress id="progress2" max="100" value="0"></progress>
							<em>上传进度：</em><span id="progressvalue2">0%</span>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>