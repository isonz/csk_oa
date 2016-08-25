<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>角色管理</title>
<script type="text/javascript">
function cancel(){
	history.go(-1);
}
</script>
</head>
<body style="width:100%">
<form:form action="${ctx}/system/role/doupdate" commandName="role">
<form:hidden path="id"/>
<table style="width:100%">
<tr>
<td>角色代码</td><td><form:input path="code"/></td>
</tr>
<tr>
<td>角色名称</td><td><form:input path="name" /></td>
</tr>

<tr>
<td colspan="1"><input type="submit" value="保存"></td>
<td><input type="button" value="取消" onclick="cancel();"></td>
</tr>
</table>
</form:form>

</body>
</html>