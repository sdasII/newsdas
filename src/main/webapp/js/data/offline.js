/**
 * 离线数据导入的js
 */
//websocket
var initFiles=[];//原始数据文件数组
$(function() {
			$("#form1").submit(function(e) {
						$("#submit").attr("disabled", true);
					});
			$("#form2").submit(function() {
						$("#submit1").attr("disabled", true);
					});
			$("#file3").change(function(e) {
						file_upload(e.target.files);
					});
			$("#reset_btn").click(function() {
						$("#nettime_error").hide();
						$("#netcaltime_error").hide();
						$("#netpath_error").hide();
					});
			//datapicker
			
		    $(".form_datetime").datetimepicker({
		    	 format: 'yyyymm',  
		    	 startView: 'year',
		         minView:'year',
		         maxView:'decade',
		         language:  'zh-CN' 
		    });
		  //年月默认值
		    var date=new Date;
		    var year=date.getFullYear(); 
		    var month=date.getMonth();//默认上一个月
		    month =(month<10 ? "0"+month:month); 
		    var mydate = (year.toString()+month.toString());
		    $(".form_datetime").val(mydate);
		    
		    $("input[type='file']").on('change', function(e){
		        var name = e.currentTarget.files[0].name;
		        var filediv=$(e.currentTarget).prev('input[type="file"]').prevObject[0];
		        if($(filediv).attr("id").indexOf("customerfile")>-1){
		        	 var title=$(e.currentTarget).siblings(".upload_title")[1];
				        $(title).html(name);
		        }else{
		        	 var title=$(e.currentTarget).siblings(".upload_title")[0];
				        $(title).html(name);
		        }
		    });
		    //log初始化
		    drawTables('');
		});
        
        
function openIframe(url,title){
	//top.$("#offline").attr('src',"/newsdas/log/file/page?type="+type);
	iframeconvert(url,title);
}

var upload_url = ctx + "/data/uploadfile";



function file_upload(file){
	var ifupload=true;
	if(file.length>3){
		alert("超过上传限制，一次最多上传3个！");
		file.value="";
		ifupload=false;
		return;
	}else{
		if($("#fileList").find("li")!=undefined&&$("#fileList").find("li").length>=6){
			alert("超过上传限制，一次最多上传3个！");
			file.value="";
			ifupload=false;
			return;
		}
		var isIE = /msie/i.test(navigator.userAgent) && !window.opera; 
		var fileSize = 0;         
	    if (isIE && !file) {     
	      var filePath = file.value;
	      var fileSystem = new ActiveXObject("Scripting.FileSystemObject");        
	      var file = fileSystem.GetFile (filePath); 
	      if(file.length>1){
	    		$.each(file,function(i,e){
	    			fileSize += e.size;
	    		});
	    	}else{
	    		fileSize = file.Size;  
	    	}
	    } else {
	    	initFiles=[];
	    	if(file.length>1){
	    		var html="";
	    		$.each(file,function(i,e){
	    			fileSize += e.size;
	    			initFiles.push(e);
	    			html+="<li><form action='"+upload_url+"' method='post'enctype='multipart/form-data'><span>"+ e.name+"</span>"
					+'<button class="btn btn-success btn-circle" type="submit" id="file_'+i+'"><i class="fa fa-upload"></i></button>'
					+"<button class='btn btn-danger btn-circle' type='button' id='del_"+i+"' onclick='deleteFile(this)'><i class='fa fa-times'></i></button>"
					+"<input  type='file' id='file_"+i+"' name='file' value='' style='display:none'>"
					+"</form></li>";//<li><span>上传进度：</span><progress  max='200' >正在上传...</progress></li>
	    			//$("#file_"+i).val(e);
	    		});
	    		$("#fileList").append(html);
	    	}else{
	    		initFiles.push(file[0]);
	    		var html="<li><form action='"+upload_url+"' method='post'enctype='multipart/form-data'><span>"+file[0].name+"</span>"
					+"<button class='btn btn-success btn-circle' type='submit' id='file_0'><i class='fa fa-upload'></i></button>"
					+"<button class='btn btn-danger btn-circle' type='button' id='del_0' onclick='deleteFile(this)'><i class='fa fa-times'></i></button>"
					+"<input  type='file' id='file_0' name='file' value='' style='display:none'>"
					+"</li><li><span>上传进度：</span><progress  max='200' >正在上传...</progress></li>";
	    		
	    		fileSize = file[0].size; 
	    		$("#fileList").append(html);
	    		//$("#file_0").val(file[0]);
	    	}
	     }   
	     var size = fileSize/(1024*1024*1024);    
	     if(size>4){
	      alert("上传文件总大小不能大于4G");
	      file.value="";
	      ifupload=false;
	      return
	     }
	}
}

function deleteFile(obj){
	$(obj).parent().next().remove();
	$(obj).parent().remove();
	var num=$(obj).attr("id").split("_")[1];
	initFiles.splice(num,1); 
}

