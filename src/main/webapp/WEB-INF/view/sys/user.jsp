<%@page import="com.iscas.sdas.dto.sys.UserDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/include/common.jsp"%>
<script src="${context}/lib/hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="${context}/js/sys/user.js"></script>
<style type="text/css">
.btn-primary {
	margin-right: 5px
}
</style>
</head>
<body style="margin: 0px 25px;">
	<script type="text/javascript">
		var role = "${role}";
	</script>
	<%
		UserDto userinfo = (UserDto) session.getAttribute("userInfo");
		pageContext.setAttribute("role", userinfo.getRolename());
	%>
	<div>
		<div class="row">
			<div class="ibox-title">
				<h5>用户信息</h5>
			</div>

			<div class="ibox-content">
				<div class="form-group">
					<form id="searchForm" method="post">
						<div>
							<label>用户ID</label> <input type="text" name="userId" id="userId" />
							<label>用户名</label> <input type="text" name="userName"
								id="userName" />
							<my:btn type="search" onclick="searchUserInfo()"></my:btn>
						</div>
					</form>
				</div>
				<div id="toolbar" class="btn-group" style="margin-bottom: 10px">
					<my:btn type="insert"></my:btn>
					<my:btn type="update"></my:btn>
					<my:btn type="delete"></my:btn>
				</div>
				<br> <br>
				<table id="userInfoTable">
				</table>
			</div>
		</div>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="height: auto;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" onclick="closemodal()">×</button>
						<p id="title"></p>
					</div>

					<form id="modalForm">
						<!--设置该属性，用于判断是新建还是编辑0：编辑，1新建  -->
						<input type="hidden" name="isNew" class="form-control" id="isNew">
						<div class="modal-body">
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<label for="userId">用户ID</label> <input type="text"
											name="userId" class="form-control">
									</div>
									<div class="form-group">
										<label for="username">用户名</label> <input type="text"
											name="username" class="form-control" id="username">
									</div>
									<div class="form-group" id="passWord">
										<label for="password">密码</label>
										<!-- <input type="password" name="userPassword" class="form-control" id="userPassword" onblur="EncryptPassword()"> -->
										<input type="password" name="password" class="form-control"
											id="password" onblur="EncryptPassword()">
									</div>
									<div class="form-group">
										<label for="mobile">手机号码</label> <input type="text"
											name="mobile" class="form-control" id="mobile">
									</div>
									<div class="form-group">
										<label for="email">邮箱</label> <input type="text" name="email"
											class="form-control" id="email">
									</div>
									<div class="form-group">
										<label for="rolename">用户角色</label>
										<select class="selectpicker form-control"
											data-style="btn-success" id="rolename" name="rolename">
											<option>管理员</option>
											<option>普通用户</option>
										</select>
									</div>
									<div class="form-group">
										<label for="userLocked">用户账号状态</label> <select
											class="selectpicker form-control" data-style="btn-success"
											id="userLocked" name="userLocked">
											<option value="0">正常</option>
											<option value="1">锁定</option>
										</select>
									</div>
									</div>
							</div>
						</div>
						<div class="modal-footer">
							<my:btn type="cancle"></my:btn>
							<my:btn type="save"></my:btn>
						</div>
					</form>
				</div>
			</div>
		</div>
</body>
</html>