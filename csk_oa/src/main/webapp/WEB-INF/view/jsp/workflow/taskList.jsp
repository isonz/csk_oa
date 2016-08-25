<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
<title>待办任务列表</title>

</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<table >
<tr>
	<td>任务ID</td><td>任务名称</td><td>待办人</td><td>操作</td>
</tr>
<c:forEach var="task" items="${list}">
	<td>${task.id }</td>
	<td>${task.name}</td>
	<td>${task.assignee }</td>
	<td><a href="${ctx}/workflow/toaudit?taskid=${task.id }">办理</a>
		<a href="${ctx}/workflow/viewimagepagebytaskid?taskid=${task.id }">查看流程</a>
	</td>
</c:forEach>
</table>
</body>
</html>