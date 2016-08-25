function todelete(url){
	if(confirm('确定要删除?')){
		var chkvalue;
		var chkObjs = document.getElementsByName("ids");
		for(var i=0;i<chkObjs.length;i++){
	        if(chkObjs[i].checked){
	        	chkvalue = chkObjs[i].value;
	        	break;
	        }
	    }
		//alert(chkvalue);
		if(!chkvalue){
			alert("请选择数据");
		}
		var par="id="+chkvalue;
		$.post(url,
				par, 
				function (result)
	            {
					var obj = eval('(' + result + ')');
					if(obj.ok<0){
						alert(obj.msg);
					}
					else{
						var url_new=url+"?id="+chkvalue;
						window.location=url_new;
					}
	            });
		
	}
}

function commit(){
	var chkvalue;
	var chkObjs = document.getElementsByName("ids");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	chkvalue = chkObjs[i].value;
        	break;
        }
    }
	//alert(chkvalue);
	if(!chkvalue){
		alert("请选择数据");
	}
	var url="${ctx}/extraworkapply/commit?userid=${userid}&id="+chkvalue;
	window.location=url;
}