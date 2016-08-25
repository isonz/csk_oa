<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<title>用人需求申请表</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_submit.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_date.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/button.css"/>
<script type="text/javascript">


function check(){
	var tache="${entity.tache}";
	if(tache==0){
		
		return true;
	}
	alert("已提交的数据不允许修改");
	return false;
}
</script>
</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>

<form:form action="${ctx}/${iniMap.classRequestMapping}/doadd" onsubmit="return check()"
	method="POST" modelAttribute="entity">
<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="0" style="width:100%">
<tr><td colspan="2">申请职位</td></tr>
<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>申请日期</td><td>
<form:input path="applyDate" type="date" readonly="false"/>
</td></tr>
<tr><td>职位名称</td><td><form:input path="postName" readonly="false"/></td></tr>
<tr><td>需求人数</td><td><form:input path="personNum" type="number"/></td></tr>
<tr><td>现有人数</td><td><form:input path="havePersonNum" type="number"/></td></tr>
<tr><td>期望到岗日期</td><td><form:input path="arrivalDate" type="date"/></td></tr>
<tr><td colspan="2">申请理由</td></tr>
<tr>
	<td>类别</td>
	<td><form:select path="type" items="${typeMap}"></form:select></td>
</tr>
<tr><td>理由</td><td><form:input path="applyReason" readonly="false"/></td></tr>
<tr><td colspan="2">主要工作职责</td></tr>
<tr><td colspan="2"><form:input path="duties" readonly="false"/></td></tr>
<tr><td colspan="2">任职条件</td></tr>
<tr><td>专业知识及技能要求</td><td><form:input path="skill" readonly="false"/></td></tr>
<tr><td>工作经验要求</td><td><form:input path="experience" readonly="false"/></td></tr>
<tr><td>能力素质要求</td><td><form:input path="capacity" readonly="false"/></td></tr>
<tr><td>状态:</td>
	<td>
	<c:if test="${entity.tache==0}">未提交</c:if>
	<c:if test="${entity.tache==1}">审核中</c:if>
	<c:if test="${entity.tache==2}">已批准</c:if>
	<c:if test="${entity.tache==3}">不批准</c:if>
	</td>
</tr>
<c:if test="${entity.tache!=0}">
	<tr><td colspan="2">
	<c:if test="${entity.tache==1}">
	<a href="${ctx}/workflow/viewimagepage?id=${entity.id}&type=${iniMap.classEntityName}" target="_blank">查看流程图</a>
	&nbsp;
	</c:if>
	<a href="${ctx}/${iniMap.classRequestMapping}/viewauditlist?id=${entity.id}">查看审核记录</a>
	</td></tr>
</c:if>
<c:if test="${entity.tache==2}">
<tr><td colspan="2">实际录用和到位情况（人事行政部填写）</td></tr>
<tr><td colspan="2">内容</td></tr>
<tr><td colspan="2"><form:input path="feedback" readonly="true"/></td></tr>
<tr><td>日期</td><td><form:input path="feedbackDate" type="date" readonly="true"/></td></tr>
</c:if>


</table>
<c:if test="${entity.tache==0}">
<input type="submit" value="保存" class="edit_submit" />
</c:if>
</form:form>

</body>
</html>