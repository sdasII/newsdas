/**
 * by ly 2017年10月31日下午14:00:31
 */
var url1 = ctx + '/timer/section';
function section(){
	$("#Source_error").hide();
	$("#files_error").hide();
	if($("#Source").val()==""){
		$("#Source_error").show();
	}else if($("#files").val()==""){
		$("#files_error").show();
	}else{
		var Source=$("#Source").val();
		var files=$("#files").val();
		var ifDel=$("input[name='deleteOut']:checked").val();
		$.ajax({
			url:url1,
			data:{
				"source":Source,
				"files":files,
				"ifdel":ifDel
			},
			daraType:"post",
			success:function(data){
				alert("切片成功！");
				//还原
				$("#Source").val("");
				$("#Output").val("");
				$(":radio[name='deleteOut'][value='1']").prop("checked", "checked");
			}
		});
	}
}
function calculate(id,url,areaId){
	$(id).parent().find(".error_msg").hide();
	var time=$(id).val();
	if($(id).val()==""){
		$(id).parent().find(".error_msg").show();
	}else{
		$.ajax({
			url:url,
			data:{
				"time":time
			},
			daraType:"post",
			success:function(data){
				alert("计算成功！");
			}
		});
	}
}