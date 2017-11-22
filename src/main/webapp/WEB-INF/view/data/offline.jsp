<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/include/common.jsp"%>
<script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="${context}/js/data/offline.js"></script>
<style type="text/css"
	src="${context}/lib/hplus/css/plugins/webuploader/webuploader.css"></style>
<script
	src="${context}/lib/hplus/js/plugins/webuploader/webuploader.min.js"></script>
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
	height:40px;
	padding: 3px;
	border: 1px solid #ccc;
}
#fileList li button{float:right;margin-right: 5px;margin-top: -28px}
#fileList input{margin-top: 5px;}
</style>
</head>
<body>
	<script type="text/javascript">
		var status = '${success}';
		if (status == 'success') {
			showOnlyMessage(INFO, "导入数据成功！");
		} else if (status.indexOf("fail") >= 0) {
			showOnlyMessage(ERROR, status);
		}
	</script>
	<div class="ibox-content" id="offline">
		<div class="row">
			<!-- <div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">数据</div>
					<div class="panel-body">
						
						<div id="uploader" class="wu-example">
							用来存放文件信息
							<div id="thelist" class="uploader-list"></div>
							<div class="btns">
								<div id="picker">选择文件</div>
								<button id="ctlBtn" class="btn btn-default">开始上传</button>
							</div>
						</div>
						
					</div>
				</div>
			</div> -->
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">中兴网管指标数据</div>
					<div class="panel-body">
						<div>
							<span><i>备注：</i> </span> <span>请选择小区一天的网管数据</span>
						</div>
						<form id="form1" action="${context}/data/upload?type=network"
							method="post" enctype="multipart/form-data">
							<input id="time" name="time"
								style="display: inline; padding: -10px; margin: -10px; height: 39px; margin-right: 10px;"
								class="btn btn-white layer-date starttime"
								placeholder="请选择计算模式年月"
								onclick="laydate({istime: true, format: 'YYYYMM'})"> <input
								class="btn btn-white" type="file" name="file" id="file1"
								style="display: inline;" accept=".csv"> <br> <br>
							<input class="btn btn-white" type="reset" value="重选"> <input
								class="btn btn-white" type="button" value="上传"
								onclick="submit_upload('#file1','#form1')"> <input
								class="btn btn-white" type="button" value="查看上传记录"
								onclick="openIframe('中兴网管指标数据')">
							<progress id="progress" style="display: none">正在上传...</progress>
						</form>
					</div>
				</div>
			</div>
			
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">性能工单</div>
					<div class="panel-body">
						<div>
							<span><i>备注：</i> </span> <span>请选择单个性能工单表格文件</span>
						</div>
						<form id="form2" action="${context}/data/upload?type=capacity"
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
					<div class="panel-body" style="height: 230px">
							<div class="col-sm-6" style="height: 125px;width: 100%">
								<!-- <label>已选文件：</label> -->
								<div
									style="width: 100%; height: 95%; margin-top: -5px; border: 1px solid #ccc;">
									<ul id="fileList">
										<li>
										<form action='${context}/data/upload?type=file' method='post'enctype='multipart/form-data'>
											<input type="file" name="file" />
											<button class="btn btn-white" type="reset">清空</button>
											<button class="btn btn-success" type="submit">上传</button>
										</form>
										</li>
										<li>
										<form action='${context}/data/upload?type=file' method='post'enctype='multipart/form-data'>
											<input type="file" name="file" />
											<button class="btn btn-white" type="reset">清空</button>
											<button class="btn btn-success" type="submit">上传</button>
										</form>
										</li>
										<li>
										<form action='${context}/data/upload?type=file' method='post'enctype='multipart/form-data'>
											<input type="file" name="file" />
											<button class="btn btn-white" type="reset">清空</button>
											<button class="btn btn-success" type="submit">上传</button>
										</form>
										</li>
									</ul>
									<input class="btn btn-white" type="button" value="查看上传记录" onclick="openIframe('中兴网管指标原始数据')">
								</div>
							</div>


					</div>
				</div>
			</div>

		</div>
		<div class="row" style="display: none !important;">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">故障工单</div>
					<div class="panel-body">
						<!-- <p>容量：2.4T</p>
						<p>采样频率：15min</p>
						<p>最后采样时间：2017-08-01</p> -->
						<form id="form4" action="${context}/data/upload?type=fault"
							method="post" enctype="multipart/form-data">

							<input class="btn btn-white" type="file" name="file" id="file4"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="重选"> <input
								class="btn btn-white" type="button" value="上传"
								onclick="submit_upload('#file4','#form4')">

						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">投诉数据</div>
					<div class="panel-body">
						<!-- <p>容量：2.4T</p>
						<p>采样频率：1天</p>
						<p>最后采样时间：2017-08-01</p> -->
						<form id="form5" action="${context}/data/upload?type=complaint"
							method="post" enctype="multipart/form-data">

							<input class="btn btn-white" type="file" name="file" id="file5"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="重选"> <input
								class="btn btn-white" type="button" value="上传"
								onclick="submit_upload('#file5','#form5')">

						</form>

					</div>

				</div>
			</div>
		</div>
		<div class="row" style="display: none !important;">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">可视化小区退服</div>
					<div class="panel-body">
						<!-- <p>容量：2.4T</p>
						<p>采样频率：实时数据</p>
						<p>最后采样时间：2017-08-01</p> -->
						<form id="form6" action="${context}/data/upload?type=outservice"
							method="post" enctype="multipart/form-data">

							<input class="btn btn-white" type="file" name="file" id="file6"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="重选"> <input
								class="btn btn-white" type="button" value="上传"
								onclick="submit_upload('#file6','#form6')">

						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">投诉详情数据</div>
					<div class="panel-body">
						<form id="form7" action="" method="post"
							enctype="multipart/form-data">
							<input class="btn btn-white" type="file" name="file" id="file7"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="重选"> <input
								class="btn btn-white" type="button" value="上传"
								onclick="submit_upload('#file7','#form7')">

						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- <script type="text/javascript">
	var uploader = WebUploader.create({

	    // swf文件路径
	    swf: '${context}' + '/lib/hplus/js/plugins/webuploader/Uploader.swf',
	    // 文件接收服务端。
	    server: '',
	    // 选择文件的按钮。可选。
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: '#picker',
	    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	    resize: false
	});
	// 当有文件被添加进队列的时候
	uploader.on( 'fileQueued', function( file ) {
	    $list.append( '<div id="' + file.id + '" class="item">' +
	        '<h4 class="info">' + file.name + '</h4>' +
	        '<p class="state">等待上传...</p>' +
	    '</div>' );
	});
	// 文件上传过程中创建进度条实时显示。
	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $percent = $li.find('.progress .progress-bar');
	    // 避免重复创建
	    if ( !$percent.length ) {
	        $percent = $('<div class="progress progress-striped active">' +
	          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	          '</div>' +
	        '</div>').appendTo( $li ).find('.progress-bar');
	    }

	    $li.find('p.state').text('上传中');
	    $percent.css( 'width', percentage * 100 + '%' );
	});
	// 文件上传成功/失败显示。
	uploader.on( 'uploadSuccess', function( file ) {
	    $( '#'+file.id ).find('p.state').text('已上传');
	});

	uploader.on( 'uploadError', function( file ) {
	    $( '#'+file.id ).find('p.state').text('上传出错');
	});

	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').fadeOut();
	});
	</script> -->
</body>
</html>