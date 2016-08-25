<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>OA登陆</title>

</head>
<body>
<form action="${ctx}/login" method="post">
<table>
<tr>
<td>
用户id：
</td>
<td>
<input name="userid">
</td>
</tr>
<tr>
<td colspan="2">
<input type="submit">
</td>
</tr>
</table>
</form>
</body>
</html>