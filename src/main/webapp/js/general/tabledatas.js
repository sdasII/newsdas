var healthTableUrl = ctx + "/cell/healthtabl";
$(function() {
	// loading初始化显示
	$(".loading_bk").show();
	$(".loading").show();
	/*
     * 表格
     */
    $.ajax({
                url : healthTableUrl,
                data : {
                    'cellname' : cellname
                },
                type : "POST",
                success : function(data, status) {
                   // var list = data.rows;
                    var temp = eval('(' + data + ')'); 
                    var list = temp.rows;
                    refresh_healthtable(list);
                }
     });
});
function refresh_healthtable(list) {
	var html="<tr style='background-color:#F5F5F6;'><th>时间</th>";
	$.each(list[0].moments,function(i,e){
		html+="<th>"+i+"点</th>";
	});
	html+="</tr>";
	$("#table_list_healthtable").append(html);
	$("#table_list_healthtable").append("<tbody>");
	$.each(list,function(m,n){
		var str="<tr><td>"+n.yyyyMMdd+"</td>";
		$.each(n.moments,function(j,k){
			if(k.result==-1){//查询不到
				str+="<td class='gray'>"+k.ratio+"</td>";
			}else if(k.result==0){//Fault
				str+="<td class='red'>"+k.ratio+"</td>";
			}else if(k.result==1){//Normal
				str+="<td>"+k.ratio+"</td>";
			}else if(k.result==2){//Warning
				str+="<td>"+k.ratio+"</td>";
				//str+="<td class='yellow'>"+k.ratio+"</td>";
			}else if(k.result==3){//Unkonw
				str+="<td class='gray'>"+k.ratio+"</td>";
			}else if(k.result==4){//Error
				str+="<td class='gray'>"+k.ratio+"</td>";
			}
		});
		str+="</tr>";
		$("#table_list_healthtable").append(str);
	});
	$("#table_list_healthtable").append("</tbody>");
	 //loading隐藏
	$("#healthtable_loadbk").hide();
	$("#healthtable_load").hide();
}
function backDetail(code){
	top.$("#iframe_home").attr('src',ctx +"/general/todetail?cell_code="+code);
}