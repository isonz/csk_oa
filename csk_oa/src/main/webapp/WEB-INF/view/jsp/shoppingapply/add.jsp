<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<title>调班调休申请</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_submit.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_date.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/button.css"/>
<script type="text/javascript">
var size = ${fn:length(entity.detail)};

function addMX(){
	$("#addMXAfterFlag").before('<input type="hidden" id="flag_'+size+'" name="detail['+size+'].flag" value="y"/>');
	$("#addMXAfterFlag").before('<tr name="'+"tr_"+size+'"><td colspan="2"><input type="radio" name="ids" id="'+size+'" value="'+size+'"/><label for="'+size+'">选择</label></td></tr>');
	$("#addMXAfterFlag").before('<tr name="'+"tr_"+size+'"><td>物品名称</td><td><input type="text" name="detail['+size+'].name" value="${mx.name }"/> </td></tr>');
	$("#addMXAfterFlag").before('<tr name="'+"tr_"+size+'"><td>单价</td><td><input id="price_'+size+'"  type="number" step="0.01" name="detail['+size+'].price" value="${mx.price }" onblur="calc('+size+')"/></td></tr>');
	$("#addMXAfterFlag").before('<tr name="'+"tr_"+size+'"><td>数量</td><td><input id="number_'+size+'"  type="number" step="1" name="detail['+size+'].number" value="${mx.number }" onblur="calc('+size+')"/></td></tr>');
	$("#addMXAfterFlag").before('<tr name="'+"tr_"+size+'"><td>总额</td><td><input readonly="readonly" type="text" id="total_'+size+'"  value="${mx.price * mx.number }"/></td></tr>');
	$("#addMXAfterFlag").before('<tr name="'+"tr_"+size+'"><td>用途</td><td><input type="text" name="detail['+size+'].purpose" value="${mx.purpose }"/></td></tr>');
	size=size+1;
}
function calc(id){
	var price = document.getElementById('price_'+id).value;
	var number = document.getElementById('number_'+id).value;
	var total = price * number;
	document.getElementById('total_'+id).value= total.toFixed(2);
	
	var all_total=0;
	for(var i=0;i<size;i++){
		var p = document.getElementById('price_'+i);
		if(p){
			price = document.getElementById('price_'+i).value;
			number = document.getElementById('number_'+i).value;
			total = price * number;
			all_total = all_total + total;
		}
	}
	document.getElementById('total').value= all_total.toFixed(2);
}
function deleteMX(){
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
	$("tr[name=tr_"+chkvalue+"]").remove();
	$("#flag_"+chkvalue).remove();
}
function check(){
	var tache="${entity.tache}";
	if(tache==0){
		
		return true;
	}
	alert("已提交的数据不允许修改");
	return false;
}
</script>
</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<c:if test="${entity.tache==0}">
<div style="background-color:gray;width:100%;height: 25px;position: fixed;top: 35;z-index: 99;border-bottom: 1px solid green; padding-bottom: 10px;border-top: 1px solid green;padding-top: 10px; ">
<input style="color:red;margin: 0 30px;" type="button" value="添加物品" onclick="javascript:addMX();"/>
<input style="color:red;margin: 0 30px;" type="button" value="删除物品" onclick="javascript:deleteMX();"/> 
</div>
</c:if>
<div style="height: 50px;">
</div>
<form:form action="${ctx}/${iniMap.classRequestMapping}/doadd" onsubmit="return check()"
	method="POST" modelAttribute="entity">
<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="0" style="width:100%">

<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>申请日期</td><td>
<form:input path="applyDate" type="date" readonly="false"/>
</td></tr>
<tr><td>申购人</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td>仓管员</td><td><form:input path="storeKeeper" readonly="false"/></td></tr>
<tr><td>状态:</td>
	<td>
	<c:if test="${entity.tache==0}">未提交</c:if>
	<c:if test="${entity.tache==1}">审核中</c:if>
	<c:if test="${entity.tache==2}">已批准</c:if>
	<c:if test="${entity.tache==3}">不批准</c:if>
	</td>
	</tr>
	<c:if test="${entity.tache!=0}">
		<tr><td colspan="2">
		<c:if test="${entity.tache==1}">
		<a href="${ctx}/workflow/viewimagepage?id=${entity.id}&type=${iniMap.classEntityName}" target="_blank">查看流程图</a>
		&nbsp;
		</c:if>
		<a href="${ctx}/${iniMap.classRequestMapping}/viewauditlist?id=${entity.id}">查看审核记录</a>
		</td></tr>
	</c:if>
<tr>
<tr id="addMXAfterFlag"><td>合计金额</td><td><form:input path="total" readonly="true"/></td></tr>	
<td colspan="2">

</td>
</tr>
<c:if test="${fn:length(entity.detail)>0}">
<c:forEach var="mx" items="${entity.detail}" varStatus="status">
		<input type="hidden" id="flag_${status.index}" name="detail[${status.index}].flag" value="y"/>
		<tr name="tr_${status.index}"><td colspan="2"><input type="radio" name="ids" id="${status.index}" value="${status.index}"/><label for="${status.index}">选择</label></td></tr>
		<tr name="tr_${status.index}"><td>物品名称</td><td><input type="text" name="detail[${status.index}].name" value="${mx.name }"/> </td></tr>
		<tr name="tr_${status.index}"><td>单价</td><td><input type="number" id="price_${status.index}" step="0.01" name="detail[${status.index}].price" value="${mx.price }" onblur="calc('${status.index}')"/></td></tr>
		<tr name="tr_${status.index}"><td>数量</td><td><input type="number" id="number_${status.index}" step="1" name="detail[${status.index}].number" value="${mx.number }" onblur="calc('${status.index}')"/></td></tr>
		<tr name="tr_${status.index}"><td>总额</td><td><input type="text" id="total_${status.index}"  value="${mx.total }" readonly="readonly"/></td></tr>
		<tr name="tr_${status.index}"><td>用途</td><td><input type="text" name="detail[${status.index}].purpose" value="${mx.purpose }"/></td></tr>
	</c:forEach>
</c:if>



</table>
<c:if test="${entity.tache==0}">
<input type="submit" value="保存" class="edit_submit" />
</c:if>
</form:form>

</body>
</html>