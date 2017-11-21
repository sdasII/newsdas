/**
 * 离线数据导入的js
 */
var initFiles=[];//原始数据文件数组
$(function(){
$("#form1").submit(function(e) {
    $("#progress").css("display", "inline");
    $("#submit").attr("disabled", true);
});
$("#form2").submit(function() {
    $("#progress1").css("display", "inline");
    $("#submit1").attr("disabled", true);
});
$("#file3").change(function(e){
	file_upload(e.target.files);
});
});
function openIframe(type){
	top.$("#offline").attr('src',"/newsdas/log/file/page?type="+type);
}
var upload_url = ctx + "/data/upload";
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
	    			html+="<li><span>"+ e.name+"</span>"
					+'<button class="btn btn-success btn-circle" type="button" id="file_'+i+'" onclick="uploadFile(this)"><i class="fa fa-upload"></i></button>'
					+"<button class='btn btn-danger btn-circle' type='button' id='del_"+i+"' onclick='deleteFile(this)'><i class='fa fa-times'></i></button>"
					+"</li><li><span>上传进度：</span><progress  max='200' >正在上传...</progress></li>";
	    		});
	    		$("#fileList").append(html);
	    	}else{
	    		initFiles.push(file[0]);
	    		var html="<li><span>"+ file[0].name+"</span>"
					+"<button class='btn btn-success btn-circle' type='button' id='file_0' onclick='uploadFile(this)'><i class='fa fa-upload'></i></button>"
					+"<button class='btn btn-danger btn-circle' type='button' id='del_0' onclick='deleteFile(this)'><i class='fa fa-times'></i></button>"
					+"</li><li><span>上传进度：</span><progress  max='200' >正在上传...</progress></li>";
	    		fileSize = file[0].size; 
	    		$("#fileList").append(html);
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
        success : function(data) {
            alert(data);
        },
        error : function(status){
            alert(status)
        }
    });
}

function submit_upload(id,formid){
    var value = $(id).val();
	if($(id).val()==""){
		 alert("请选择文件进行上传");
	}else{
		$(formid).submit();
	}
}