<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>部门管理</title>
<script type="text/javascript">
function cancel(){
	history.go(-1);
}
</script>
</head>
<body style="width:100%">
<form:form action="${ctx}/system/dept/doupdate" commandName="department">
<table style="width:100%">
<tr>
<td>部门名称</td><td><form:input path="name"/></td>
</tr>
<tr>
<td>父部门</td>
<td><form:select path="parent.id" >
		<option value="">请选择</option>  
		<c:forEach items="${deptmap}" var="item">
		<form:option value="${item.key}">${item.value}</form:option>
		</c:forEach>
	</form:select></td>
</tr>
<tr>
<td colspan="1"><input type="submit" value="保存"></td>
<td><input type="button" value="取消" onclick="cancel();"></td>
</tr>
</table>
</form:form>

</body>
</html>