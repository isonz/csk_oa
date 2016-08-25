<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>部门管理</title>

</head>
<body >
<div >
<table>
<tr>
<td><a href="${ctx}/system/dept/toupdate">新增</a></td>

</tr>
</table>
<table style="width:100%">
<tr>
<td>部门ID</td><td>部门名称</td><td>父部门</td><td>操作</td>

</tr>
<c:forEach items="${list}" var="dept">
<tr>
<td>${dept.id }</td>
<td>${dept.name }</td>
<td>${dept.parent.name}</td>
<td>
	<a href="${ctx}/system/dept/toupdate?id=${dept.id }">修改</a>
	<a href="${ctx}/system/dept/delete?id=${dept.id }">删除</a>
	
</td>
</tr>
</c:forEach>
</table>
</div>
</body>
</html>