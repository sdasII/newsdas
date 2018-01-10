<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="/include/common.jsp"%>
<script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="${context}/js/work/capacity.js"></script>
<style type="text/css">
input {
	padding-top: 5px;
	padding-bottom: 5px;
	margin-top: 10px;
	margin-bottom: 10px
}
.SelectRed{
	color:red;
}
.SelectGre{
	color:green;
}
.SelectYel{
	color: #DFCD15;
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
.ibox-content select{width: 120px; height: 35px; margin-right: 10px; margin-left: 10px;}
#table_list_1 a{text-decoration: underline}
.btn-primary{margin-left: 10px}
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
						<h5>工单验证列表</h5>
						<div class="ibox-tools">
						<div class="btn-group" id="datePicker">
								<button class="btn btn-info datePicker" type="button">最近一日</button>
								<button class="btn btn-white datePicker" type="button">周</button>
								<button class="btn btn-white datePicker" type="button">月</button>
								<button class="btn btn-white datePicker" type="button">按时间选择</button>
								<div id="timeselect" style="display: none;float: left;">
									<input style="margin-left:5px;margin-top: -7px !important;" id="starttime"
										class="layer-date starttime" placeholder="请输入开始时间"
										onclick="laydate({istime: false, format: 'YYYY-MM-DD'})">
									<span id="span" style="margin-top: -10px ;display: inline !important;"
										class="input-group-addon">到</span> 
									<input  style="margin-top: -7px !important;" class="layer-date endtime" id="endtime"
										placeholder="请输入结束时间"
										onclick="laydate({istime: false, format: 'YYYY-MM-DD'})">
									<button class="btn btn-info search" type="button" onclick="select()">确定</button>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<div>
							<label>小区名称</label> <input type="text" placeholder="请输入小区名称"
								id="name" name="name"> 
							<label>所属区域</label> 
								<select id="area" class="btn btn-white">
									<option>全部</option>
								</select> 
							<label>监控内容</label> 
								<select id="content"  class="btn btn-white" style="width: 175px">
									<option>全部</option>
									<option>新切换出成功率(4次连续)</option>
									<option>新PRB利用率(4次连续)</option>
								</select>
							<label>判断结果</label> 
							<select id="result"  class="btn btn-white">
								<option>全部工单</option>
								<option>高度可疑</option>
								<option>可疑工单</option>
								<option>正常工单</option>
							</select>							
							<!-- <button class="btn btn-success" onclick="javascript:select()">查询</button> -->
							<my:btn type="search" onclick="select()"></my:btn>	
							<my:btn type="export" onclick="exportExcel()"></my:btn>																				
							<!-- <button class="btn btn-success" onclick="exportExcel()">导出</button> -->
						</div>
					</div>
					<div class="footer" style="height: 60px;color:red">
							<div>
								<span>备注：红色为高度可疑工单;绿色为符合条件工单;黄色为可疑工单;其它为待验证工单。</span>
							</div>
						</div>
					<div>
						<!-- loading -->
						<div class="loading_bk" id="table_loadbk"></div>
						<div class="loading" id="table_load">
							<img
								src="${context}/lib/hplus/css/plugins/blueimp/img/loading.gif"><span>内容加载中...</span>
						</div>
						<!-- loading -->
						<table id="table_list_1"></table>
						<div id="toolbar"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>