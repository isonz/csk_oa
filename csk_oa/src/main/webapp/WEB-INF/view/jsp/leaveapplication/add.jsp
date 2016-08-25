<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<title>请假申请</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_submit.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_date.css"/>
<style type="text/css">
.td_title{
	width:80px;
}
</style>
</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<form:form action="${ctx}/leaveapplication/doadd" method="POST" commandName="leaveApplication">

<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="0" style="width:100%">
<tr><td class="td_title">姓名</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td class="td_title">部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td class="td_title">申请日期</td><td>

<form:input path="applyDate" type="date"/>
</td></tr>
<tr><td class="td_title">假期类别</td>
<td><form:select path="type" items="${types}"/>  
</td>
</tr>

<tr><td class="td_title">开始时间</td><td>
<form:input path="part_start_date" type="date" class="input_edit_date"/>
<form:input path="part_start_time" type="time" class="input_edit_time"/> 
</td></tr>
<tr><td class="td_title">结束时间</td><td>
<form:input path="part_end_date" type="date" class="input_edit_date"/> 
<form:input path="part_end_time" type="time" class="input_edit_time"/> 
</td></tr>
<tr><td class="td_title">天数</td><td><form:input path="days"  /> </td></tr>
<tr><td class="td_title">备注(原因)</td><td><form:input path="reason" /></td></tr>
<tr><td colspan="2" align="center">
<input type="submit" value="提交" class="edit_submit"/>
</td></tr>

</table>
</form:form>

</body>
</html>