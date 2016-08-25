<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<title>加班申请</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_submit.css"/>
</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<form:form action="${ctx}/extraworkapply/doadd" method="POST" commandName="extraWorkApply">

<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="0" style="width:100%">
<tr><td>姓名</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>加班日期</td><td>

<form:input path="date" value="${extraWorkApply.date}" type="date"/>
</td></tr>
<tr><td>加班时间</td><td><form:input path="times"/><form:select path="timesUnit" items="${timesUnitMap}"/>  </td></tr>
<tr><td>加班事由</td><td><form:input path="reason" /></td></tr>
<tr><td>有效日期</td><td><form:input path="validDate" value="${extraWorkApply.validDate}" type="date"/></td></tr>
<tr><td colspan="2" align="center"><input type="submit" value="提交" class="edit_submit"/></td></tr>
</table>

</form:form>

</body>
</html>