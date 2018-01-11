/**
 * 用户管理js
 */
var api_getPagedList = ctx + '/system/user/list';
var api_insertUrl = ctx + '/system/user/insert';
var api_detail = ctx + '/system/user/one';
var api_deleteSelected = ctx + '/system/user/delete';
$(function(){
	//
	$('#myModal').on('hidden.bs.modal', function() {
        $("#modalForm").data('bootstrapValidator').destroy();
        $('#modalForm').data('bootstrapValidator', null);
        formValidator();
    });
	formValidator();
	//
    $('#userInfoTable').bootstrapTable({
        cache : false,
        striped : true,
        pagination : true,
        toolbar : '#toolbar',
        pageSize : 5,
        pageNumber : 1,
        pageList : [ 5, 10, 20 ],
        clickToSelect : true,
        sidePagination : 'server',// 设置为服务器端分页
        columns : [ 
            { field : "", checkbox : true },
            { field : "userId", title : "用户ID", align : "center", valign : "middle" },
            { field : "username", title : "用户名", align : "center", valign : "middle" },
            { field : 'rolename', title : '角色', align : "center", valign : "middle" },
            { field : "email", title : "邮箱", align : "center", valign : "middle" },
            /*{ field : "birthday", title : "生日", align : "center", valign : "middle" },
            { field : "address", title : "地址", align : "center", valign : "middle" },
            { field : "tel", title : "座机", align : "center", valign : "middle" },
            { field : "mobile", title : "手机", align : "center", valign : "middle" },*/
            { field : 'userLocked', title : '用户账号状态', align : "center", valign : "middle",formatter:function(data,row){
                if(data==0){
                    return "正常";
                }else{
                    return "锁定";
                }
            } },
            { field : "opration", title : "操作", align : "center", valign : "middle",
                formatter : function(value, row, index) {
                    return detailBtn(row.userId);
                }
            } 
        ],
        onPageChange : function(size, number) {
            searchUserInfo();
        },
        formatNoMatches : function() {
            return NOT_FOUND_DATAS;
        }
    });
    
    searchUserInfo();
});
//
function formValidator(){
	$('#modalForm').bootstrapValidator({
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			userId : {
				validators : {
					notEmpty : {
						message : '请输入用户ID'
					},
					regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '用户ID只能包含大写、小写、数字和下划线'
                    }
				}
			},
			username : {
				validators : {
					notEmpty : {
						message : '请输入用户名'
					}
				}
			},
			password : {
				validators : {
					notEmpty : {
						message : '请输入密码'
					},
					stringLength: {
                        min: 6,
                        max: 18,
                        message: '密码长度必须在6到18位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '密码只能包含大写、小写、数字和下划线'
                    }
				}
			},
			mobile : {
				validators : {
					notEmpty : {
						message : '请输入手机号码'
					},
					regexp: {
                        regexp: /^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/,
                        message: '请输入正确的手机号码'
                    }
				}
			},
			email : {
                validators : {
                    notEmpty : {
                        message : '请输入邮箱'
                    },
                    regexp: {
                        regexp: /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/,
                        message: '请输入正确格式的邮箱'
                    }
                }
            },
            rolename : {
				validators : {
					notEmpty : {
						message : '请选择用户角色'
					}
				}
			},
			userLocked : {
				validators : {
					notEmpty : {
						message : '请选择帐号状态'
					}
				}
			}
		}
	}).on('success.form.bv', function(e) {
		e.preventDefault();
        insert();
	});
}
function searchUserInfo() {
    var data = getFormJson("searchForm");//获取查询条件
    commonRowDatas("userInfoTable", data, api_getPagedList, "commonCallback", true);
}
//查看用户详细信息
function checkDetail(userId) {
    var data={
        userId:userId
    };
    doAjax(POST, api_detail, data, checkDetailSuccess)
}
//新增用户
function addNew(){
    $('#title').html('');
    $('#title').append("添加用户");//设置modal的标题
    //$('#userBirthday').val('');
    $("#isNew").val('1');
    $("#btn_save").show();
    $('#myModal').modal({show:true,backdrop: 'static', keyboard: false});
}
// 关闭modal画面
function closemodal() {
    // 关闭前先清空modal画面的form表单
    //$('#modalForm').data('bootstrapValidator').resetForm(true);
    //将hidden项清空
    $("#isNew").val('');
    $("#orgId").val('');
    $("#roleId").val('');
    $('#title').html('');//设置modal的标题
    $("#modalForm #userId").removeAttr("readonly");
    $("#modalForm").resetForm();
    $("#modalForm input").removeAttr("readonly");
    $("#modalForm select").removeAttr("disabled");
    $('#myModal').modal('hide');
}
//插入数据
function insert(){
     var data = getFormJson("modalForm");//获取查询条件
     $.ajax({
        url : api_insertUrl,
        type : 'post',
        data : data,
        success : function(data,status){
            searchUserInfo();
            if($("#isNew").val()=="1"){
            	showOnlyMessage(INFO, "添加成功");
            }else{
            	showOnlyMessage(INFO, "修改成功");
            }
            closemodal();
        }
     })
}
// 点击编辑按钮向后台请求要查询的数据
function editRow() {
    var selectRows = selectedCount("userInfoTable");
    if (selectRows == 0){
        showOnlyMessage(ERROR,$message("ErrorNoSelectEdit",null));
    }else if(selectRows > 1){
        showOnlyMessage(ERROR,$message("ErrorSelectMultiEdit",null));
    }
    else{
        var row = selectedRows("userInfoTable");
        $("#isNew").val('0');
        $("#btn_save").show();
        editDetail(row[0].userId);
    }
}
//编辑用户详细信息
function editDetail(userId) {
    var data={
        userId:userId
    };
    doAjax(POST, api_detail, data, checkDetailSuccess)
}
//查看用户详细信息
function checkDetail(userId) {
    var data={
        userId:userId
    };
    doAjax(POST, api_detail, data, show_Detail)
}
//查看数据
function show_Detail(response){
	  if(response != null && response != undefined)    {
	        $("#modalForm input").attr("readonly","readonly");
	        $("#modalForm select").attr("disabled","disabled");
	        $("#modalForm #userId").val(response.userId);
	        $("#modalForm #password").val(response.password);
	       /* $("#modalForm #address").val(response.address);
	        $("#modalForm #tel").val(response.tel);
	        $("#modalForm #birthday").val(response.birthday);*/
	        $("#modalForm #username").val(response.username);
	        $("#modalForm #email").val(response.email);
	        $("#modalForm #mobile").val(response.mobile);
	        $("#modalForm #rolename").val(response.rolename);
	        $("#modalForm #userLocked").val(response.userLocked);
	        $('#title').html('');
	        $('#title').append("查看用户信息");//设置modal的标题
	        $("#btn_save").hide();
	        $('#myModal').modal({show:true,backdrop: 'static', keyboard: false});
	    }
}
//编辑数据
function checkDetailSuccess(response){
    if(response != null && response != undefined)    {
        $("#modalForm #userId").attr("readonly","readonly");
        $("#modalForm #userId").val(response.userId);
        $("#modalForm #password").val(response.password);
       /* $("#modalForm #address").val(response.address);
        $("#modalForm #tel").val(response.tel);
        $("#modalForm #birthday").val(response.birthday);*/
        $("#modalForm #username").val(response.username);
        $("#modalForm #email").val(response.email);
        $("#modalForm #mobile").val(response.mobile);
        $("#modalForm #rolename").val(response.rolename);
        $("#modalForm #userLocked").val(response.userLocked);
//        $('#modalForm #userLocked').selectpicker('refresh');
        $('#title').html('');
        $('#title').append("编辑用户信息");//设置modal的标题
        $("#btn_save").show();
        //$('#myModal').modal('show');
        $('#myModal').modal({show:true,backdrop: 'static', keyboard: false});
    }
}
//删除一行记录
function deleteRow() {
    var rowCount = selectedCount("userInfoTable");
    if (rowCount > 0) {
        // 获取选中行
        var rows = selectedRows("userInfoTable");
        var rowIds = "";
        for ( var i = 0; i < rows.length; i++) {
            rowIds += rows[i].userId + ",";
        }
        var data = {
            userId:rowIds       
        }
        showConfirm(sureDelete, IF_DELETE_INFO, POST, api_deleteSelected, data, searchUserInfo);
    } else {
        showOnlyMessage(ERROR, $message("ErrorSelectNoDelete", null));
    }

}
function sureDelete(type, url, data, success) {
    doAjax(POST, url, data, success);
    showOnlyMessage(INFO, "删除成功");
}
//将密码用md5加密
function EncryptPassword(){
    Encrypt("password");
}