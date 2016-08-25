<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
<title>调班调休申请审核</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/div_title.css"/>
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
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<div class="div_title">申请信息</div>

<form:form method="POST" commandName="compTimeApply">
<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="0" style="width:100%">
<tr><td>姓名</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>申请日期</td><td>
<form:input path="applyDate" type="date" readonly="true"/>
</td></tr>

<tr><td>原上班时间</td><td><form:input path="startDate1" type="date" readonly="true"/> </td></tr>
<tr><td>调班时间</td><td><form:input path="endDate1" type="date" readonly="true"/> </td></tr>
<tr><td>原上班时间</td><td><form:input path="startDate2" type="date" readonly="true"/> </td></tr>
<tr><td>调班时间</td><td><form:input path="endDate2" type="date" readonly="true"/> </td></tr>
<tr><td>备注（原因）</td><td><form:input path="reason"  readonly="true"/></td></tr>
</table>
</form:form>

<form action="${ctx}/comptimeapply/doaudit" method="post" name="audit" id="audit">
<input type="hidden" name="taskid" id="taskid" value="${taskid}">
<input type="hidden" name="id"  id="id" value="${compTimeApply.id}">
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

<div class="div_title">审核历史</div>
<table  border="0" style="width:100%">
<tr><td>审核人</td><td>审核日期</td><td>意见</td><td>批注</td></tr>
<c:forEach items="${list}" var="vo">

	<tr><td>${vo.userName}</td><td><fmt:formatDate value="${vo.date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	<td>${vo.type}</td><td>${vo.message}</td></tr>
</c:forEach>
</table>

</body>
</html>