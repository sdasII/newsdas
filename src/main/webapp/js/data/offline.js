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
		});
        
        
function openIframe(type){
	top.$("#offline").attr('src',"/newsdas/log/file/page?type="+type);
}

var upload_url = ctx + "/data/uploadfile?type=file";



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

function submit_cal(){
	$("#nettime_error").hide();
	$("#netcaltime_error").hide();
	$("#netpath_error").hide();
	if($("#net_time").val()==""){
		$("#nettime_error").show();
		return;
	}else if($("#net_caltime").val()==""){
		$("#netcaltime_error").show();
		return;
	}else if($("#net_path").val()==""){
		$("#netpath_error").show();
		return;
	}else{
		$("#form1").submit();
	}
}



function formatSubmit(){
    $("#formatFile").submit();
}


