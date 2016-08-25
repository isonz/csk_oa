<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
<title>请假申请审核</title>
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


<form:form method="POST" commandName="leaveApplication">
<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="1" style="width:100%">
<tr><td>姓名</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>申请日期</td><td><form:input path="applyDate" readonly="true"/></td></tr>
<tr><td>假期类别</td><td><form:input path="type" readonly="true"/></td></tr>
<tr><td>开始时间</td><td><form:input path="startDate" readonly="true"/> </td></tr>
<tr><td>结束时间</td><td><form:input path="endDate" readonly="true"/>  </td></tr>
<tr><td>天数</td><td><form:input path="days" readonly="true"/>  </td></tr>
<tr><td>备注（原因）</td><td><form:input path="reason" readonly="true"/></td></tr>

</table>
</form:form>

<table>
<tr>
<td>批注</td>
<td>
<form action="${ctx}/leaveapplication/doaudit" method="post" name="audit" id="audit">
<input type="hidden" name="taskid" id="taskid" value="${taskid}">
<%-- <input type="hidden" name="userid" id="userid" value="${userid}"> --%>
<input type="hidden" name="id"  id="id" value="${leaveApplication.id}">
<input type="hidden" name="auditstatus" id="auditstatus" >
<input type="text" name="message" id="message" value=""/>
</form>
</td>
</tr>
</table>
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
<table  border="1" style="width:100%">
<tr><td>审核人</td><td>审核日期</td><td>意见</td><td>批注</td></tr>
<c:forEach items="${list}" var="vo">

	<tr><td>${vo.userName}</td><td><fmt:formatDate value="${vo.date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	<td>${vo.type}</td><td>${vo.message}</td></tr>
</c:forEach>
</table>

</body>
</html>