function uploadFile(obj) {
	var num = $(obj).attr("id").split("_")[1];
	var file = initFiles[num];
	if ($(obj).find("i").attr("class").indexOf("upload") > -1) {
		$(obj).find("i").attr("class", "fa fa-pause");
		var data = {};
		data.file = file;
		data.type = "file";
		// 上传
		/*$.ajax({
					url : upload_url,
					data : data,
					type : 'post',
					success : function(data) {
					}
				});*/
        upload(file);
	} else if ($(obj).find("i").attr("class").indexOf("pause") > -1) {
		$(obj).find("i").attr("class", "fa fa-play");
		// 暂停
		var data = {};
		data.file = file;
		data.type = "file";
		/*$.ajax({
					url : upload_url,
					data : data,
					type : 'post',
					success : function(data) {
					}
				});*/
        upload(file);
	}
}

function upload(f){
    data = {};
    data.file = f;
    data.type = "file";
    $.ajax({
        url : upload_url,
        data : data,
        type : 'post',
        processData:false,
        mimeType:"multipart/form-data",
        contentType:false,
        success : function(data) {
            alert(data);
        },
        error : function(status){
            alert(status)
        }
    });
}

var upload_progress_url = ctx + "/data/uploadstatus";
var polingCount=0;
function longPoling(){
    $.ajax({
        url:upload_progress_url,
        type:'get',
        timeout: 5000,
        success:function(data,status){
            var progress = eval("("+data+")");
            if(progress.success){
                progress = progress.progress;
                $("#progress2").attr("value",progress);
                var value = progress+"%";
                $("#progressvalue2").text(value);
                if(progress!=100){
                    longPoling();
                }
            }else{
                longPoling();
            }
        },
        error:function(xhr,status,exception){
            polingCount++;
            if(status=="timeout"){//请求超时
                if(polingCount<10){//失败十次请求结束
                    longPoling();
                }
                
            }else{// 其他错误，如网络错误等
                if(polingCount<10){
                    longPoling();
                }
                
            }
        }
    });
}
/**
 * 文件上传
 * @param {} id
 * @param {} formid
 */
function submit_upload(id,formid){
	if($(id).val()==""){
		 showOnlyMessage(ERROR, "请选择文件！");
	}else{
		$(formid).submit();
        if(formid == "#form1"){
            $("#network_load").css("display","inline");
        }else if(formid == "#form2"){
            $("#capacity_load").css("display","inline");
        }  
	}
}
function complainSumit(element) {
	var file1 = $("#comlainfile").val();
	var file2 = $("#customerfile").val();
	var complaintimes=$("#complaintime").val();
	//var length = $("#comlainfile").files
	if (complaintimes != "" &&file1 != "" && file2 != "") {
		$(element).ajaxSubmit(function(message) {
			$("#complaint_load").css("display", "none");
			var status = message.success;
			if (status.indexOf("成功") > 0) {
				showOnlyMessage(INFO, status);
			} else {
				showOnlyMessage(ERROR, status);
			}

		});
		$("#complaint_load").css("display", "inline");
	}else if (complaintimes == "") {
		showOnlyMessage(ERROR, "请选择时间！");
	} else if (file1 == "") {
		showOnlyMessage(ERROR, "请选择常驻小区文件！");
	} else if (file2 == "") {
		showOnlyMessage(ERROR, "请选择投诉情况文件！");
	}
	return false;
}
/*
 * 单个csv网管文件导入
 */
function signalCSVSumit(element) {
	var file = $("#file3").val();
	var times=$("#nettest_time").val();
	if (times != "" &&file != "") {
		$(element).ajaxSubmit(function(message) {
            $("#csv_load").css("display", "none");
			var status = message.success;
			if (status.indexOf("成功") > 0) {
				showOnlyMessage(INFO, status);
			} else {
				showOnlyMessage(ERROR, status);
			}
		});
		$("#csv_load").css("display", "inline");
	}else if (times == "") {
		showOnlyMessage(ERROR, "请选择时间！");
	} else if (file == "") {
		showOnlyMessage(ERROR, "请选择文件！");
	}
	return false;
}
/*
 * 单个网管csv文件分析
 */
