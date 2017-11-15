/**
 * by dq 2017年9月14日下午8:00:31
 */
var updatetimeUrl = ctx + "/alarm/updatetime"
var currAlarm = ctx + "/alarm/currentday";
var currIndexAlarm = ctx + "/indexalarm/currentday";
$(function(){
	$.ajax({
		url:updatetimeUrl,
		type:"post",
		async:false,
		success:function(data){
			console.info(data);
		}
	})
});
