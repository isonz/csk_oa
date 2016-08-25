<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/button.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_date.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/select.css"/>
<title>调班调休申请列表</title>

</head>
<body>
<script type="text/javascript">
function getList(){
	//alert(size);
	var applyDate=document.getElementById("applyDate").value;
	var tache=document.getElementById("tache").value;
	var url="${ctx}/comptimeapply/tolist?tache="+tache+"&applyDate="+applyDate;
	window.location=url;
}
function toadd(){
	var chkvalue;
	var chkObjs = document.getElementsByName("ids");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	chkvalue = chkObjs[i].value;
        	break;
        }
    }
	//alert(chkvalue);
	var url="${ctx}/comptimeapply/toadd";
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
		$.post('${ctx}/comptimeapply/delete',
				par, 
				function (result)
	            {
					var obj = eval('(' + result + ')');
					if(obj.ok<0){
						alert(obj.msg);
					}
					else{
						var url="${ctx}/comptimeapply/tolist";
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
	//alert(chkvalue);
	if(!chkvalue){
		alert("请选择数据");
		return;
	}
	var url="${ctx}/comptimeapply/commit?id="+chkvalue;
	window.location=url;
}
</script>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<div style="width:100%">
日期：<input  class="input_date" type="date"  id="applyDate" name="applyDate" 
	value='<fmt:formatDate value="${applyDate}" pattern="yyyy-MM-dd"/>' >
状态：<select name="tache" id="tache" class="select_status">
		<option value="-1">全部</option>
		<option value="0">未提交</option>
		<option value="1">审核中</option>
		<option value="2">已批准</option>
		<option value="3">已驳回</option>
	  </select>
<br>	  
<button onclick="getList()" >查询</button>
<button onclick="toadd()">新增</button>
<button onclick="toadd()">修改</button>
<button onclick="todelete()">删除</button>
<button onclick="commit()">提交</button>
</div>
<div style="width:100%;overflow: hidden;">
<hr>
<c:forEach var="apply" items="${list}">
	<table border="0" style="width:100%">
	<tr ><td colspan="2"><input type="radio" id="ids" name="ids"  value="${apply.id}">选择</td></tr>
	<tr><td>申请日期:</td><td>${apply.applyDate }</td></tr>
	<tr><td>姓名:</td><td>${apply.username }</td></tr>
	<tr><td>部门:</td><td>${apply.dept }</td></tr>
	
	<tr><td>原时间:</td><td>${apply.startDate1}</td></tr>
	<tr><td>调班时间:</td><td>${apply.endDate1}</td></tr>
	<tr><td>原时间:</td><td>${apply.startDate2}</td></tr>
	<tr><td>调班时间:</td><td>${apply.endDate2}</td></tr>
	<tr><td>备注（原因）:</td><td>${apply.reason }</td></tr>
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
		<a href="${ctx}/workflow/viewimagepage?id=${apply.id}&type=CompTimeApply" target="_blank">查看流程图</a>
		&nbsp;
		</c:if>
		<a href="${ctx}/comptimeapply/viewauditlist?id=${apply.id}">查看审核记录</a>
		</td></tr>
	</c:if>
	</table>
	<hr>
</c:forEach>
</div>
</body>
</html>