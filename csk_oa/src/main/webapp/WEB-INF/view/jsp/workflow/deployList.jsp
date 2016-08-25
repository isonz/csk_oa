<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>流程部署列表</title>
<!-- <style type="text/css">
	table td{
		background: "#000000";
	}
	table{
		border-collapse:collapse;//表格单元格间距样式
		border:1px solid #F00;
	}
	td{border:1px solid #F00;}
	tr{border:0px;}
</style> -->
<style>  

body {  
width: 100%;  
margin: 20px auto;  
 /*  font-family: 'trebuchet MS', 'Lucida sans', Arial;   */
font-size: 14px;  
color: #444;  
}   
  
table {  
*border-collapse: collapse; /* IE7 and lower */  
border-spacing: 0;  
width: 100%;  
}  
  
.bordered {  
border: solid #ccc 1px;  
-moz-border-radius: 6px;  
-webkit-border-radius: 6px;  
border-radius: 6px;  
-webkit-box-shadow: 0 1px 1px #ccc;  
-moz-box-shadow: 0 1px 1px #ccc;  
box-shadow: 0 1px 1px #ccc;  
}  
  
.bordered tr:hover {  
background: #fbf8e9;  
-o-transition: all 0.1s ease-in-out;  
-webkit-transition: all 0.1s ease-in-out;  
-moz-transition: all 0.1s ease-in-out;  
-ms-transition: all 0.1s ease-in-out;  
transition: all 0.1s ease-in-out;  
}  
  
.bordered td, .bordered th {  
border-left: 1px solid #ccc;  
border-top: 1px solid #ccc;  
padding: 10px;  
text-align: left;  
}  
  
.bordered th {  
background-color: #dce9f9;  
background-image: -webkit-gradient(linear, left top, left bottom, from(#ebf3fc), to(#dce9f9));  
background-image: -webkit-linear-gradient(top, #ebf3fc, #dce9f9);  
background-image: -moz-linear-gradient(top, #ebf3fc, #dce9f9);  
background-image: -ms-linear-gradient(top, #ebf3fc, #dce9f9);  
background-image: -o-linear-gradient(top, #ebf3fc, #dce9f9);  
background-image: linear-gradient(top, #ebf3fc, #dce9f9);  
-webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;  
-moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;  
box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;  
border-top: none;  
text-shadow: 0 1px 0 rgba(255,255,255,.5);  
}  
  
.bordered td:first-child, .bordered th:first-child {  
border-left: none;  
}  
  
.bordered th:first-child {  
-moz-border-radius: 6px 0 0 0;  
-webkit-border-radius: 6px 0 0 0;  
border-radius: 6px 0 0 0;  
}  
  
.bordered th:last-child {  
-moz-border-radius: 0 6px 0 0;  
-webkit-border-radius: 0 6px 0 0;  
border-radius: 0 6px 0 0;  
}  
  
.bordered th:only-child{  
-moz-border-radius: 6px 6px 0 0;  
-webkit-border-radius: 6px 6px 0 0;  
border-radius: 6px 6px 0 0;  
}  
  
.bordered tr:last-child td:first-child {  
-moz-border-radius: 0 0 0 6px;  
-webkit-border-radius: 0 0 0 6px;  
border-radius: 0 0 0 6px;  
}  
  
.bordered tr:last-child td:last-child {  
-moz-border-radius: 0 0 6px 0;  
-webkit-border-radius: 0 0 6px 0;  
border-radius: 0 0 6px 0;  
}  
  
/*----------------------*/  
  
.zebra td, .zebra th {  
padding: 10px;  
border-bottom: 1px solid #f2f2f2;  
}  
  
.zebra tbody tr:nth-child(even) {  
background: #f5f5f5;  
-webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;  
-moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;  
box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;  
}  
  
.zebra th {  
text-align: left;  
text-shadow: 0 1px 0 rgba(255,255,255,.5);  
border-bottom: 1px solid #ccc;  
background-color: #eee;  
background-image: -webkit-gradient(linear, left top, left bottom, from(#f5f5f5), to(#eee));  
background-image: -webkit-linear-gradient(top, #f5f5f5, #eee);  
background-image: -moz-linear-gradient(top, #f5f5f5, #eee);  
background-image: -ms-linear-gradient(top, #f5f5f5, #eee);  
background-image: -o-linear-gradient(top, #f5f5f5, #eee);  
background-image: linear-gradient(top, #f5f5f5, #eee);  
}  
  
.zebra th:first-child {  
-moz-border-radius: 6px 0 0 0;  
-webkit-border-radius: 6px 0 0 0;  
border-radius: 6px 0 0 0;  
}  
  
.zebra th:last-child {  
-moz-border-radius: 0 6px 0 0;  
-webkit-border-radius: 0 6px 0 0;  
border-radius: 0 6px 0 0;  
}  
  
.zebra th:only-child{  
-moz-border-radius: 6px 6px 0 0;  
-webkit-border-radius: 6px 6px 0 0;  
border-radius: 6px 6px 0 0;  
}  
  
.zebra tfoot td {  
border-bottom: 0;  
border-top: 1px solid #fff;  
background-color: #f1f1f1;  
}  
  
.zebra tfoot td:first-child {  
-moz-border-radius: 0 0 0 6px;  
-webkit-border-radius: 0 0 0 6px;  
border-radius: 0 0 0 6px;  
}  
  
.zebra tfoot td:last-child {  
-moz-border-radius: 0 0 6px 0;  
-webkit-border-radius: 0 0 6px 0;  
border-radius: 0 0 6px 0;  
}  
  
.zebra tfoot td:only-child{  
-moz-border-radius: 0 0 6px 6px;  
-webkit-border-radius: 0 0 6px 6px  
border-radius: 0 0 6px 6px  
}  
  
</style>
</head>
<body>

已定义流程
<hr>
<div>
<table class="bordered">
<tr>
<td>流程名称</td><td>操作</td>
</tr>
<c:forEach var="deploy" items="${dlist}">
<tr>
	<td>${deploy.name}</td>
	<td>
		<a href="${ctx}/workflow/deploy?name=${deploy.name}">发布流程</a>
	</td>
</tr>	
</c:forEach>
</table>
</div>
<br>

已发布流程
<hr>
<table border="0" class="bordered">
<tr>
	<td>ID</td><td>流程名称</td><td>发布时间</td><td>操作</td>
</tr>
<c:forEach var="deploy" items="${list}">
<tr>
	<td>${deploy.id }</td>
	<td>${deploy.name}</td>
	
	<td><f:formatDate value="${deploy.deploymentTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	<td>
		<a href="${ctx}/workflow/viewimagepagebydeploy?id=${deploy.id }">查看流程</a>
	</td>
</tr>
</c:forEach>
</table>
</body>
</html>