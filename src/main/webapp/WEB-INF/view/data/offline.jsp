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
<style type="text/css">
.btn-circle{
	width: 25px;
    height: 25px;
    padding: 0px;
    margin-right:5px;
    vertical-align: middle;
    float: right
    }
    #fileList{
    list-style: none;
    padding-left: 0px;
    }
    #fileList span{
   		font-size: 15px;
        margin-top: -2px;
    }
    #fileList li{
    padding: 3px;
    border: 1px solid #ccc;
    }
</style>
</head>
<body>
	<script type="text/javascript">
		var status = '${success}';
		if (status == 'success') {
			showOnlyMessage(INFO, "å¯¼å¥æ°æ®æåï¼");
		} else if (status.indexOf("fail") >= 0) {
			showOnlyMessage(ERROR, status);
		}
	</script>
	<div class="ibox-content" id="offline">
		<div class="row">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">ä¸­å´ç½ç®¡ææ æ°æ®</div>
					<div class="panel-body">
						<div>
							<span><i>å¤æ³¨ï¼</i> </span> <span>è¯·éæ©å°åºä¸å¤©çç½ç®¡æ°æ®</span>
						</div>
						<form id="form1" action="${context}/data/upload?type=network"
							method="post" enctype="multipart/form-data">
							<input id="time" name="time"
								style="display: inline; padding: -10px; margin: -10px; height: 39px; margin-right: 10px;"
								class="btn btn-white layer-date starttime"
								placeholder="è¯·éæ©è®¡ç®æ¨¡å¼å¹´æ"
								onclick="laydate({istime: true, format: 'YYYYMM'})"> <input
								class="btn btn-white" type="file" name="file" id="file1"
								style="display: inline;" accept=".csv"> <br> <br>
							<input class="btn btn-white" type="reset" value="éé"> <input
								class="btn btn-white" type="button" value="ä¸ä¼ "
								onclick="submit_upload('#file1','#form1')"> <input
								class="btn btn-white" type="button" value="æ¥çä¸ä¼ è®°å½"
								onclick="openIframe('ä¸­å´ç½ç®¡ææ æ°æ®')">
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
						<form id="form2" action="${context}/data/upload?type=capacity"
							method="post" enctype="multipart/form-data">
							<input class="btn btn-white" type="file" name="file" id="file2"
								accept=".xls"> <br> <input class="btn btn-white"
								type="reset" value="éé"> <input id="submit1"
								class="btn btn-white" type="button" value="ä¸ä¼ "
								onclick="submit_upload('#file2','#form2')"> <input
								class="btn btn-white" type="button" value="æ¥çä¸ä¼ è®°å½"
								onclick="openIframe('æ§è½å·¥åæ°æ®')">
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
					<div class="panel-body" style="height:230px">
						<div>
							<span><i>å¤æ³¨ï¼</i> </span> <span>å¯éæ©å¤ä¸ªæä»¶ï¼</span>
						</div>
						<form id="form3" action="${context}/data/upload?type=file"
							method="post" enctype="multipart/form-data">
							<div class="col-sm-6" style="border-right: 2px solid #999999;width:30%;height: 175px;">
								<input class="btn btn-white" multiple="multiple" type="file" 
									id="file3" style="display: inline; width: 95px" /> <br><br><input
									class="btn btn-white" type="button" value="æ¥çä¸ä¼ è®°å½"
									onclick="openIframe('ä¸­å´ç½ç®¡ææ åå§æ°æ®')">
								<progress id="progress" style="display: none">æ­£å¨ä¸ä¼ ...</progress>

							</div>
							<div class="col-sm-6" style="height: 125px;margin-top: -20px;width:70%">
								<label>å·²éæä»¶ï¼</label>
								<div style="width: 100%;height: 95%;margin-top: -5px;border: 1px solid #ccc;">
									<ul id="fileList">
									</ul>
								</div>
							</div>
						</form>


					</div>
				</div>
			</div>

		</div>
		<div class="row" style="display: none !important;">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">æéå·¥å</div>
					<div class="panel-body">
						<!-- <p>å®¹éï¼2.4T</p>
						<p>éæ ·é¢çï¼15min</p>
						<p>æåéæ ·æ¶é´ï¼2017-08-01</p> -->
						<form id="form4" action="${context}/data/upload?type=fault"
							method="post" enctype="multipart/form-data">

							<input class="btn btn-white" type="file" name="file" id="file4"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="éé"> <input
								class="btn btn-white" type="button" value="ä¸ä¼ "
								onclick="submit_upload('#file4','#form4')">

						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">æè¯æ°æ®</div>
					<div class="panel-body">
						<!-- <p>å®¹éï¼2.4T</p>
						<p>éæ ·é¢çï¼1å¤©</p>
						<p>æåéæ ·æ¶é´ï¼2017-08-01</p> -->
						<form id="form5" action="${context}/data/upload?type=complaint"
							method="post" enctype="multipart/form-data">

							<input class="btn btn-white" type="file" name="file" id="file5"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="éé"> <input
								class="btn btn-white" type="button" value="ä¸ä¼ "
								onclick="submit_upload('#file5','#form5')">

						</form>

					</div>

				</div>
			</div>
		</div>
		<div class="row" style="display: none !important;">
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">å¯è§åå°åºéæ</div>
					<div class="panel-body">
						<!-- <p>å®¹éï¼2.4T</p>
						<p>éæ ·é¢çï¼å®æ¶æ°æ®</p>
						<p>æåéæ ·æ¶é´ï¼2017-08-01</p> -->
						<form id="form6" action="${context}/data/upload?type=outservice"
							method="post" enctype="multipart/form-data">

							<input class="btn btn-white" type="file" name="file" id="file6"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="éé"> <input
								class="btn btn-white" type="button" value="ä¸ä¼ "
								onclick="submit_upload('#file6','#form6')">

						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="panel panel-success">
					<div class="panel-heading">æè¯è¯¦ææ°æ®</div>
					<div class="panel-body">
						<form id="form7" action="" method="post"
							enctype="multipart/form-data">
							<input class="btn btn-white" type="file" name="file" id="file7"
								multiple="multiple" accept="text/*"> <br> <input
								class="btn btn-white" type="reset" value="éé"> <input
								class="btn btn-white" type="button" value="ä¸ä¼ "
								onclick="submit_upload('#file7','#form7')">

						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>