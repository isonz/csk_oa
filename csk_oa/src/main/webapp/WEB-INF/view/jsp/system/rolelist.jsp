<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>角色管理</title>

</head>
<body >
<div >
<table>
<tr>
<td><a href="${ctx}/system/role/toupdate">新增</a></td>
</tr>
</table>
<table style="width:100%">
<tr>
<td>角色ID</td><td>角色代码</td><td>角色名称</td><td>操作</td>
</tr>
<c:forEach items="${list}" var="role">
<tr>
<td>${role.id }</td>
<td>${role.code }</td>
<td>${role.name }</td>
<td>
	<a href="${ctx}/system/role/toupdate?id=${role.id }">修改</a>
	<a href="${ctx}/system/role/delete?id=${role.id }">删除</a>
</td>
</tr>
</c:forEach>
</table>
</div>
</body>
</html>