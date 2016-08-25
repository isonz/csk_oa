<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<title>调班调休申请</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_submit.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_date.css"/>
</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<form:form action="${ctx}/comptimeapply/doadd" method="POST" commandName="compTimeApply">

<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="0" style="width:100%">
<tr><td>姓名</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>申请日期</td><td>
<form:input path="applyDate" type="date" readonly="false"/>
</td></tr>

<tr><td>原上班时间</td><td><form:input path="startDate1" type="date"/> </td></tr>
<tr><td>调班时间</td><td><form:input path="endDate1" type="date"/> </td></tr>
<tr><td>原上班时间</td><td><form:input path="startDate2" type="date"/> </td></tr>
<tr><td>调班时间</td><td><form:input path="endDate2" type="date"/> </td></tr>
<tr><td>备注（原因）</td><td><form:input path="reason" /></td></tr>
</table>
<input type="submit" value="保存" class="edit_submit"/>
</form:form>

</body>
</html>