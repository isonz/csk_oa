<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
<title>加班申请审核记录</title>

</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
申请信息
<hr>
<table border="0" style="width:100%">
<form:form method="POST" commandName="extraWorkApply">
<form:hidden path="id"/>
<form:hidden path="userid"/>
<table border="0" style="width:100%">
<tr><td>姓名</td><td><form:input path="username" readonly="true"/></td></tr>

<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>

<tr><td>加班日期</td><td><form:input path="date" readonly="true"/></td></tr>

<tr><td>加班时间</td><td>
<form:input path="times" readonly="true"/>
<form:select disabled="true" path="timesUnit" items="${timesUnitMap}"/>  
</td></tr>

<tr><td>加班事由</td><td><form:input path="reason" readonly="true"/></td></tr>

<tr><td>有效期</td><td>
<form:input path="validDate" readonly="true"/>
</td></tr>

</table>
</form:form>
</table>

<br>
<hr>
审核历史
<hr>
<table  border="0" style="width:100%">
<tr><td>审核人</td><td>审核日期</td><td>意见</td><td>批注</td></tr>
<c:forEach items="${list}" var="vo">
	<tr><td>${vo.userName}</td><td><fmt:formatDate value="${vo.date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	<td>${vo.type}</td><td>${vo.message}</td></tr>
</c:forEach>
</table>

</body>
</html>