function submit_cal(){
	var time = $("#net_caltime").val();
	if(time==""){//默认为上一个月
		var date=new Date;
		var year=date.getFullYear(); 
		var month=date.getMonth();
		month =(month<10 ? "0"+month:month);
		time=(year.toString()+month.toString());
	}
    var filetime = $("#nettest_time").val();
    var data = {};
    data.modeltime = time;
    data.filetime = filetime;
    var strurl  = ctx + "/data/csvStatistic";
    $("#csv_load").css("display", "line");
    $.ajax({
      url : strurl,
      type : "post",
      data : data,
      success : function(data,success){
           $("#csv_load").css("display", "none");
           showOnlyMessage(INFO, "计算完成！");
      }
    });
}
function netzipSumit(element) {
	var file = $("#originfile").val();
	var times=$("#origintime").val();
	if (times != "" &&file != "") {
        $("#originsubmit").attr("disabled", true);
           $("#span_progress").css("display", "inline");
                $("#progress2").attr("value", 0);
                $("#progressvalue2").text("0%");
		$(element).ajaxSubmit(function(message) {
					var msg = message;
					var fileStatus = msg.status;
					$("#originsubmit").attr("disabled", false);
					if (fileStatus.indexOf("失败") >= 0) {
						showOnlyMessage(ERROR, fileStatus);
						$("#originsubmit").val("续传");
					} else if (fileStatus.indexOf("成功") >= 0) {
						showOnlyMessage(INFO, fileStatus);
						// $("#originfile").val("");
						$("#originsubmit").val("上传");
					} else {
						showOnlyMessage("warning", fileStatus);
					}

				});
         ws.send("start");
	}else if (times == "") {
		showOnlyMessage(ERROR, "请选择时间！");
	} else if (file == "") {
		showOnlyMessage(ERROR, "请选择文件！");
	}
	return false;
}
/*
 * 网关数据分析 
 */
function submit_calzip(){
	var time = $("#net_caltime2").val();
	if(time==""){//默认为上一个月
		var date=new Date;
		var year=date.getFullYear(); 
		var month=date.getMonth();
		month =(month<10 ? "0"+month:month);
		time=(year.toString()+month.toString());
	}
    var filetime = $("#origintime").val();
    var data = {};
    data.modeltime = time;
    data.filetime = filetime;
    var strurl  = ctx + "/data/zipStatistic";
    $("#zip_load").css("display", "line");
    $.ajax({
      url : strurl,
      type : "post",
      data : data,
      success : function(data,success){
           $("#zip_load").css("display", "none");
           showOnlyMessage(INFO, "计算完成！");
      }
    });
}

/*
 * 网管数据模式计算
 */
function submit_modelzip(){
    var time = $("#net_caltime2").val();
    if(time==""){//默认为上一个月
        var date=new Date;
        var year=date.getFullYear(); 
        var month=date.getMonth();
        month =(month<10 ? "0"+month:month);
        time=(year.toString()+month.toString());
    }
    var data = {};
    data.modeltime = time;
    var strurl  = ctx + "/data/modelCalculate";
    $("#zip_load").css("display", "line");
    $.ajax({
      url : strurl,
      type : "post",
      data : data,
      success : function(data,success){
           $("#zip_load").css("display", "none");
           showOnlyMessage(INFO, "计算完成！");
      }
    });
}

function formatSubmit(){
    $("#formatFile").submit();
}
/**
 * 历史数据列表
 */
function drawTables(type){
	 $('#historyTable').bootstrapTable({
		 cache : false,
	        striped : true,
	        pagination : true,
	        toolbar : '#pager_Table',
	        pageSize : 10,
	        pageNumber : 1,
	        pageList : [ 5, 10, 20 ],
	        clickToSelect : true,
	        sidePagination : 'server',// 设置为服务器端分页
	        columns: [{
	              field: 'starttime',
	              title: '开始时间',
	              width:500,
	              formatter:function(value,row,index){
	                  var jsDate = new Date(value);
	                  var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
	                   return UnixTimeToDate;
	                 }
	          },{
	              field: 'endtime',
	              title: '结束时间',
	              width:500,
	              formatter:function(value,row,index){
	                  var jsDate = new Date(value);
	                  var UnixTimeToDate = jsDate.getFullYear() + '/' + (jsDate.getMonth() + 1) + '/'+jsDate.getDate()+ ' ' + jsDate.getHours() + ':' + jsDate.getMinutes() + ':' + jsDate.getSeconds();
	                   return UnixTimeToDate;
	                 }
	          },{
                  field: 'alltime',
                  title: '总消耗时间(ms)',
                  width:500
              },{
	              field: 'filename',
	              title: '信息',
	              width:500
	          },{
	              field: 'type',
	              title: '类型',
	              width:500
	          },{
	              field: 'result',
	              title: '状态',
	              width:500,
	              formatter:function(value,row,index){
	                 if(value=="1"){
	                	 return "成功";
	                 }else{
	                	 return "失败";
	                 }
	                 }
	          }],
	        onPageChange : function(size, number) {
	        	var data = {};
	    	 	data.type=type;
	    	    commonRowDatas("historyTable", data, "/newsdas/log/file/list", "commonCallback", true);
	        },
	        formatNoMatches : function() {
	            return NOT_FOUND_DATAS;
	        }
	    });
	 	var data = {};
	 	data.type=type;
	    commonRowDatas("historyTable", data, "/newsdas/log/file/list", "commonCallback", true);
}
function search_log(){
	var data = {};
 	data.type=$("#type").val();
 	data.result=$("#status").val();
    commonRowDatas("historyTable", data, "/newsdas/log/file/list", "commonCallback", true);
}
/*
 * 工单验证
 */
var validate_url = ctx + "/work/validate"
function workOrderValidate(){
    $.ajax({
        url : validate_url,
        type : "get",
        success:function(data,status){
            var state = data.success;
            //TODO state = true
            
            //TODO state = false
        }
    })
}