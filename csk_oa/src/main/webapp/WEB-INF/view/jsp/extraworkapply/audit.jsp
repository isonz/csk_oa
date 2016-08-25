<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
<title>加班申请审核</title>
<script type="text/javascript">
function toaudit(auditstatus){
	var form = document.getElementById("audit");
	document.getElementById("auditstatus").value=auditstatus;
	//url=url+"?taskid="+taskid+"&userid="+userid+"&id="+id+"&message="+message+"&auditstatus="+auditstatus;
	form.submit();   
}
</script>
</head>
<body>
申请信息


<form:form method="POST" commandName="extraWorkApply">
<form:hidden path="id"/>
<form:hidden path="userid"/>
<table border="0" style="width:100%">
<tr><td>姓名</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>加班日期</td><td><form:input path="date" readonly="true"/></td></tr>
<tr><td>加班时间</td><td><form:input path="times" readonly="true"/><form:select path="timesUnit" items="${timesUnitMap}" disabled="true"/>  </td></tr>
<tr><td>加班事由</td><td><form:input path="reason" readonly="true"/></td></tr>
<tr><td>有效期</td><td>
<form:input path="validDate" readonly="true"/>
</td></tr>
</table>
</form:form>

<form action="${ctx}/extraworkapply/doaudit" method="post" name="audit" id="audit">
<input type="hidden" name="taskid" id="taskid" value="${taskid}">
<input type="hidden" name="id"  id="id" value="${extraWorkApply.id}">
<input type="hidden" name="auditstatus" id="auditstatus" >
<table>
<tr>
<td>批注</td>
<td>
<input type="text" name="message" id="message"/>
</td>
</tr>
</table>
</form>

<table>
<tr>
<c:forEach items="${buttons}" var="bu">
	<td>
	<input type="button" value="${bu}" onclick="javascript:toaudit('${bu}');">
	</td>
</c:forEach>
</tr>
</table>
<br>
<hr>

审核历史
<table  border="0" style="width:100%">
<tr><td>审核人</td><td>审核日期</td><td>意见</td><td>批注</td></tr>
<c:forEach items="${list}" var="vo">

	<tr><td>${vo.userName}</td><td><fmt:formatDate value="${vo.date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	<td>${vo.type}</td><td>${vo.message}</td></tr>
</c:forEach>
</table>

</body>
</html>