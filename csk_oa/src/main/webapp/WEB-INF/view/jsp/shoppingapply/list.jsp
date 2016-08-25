<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.3.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/button.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/input_date.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/select.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/div_list.css"/>
<title>购买物品申请列表</title>

</head>
<body>
<style type="text/css">
	.td_top_line{
		border: 1px solid #000;
	}
</style>
<script type="text/javascript">
function getList(){
	//alert(size);
	var applyDate=document.getElementById("applyDate").value;
	var tache=document.getElementById("tache").value;
	var url="${ctx}/${iniMap.classRequestMapping}/tolist?tache="+tache+"&applyDate="+applyDate;
	window.location=url;
}
function view(idValue){
	select(idValue);
	toadd();
}
function toadd(){
	var chkvalue;
	var chkObjs = document.getElementsByName("ids");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	chkvalue = chkObjs[i].value;
        	break;
        }
    }
	//alert(chkvalue);
	var url="${ctx}/${iniMap.classRequestMapping}/toadd";
	if(chkvalue){
		url=url+"?id="+chkvalue;
	}
	
	window.location=url;
}
function todelete(){
	var chkvalue;
	var chkObjs = document.getElementsByName("ids");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	chkvalue = chkObjs[i].value;
        	break;
        }
    }
	//alert(chkvalue);
	if(!chkvalue){
		alert("请选择数据");
		return;
	}
	if(confirm('确定要删除?')){
		
		var par="id="+chkvalue;
		$.post('${ctx}/${iniMap.classRequestMapping}/delete',
				par, 
				function (result)
	            {
					var obj = eval('(' + result + ')');
					if(obj.ok<0){
						alert(obj.msg);
					}
					else{
						var url="${ctx}/${iniMap.classRequestMapping}/tolist";
						window.location=url;
					}
	            });
	}
}

function commit(){
	var chkvalue;
	var chkObjs = document.getElementsByName("ids");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	chkvalue = chkObjs[i].value;
        	break;
        }
    }
	//alert(chkvalue);
	if(!chkvalue){
		alert("请选择数据");
		return;
	}
	var url="${ctx}/${iniMap.classRequestMapping}/commit?id="+chkvalue;
	window.location=url;
}
function select(id){
	document.getElementById(id).checked=true;
}
</script>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<div style="width:100%">
日期：<input  class="input_date" type="date"  id="applyDate" name="applyDate" 
	value='<fmt:formatDate value="${applyDate}" pattern="yyyy-MM-dd"/>' >
状态：<select name="tache" id="tache" class="select_status">
		<c:if test="${tache==-1}"><option value="-1" selected="selected">全部</option></c:if>
		<c:if test="${tache!=-1}"><option value="-1">全部</option></c:if>
		<c:if test="${tache==-0}"><option value="0" selected="selected">未提交</option></c:if>
		<c:if test="${tache!=-0}"><option value="0" >未提交</option></c:if>
		<c:if test="${tache==1}"><option value="1" selected="selected">审核中</option></c:if>
		<c:if test="${tache!=1}"><option value="1" >审核中</option></c:if>
		<c:if test="${tache==2}"><option value="2" selected="selected">已批准</option></c:if>
		<c:if test="${tache!=2}"><option value="2" >已批准</option></c:if>
		<c:if test="${tache==3}"><option value="3" selected="selected">已驳回</option></c:if>
		<c:if test="${tache!=3}"><option value="3" >已驳回</option></c:if>
	  </select>
<br>	  
<button onclick="getList()" >查询</button>
<button onclick="toadd()">新增</button>
<button onclick="toadd()">修改</button>
<button onclick="todelete()">删除</button>
<button onclick="commit()">提交</button>
</div>
<div style="width:100%;overflow: hidden;">

<c:forEach var="apply" items="${list}">

<div>

<div class="div_list" >

<div class="div_list_select" onclick="javascript:select('ids${apply.id}')">
<input type="radio" id="ids${apply.id}" name="ids"  value="${apply.id}" >
<label for="ids${apply.id}">选择</label>
</div>

<div class="div_list_content" onclick="javascript:view('ids${apply.id}');" >
${apply.dept } ${apply.username } ${apply.applyDate }
合计金额：${apply.total } 状态:
<c:if test="${apply.tache==0}">未提交</c:if>
<c:if test="${apply.tache==1}">审核中</c:if>
<c:if test="${apply.tache==2}">已批准</c:if>
<c:if test="${apply.tache==3}">不批准</c:if>
</div>

</div>

</div>

	<%-- <table border="0" style="width:100%" >
	<tr ><td colspan="2"><input type="radio" id="ids${apply.id}" name="ids"  value="${apply.id}"><label for="ids${apply.id}">选择</label></td></tr>
	<tr><td>部门:</td><td>${apply.dept }</td></tr>
	<tr><td>申请日期:</td><td>${apply.applyDate }</td></tr>
	<tr><td>申购人:</td><td>${apply.username }</td></tr>
	<tr><td>仓管员:</td><td>${apply.storeKeeper }</td></tr>
	
	<c:forEach var="mx" items="${apply.detail}">
		<tr ><td class="td_top_line" >物品名称:</td><td class="td_top_line">${mx.name }</td></tr>
		<tr><td class="td_top_line">单价:</td><td class="td_top_line">${mx.price }</td></tr>
		<tr><td class="td_top_line">数量:</td><td class="td_top_line">${mx.number }</td></tr>
		<tr><td class="td_top_line">总额:</td><td class="td_top_line">${mx.price * mx.number }</td></tr>
		<tr ><td class="td_top_line">用途:</td><td class="td_top_line">${mx.purpose }</td></tr>
	</c:forEach>
	<tr><td>合计金额：</td><td>${apply.total }</td></tr>
	
	<tr><td>状态:</td>
	<td>
	<c:if test="${apply.tache==0}">未提交</c:if>
	<c:if test="${apply.tache==1}">审核中</c:if>
	<c:if test="${apply.tache==2}">已批准</c:if>
	<c:if test="${apply.tache==3}">不批准</c:if>
	</td>
	</tr>
	<c:if test="${apply.tache!=0}">
		<tr><td colspan="2">
		<c:if test="${apply.tache==1}">
		<a href="${ctx}/workflow/viewimagepage?id=${apply.id}&type=${iniMap.classEntityName}" target="_blank">查看流程图</a>
		&nbsp;
		</c:if>
		<a href="${ctx}/${iniMap.classRequestMapping}/viewauditlist?id=${apply.id}">查看审核记录</a>
		</td></tr>
	</c:if>
	</table>
	<hr> --%>
</c:forEach>
</div>
</body>
</html>