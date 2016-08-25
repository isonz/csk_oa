<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>用户管理</title>
<script type="text/javascript">
function cancel(){
	history.go(-1);
}

</script>
</head>
<body style="width:100%">
<form:form action="${ctx}/system/user/doupdate" commandName="user">
<form:hidden path="id"/>
<form:hidden path="openid"/>
<table style="width:100%">
<tr>
<td>企业号用户id</td><td><form:input path="userid"/></td>
</tr>
<tr>
<td>用户姓名</td><td><form:input path="userName"/></td>
</tr>
<tr>
<td>部门</td><td><form:select path="department.id" items="${deptmap}"></form:select></td>
</tr>
<tr>
<td>角色</td>
<td>
<c:forEach items="${roles}" var="role" varStatus="status">
<span>
<input id="roles${status.index}" name="roles[${status.index}].id" 
type="checkbox" value="${role.id}"/>
<label for="roles${status.index}">${role.name}</label>

</span>
</c:forEach>
</td>
</tr>

<%-- <tr>
<td>是否部门领导</td>
<td>
	 <form:radiobutton path="deptLead" value="true"/>是  
     <form:radiobutton path="deptLead" value="false"/>否  
</td>
</tr>

<tr>
<td>是董事长</td>
<td>
	 <form:radiobutton path="companyLead" value="true"/>是  
     <form:radiobutton path="companyLead" value="false"/>否  
</td>
</tr> --%>

<tr>
<td>是否有效</td>
<td>
	 <form:radiobutton path="valid" value="true"/>是  
     <form:radiobutton path="valid" value="false"/>否  
</td>
</tr>

<tr>
<td colspan="1"><input type="submit" value="保存"></td>
<td><input type="button" value="取消" onclick="cancel();"></td>
</tr>
</table>
</form:form>
<script type="text/javascript">
window.onload=function(){
	var ids="${roleHavedIds}";
	var ids1 = ids.split(",");
	var size = ${fn:length(roles)};
	for(var i=0;i<size;i++){
		var id = "roles"+i;
		var v = document.getElementById(id).value;
		var has =ids1.indexOf(v);
		
		if(has>=0){
			
			document.getElementById(id).setAttribute("checked","checked");
		}
	}
}
</script>
</body>
</html>