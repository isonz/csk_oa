<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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

<form:form action="${ctx}/${iniMap.classRequestMapping}/doadd" onsubmit="return check()"
	method="POST" modelAttribute="entity">
<form:hidden path="id"/>
<form:hidden path="userid"/>
<form:hidden path="tache"/>
<table border="0" style="width:100%">

<tr><td>部门</td><td><form:input path="dept" readonly="true"/></td></tr>
<tr><td>申请日期</td><td>
<form:input path="applyDate" type="date" readonly="false"/>
</td></tr>
<tr><td>申购人</td><td><form:input path="username" readonly="true"/></td></tr>
<tr><td>仓管员</td><td><form:input path="storeKeeper" readonly="false"/></td></tr>
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
<tr>
<tr id="addMXAfterFlag"><td>合计金额</td><td><form:input path="total" readonly="true"/></td></tr>	
<td colspan="2">

</td>
</tr>
<c:if test="${fn:length(entity.detail)>0}">
<c:forEach var="mx" items="${entity.detail}" varStatus="status">
		<input type="hidden" id="flag_${status.index}" name="detail[${status.index}].flag" value="y"/>
		<tr ><td colspan="2"><div class="div_title"></div></td></tr>
		<tr name="tr_${status.index}"><td>物品名称</td><td><input type="text" name="detail[${status.index}].name" value="${mx.name }"/> </td></tr>
		<tr name="tr_${status.index}"><td>单价</td><td><input type="number" id="price_${status.index}" step="0.01" name="detail[${status.index}].price" value="${mx.price }" onblur="calc('${status.index}')"/></td></tr>
		<tr name="tr_${status.index}"><td>数量</td><td><input type="number" id="number_${status.index}" step="1" name="detail[${status.index}].number" value="${mx.number }" onblur="calc('${status.index}')"/></td></tr>
		<tr name="tr_${status.index}"><td>总额</td><td><input type="text" id="total_${status.index}"  value="${mx.total }" readonly="readonly"/></td></tr>
		<tr name="tr_${status.index}"><td>用途</td><td><input type="text" name="detail[${status.index}].purpose" value="${mx.purpose }"/></td></tr>
	</c:forEach>
</c:if>
<tr ><td colspan="2"><div class="div_title"></div></td></tr>
</table>
</form:form>

<form action="${ctx}/${iniMap.classRequestMapping}/doaudit" method="post" name="audit" id="audit">
<input type="hidden" name="taskid" id="taskid" value="${taskid}">
<input type="hidden" name="id"  id="id" value="${entity.id}">
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