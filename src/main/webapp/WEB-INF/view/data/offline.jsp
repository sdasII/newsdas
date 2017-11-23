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
	<link href="${context}/lib/hplus/css/plugins/webuploader/webuploader.css" rel="stylesheet">
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
		var status = 'unkown';
		if (status == 'success') {
			showOnlyMessage(INFO, "å¯¼å¥æ°æ®æåï¼");
		} else if (status.indexOf("fail") >= 0) {
			showOnlyMessage(ERROR, status);
		}		
	</script>
	<script type="text/javascript">
		function mySubmit(element){
			
			var select = $("#originfile").val();
			if(select!=""){
				$(element).ajaxSubmit(function(message){
					var msg = eval("("+message+")");
					var fileStatus = msg.status;
				    if(fileStatus.indexOf("å¤±è´¥")>=0){
						showOnlyMessage(ERROR, fileStatus);
						$("#originsubmit").val("ç»­ä¼ ");
					}else if(fileStatus.indexOf("æå")>=0){
						showOnlyMessage(INFO, fileStatus);
						$("#originfile").val("");
						$("#originsubmit").val("ä¸ä¼ ");
					}else{
						showOnlyMessage("warning", fileStatus);
					}
		        });
			}else{
				showOnlyMessage(ERROR, "è¯·éæ©æä»¶ï¼");
			}
			
			
			//$("#myform").myform();
			return false;
		}
	</script>
	<div class="ibox-content" id="offline">
		<div class="row">
			<!-- <div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">æ°æ®</div>
					<div class="panel-body">
						
						<div id="uploader" class="wu-example">
							ç¨æ¥å­æ¾æä»¶ä¿¡æ¯
							<div id="thelist" class="uploader-list"></div>
							<div class="btns">
								<div id="picker">éæ©æä»¶</div>
								<button id="ctlBtn" class="btn btn-default">å¼å§ä¸ä¼ </button>
							</div>
						</div>
						
					</div>
				</div>
			</div> -->
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">ä¸­å´ç½ç®¡ææ æ°æ®</div>
					<div class="panel-body">
					<div>
								<span><i>å¤æ³¨ï¼</i> </span> <span>è¯·éæ©å°åºä¸å¤©çç½ç®¡æ°æ®</span>
							</div>
						<form id="form1" action="/newsdas/data/upload?type=network" method="post"
							enctype="multipart/form-data">
							<input id="time" name="time" style="display: inline;padding: -10px;margin: -10px;height: 39px;margin-right: 10px;"
										class="btn btn-white layer-date starttime" placeholder="è¯·éæ©è®¡ç®æ¨¡å¼å¹´æ"
										onclick="laydate({istime: true, format: 'YYYYMM'})">
							<input class="btn btn-white" type="file" name="file" id="file1" style="display: inline;"  accept=".csv"> <br> <br><input
								class="btn btn-white" type="reset" value="éé"> <input 
								class="btn btn-white" type="button" value="ä¸ä¼ " onclick="submit_upload('#file1','#form1')">
								<input class="btn btn-white" type="button" value="æ¥çä¸ä¼ è®°å½" onclick="openIframe('ä¸­å´ç½ç®¡ææ æ°æ®')"> 
								<progress id="progress" style="display: none">æ­£å¨ä¸ä¼ ...</progress>
						</form>
					</div>
				</div>
			</div>		
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">æ§è½å·¥å</div>
					<div class="panel-body">
						<div>
								<span><i>å¤æ³¨ï¼</i> </span> <span>è¯·éæ©åä¸ªæ§è½å·¥åè¡¨æ ¼æä»¶</span>
						</div>
						<form id="form2" action="/newsdas/data/upload?type=capacity"
							method="post" enctype="multipart/form-data">
							<input class="btn btn-white" type="file" name="file" id="file2" accept=".xls"> 
								<br>
								<input class="btn btn-white" type="reset" value="éé"> <input id="submit1"
								class="btn btn-white" type="button" value="ä¸ä¼ "  onclick="submit_upload('#file2','#form2')">
								<input class="btn btn-white" type="button" value="æ¥çä¸ä¼ è®°å½" onclick="openIframe('æ§è½å·¥åæ°æ®')"> 
							<progress id="progress1" max="200" style="display: none">æ­£å¨ä¸ä¼ ...</progress>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">ä¸­å´ç½ç®¡ææ åå§æ°æ®</div>
					<div class="panel-body" style="height: 230px">
							<div>
								<span><i>å¤æ³¨ï¼</i> </span> <span>æ¯æ¬¡è¯·éæ©ä¸ä¸ªæä»¶ï¼</span>
							</div>
							<div class="col-sm-6" style="height: 125px;width: 100%;margin-top: 5px;">
								<!-- <label>å·²éæä»¶ï¼</label> -->
								<div
									style="width: 100%; height: 95%; margin-top: -5px; border: 1px solid #ccc;">
									<ul id="fileList">
										<li>
										<form action='/newsdas/data/uploadfile' method='post'enctype='multipart/form-data' 
											onsubmit="return mySubmit(this);">
											<input type="file" name="file" id="originfile"/>
											<button class="btn btn-white" type="reset">æ¸ç©º</button>
											<button id="originsubmit" class="btn btn-success" type="submit">ä¸ä¼ </button>
										</form>
										</li>
										
									</ul>
									<input class="btn btn-white" type="button" value="æ¥çä¸ä¼ è®°å½" onclick="openIframe('ä¸­å´ç½ç®¡ææ åå§æ°æ®')">
								</div>
							</div>


					</div>
				</div>
			</div>

		</div>
			
	</div>

	

	<!-- <script type="text/javascript">
	var uploader = WebUploader.create({

	    // swfæä»¶è·¯å¾
	    swf: '/newsdas' + '/lib/hplus/js/plugins/webuploader/Uploader.swf',
	    // æä»¶æ¥æ¶æå¡ç«¯ã
	    server: '',
	    // éæ©æä»¶çæé®ãå¯éã
	    // åé¨æ ¹æ®å½åè¿è¡æ¯åå»ºï¼å¯è½æ¯inputåç´ ï¼ä¹å¯è½æ¯flash.
	    pick: '#picker',
	    // ä¸åç¼©image, é»è®¤å¦ææ¯jpegï¼æä»¶ä¸ä¼ åä¼åç¼©ä¸æåä¸ä¼ ï¼
	    resize: false
	});
	// å½ææä»¶è¢«æ·»å è¿éåçæ¶å
	uploader.on( 'fileQueued', function( file ) {
	    $list.append( '<div id="' + file.id + '" class="item">' +
	        '<h4 class="info">' + file.name + '</h4>' +
	        '<p class="state">ç­å¾ä¸ä¼ ...</p>' +
	    '</div>' );
	});
	// æä»¶ä¸ä¼ è¿ç¨ä¸­åå»ºè¿åº¦æ¡å®æ¶æ¾ç¤ºã
	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $percent = $li.find('.progress .progress-bar');
	    // é¿åéå¤åå»º
	    if ( !$percent.length ) {
	        $percent = $('<div class="progress progress-striped active">' +
	          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	          '</div>' +
	        '</div>').appendTo( $li ).find('.progress-bar');
	    }

	    $li.find('p.state').text('ä¸ä¼ ä¸­');
	    $percent.css( 'width', percentage * 100 + '%' );
	});
	// æä»¶ä¸ä¼ æå/å¤±è´¥æ¾ç¤ºã
	uploader.on( 'uploadSuccess', function( file ) {
	    $( '#'+file.id ).find('p.state').text('å·²ä¸ä¼ ');
	});

	uploader.on( 'uploadError', function( file ) {
	    $( '#'+file.id ).find('p.state').text('ä¸ä¼ åºé');
	});

	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').fadeOut();
	});
	</script> -->

</body>
</html>