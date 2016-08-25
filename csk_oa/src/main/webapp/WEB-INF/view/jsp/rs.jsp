<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>OA入口</title>

</head>
<body>
<table>
<tr>
<td>
<a href="${ctx}/workflow/viewtasklist?userid=${userid}">查看待办任务</a>
</td>
</tr>
<tr>
<td>
人事行政相关申请
</td>
</tr>
<tr>
<td>
<a href="${ctx}/extraworkapply/tolist?userid=${userid}">加班申请</a>
<a href="${ctx}/leaveapplication/tolist">请假申请</a>
</td>
</tr>
<tr>
<td>
物流部相关申请
</td>
</tr>
</table>
</body>
</html>