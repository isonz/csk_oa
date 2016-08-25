<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/button.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_date.css"/>
<title>加班申请列表</title>


</head>
<body>
<script type="text/javascript">
function getList(tache){
	//var size = document.getElementById("size").value;
	//var page = document.getElementById("page").value;
	var url="${ctx}/extraworkapply/tolist?userid=${userid}&tache="+tache;//+"&size="+size+"&page="+page+"&t="+new Date();
	window.location=url;
}
function toadd(){
	var url="${ctx}/extraworkapply/toadd";
	window.location=url;
}
function toupdate(){
	var chkvalue;
	var chkObjs = document.getElementsByName("ids");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	chkvalue = chkObjs[i].value;
        	break;
        }
    }
	var url="${ctx}/extraworkapply/toadd";
	if(chkvalue){
		url=url+"?id="+chkvalue;
	}
	window.location=url;
}
function todelete(){
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
		return;
	}
	if(confirm('确定要删除?')){
		
		var par="id="+chkvalue;
		$.post('${ctx}/extraworkapply/delete',
				par, 
				function (result)
	            {
					var obj = eval('(' + result + ')');
					if(obj.ok<0){
						alert(obj.msg);
					}
					else{
						var url="${ctx}/extraworkapply/delete?id="+chkvalue;
						window.location=url;
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
	if(!chkvalue){
		alert("请选择数据");
		return;
	}
	var url="${ctx}/extraworkapply/commit?userid=${userid}&id="+chkvalue;
	window.location=url;
}
</script>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<div style="width:100%">
<button onclick="toadd()">新增</button>
<button onclick="toupdate()">修改</button>
<button onclick="todelete()">删除</button>
<button onclick="getList(1)" >未审核完</button>
<button onclick="getList(2)">已审核完</button>

<button onclick="commit()">提交</button>
</div>

<div style="width:100%;overflow: hidden;">
<hr>
<c:forEach var="apply" items="${list.content}">
	<table border="0" style="width:100%">
	<tr ><td colspan="2"><input type="radio" id="ids" name="ids"  value="${apply.id}">选择</td></tr>
	<tr><td>姓名:</td><td>${apply.username }</td></tr>
	<tr><td>部门:</td><td>${apply.dept }</td></tr>
	<tr><td>加班日期:</td><td>${apply.date }</td></tr>
	<tr><td>加班时间:</td><td>${apply.times}${apply.timesUnit}</td></tr>
	<tr><td>加班事由:</td><td>${apply.reason }</td></tr>
	<tr><td>有效期:</td><td>${apply.validDate }</td></tr>
	<tr><td>状态:</td>
	<td>
	<c:if test="${apply.tache==0}">未提交</c:if>
	<c:if test="${apply.tache==1}">审核中</c:if>
	<c:if test="${apply.tache==2}">已批准</c:if>
	<c:if test="${apply.tache==3}">不批准</c:if>
	</td>
	</tr>
	<c:if test="${apply.tache!=0}">
		<tr><td colspan="2">
		<c:if test="${apply.tache==1}">
		<a href="${ctx}/workflow/viewimagepage?id=${apply.id}&type=ExtraWorkApply" target="_blank">查看流程图</a>
		&nbsp;
		</c:if>
		<a href="${ctx}/extraworkapply/viewauditlist?id=${apply.id}">查看审核记录</a>
		</td></tr>
	</c:if>
	</table>
	<hr>
</c:forEach>
</div>
<%-- 当前第<input id="page" name="page" value=${page}>页，共${list.totalPages}条记录，每页<input id="size" name="size" value=${size}>条记录
 --%>
</body>
</html>