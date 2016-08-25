<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>用户管理</title>

</head>
<body >
<div >
<table>
<tr>
<td><a href="${ctx}/system/user/toupdate">新增</a></td>
<%-- <td><a href="${ctx}/system/wx/updatedeptuser">更新通讯录(初始化)</a></td> --%>
</tr>
</table>
<table style="width:100%">
<tr>
<td>用户ID</td><td>用户名称</td><td>是否有效</td><td>微信企业号用户id</td><td>部门名称</td><td>角色</td><td>操作</td>
</tr>
<c:forEach items="${list}" var="user">
<tr>
<td>${user.id }</td>
<td>${user.userName }</td>
<td>
<c:if test="${user.valid==true}">是</c:if>
<c:if test="${user.valid==false}">否</c:if>
</td>
<td>${user.userid }</td>
<td>${user.department.name }</td>
<td>
<c:forEach items="${user.roles }" var="role">
${role.name}
</c:forEach>

</td>
<td>
	<a href="${ctx}/system/user/toupdate?id=${user.id }">修改</a>
	<a href="${ctx}/system/user/delete?id=${user.id }">删除</a>
</td>
</tr>
</c:forEach>
</table>
</div>
</body>
</html>