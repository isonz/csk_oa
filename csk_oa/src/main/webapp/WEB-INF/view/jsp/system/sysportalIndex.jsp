<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>OA系统管理</title>
<style type="text/css">
.header{border-bottom:1px solid #ccc;margin-bottom:5px;}
.MainContainer{min-width:960px;max-width:1600px;}
.sidebar{width:180px;float:left;margin-right:-180px;border-right:1px solid #ccc;min-height:500px;padding:5px;}
.content_div{float:left;margin-left:200px;padding:5px;}
.content{padding:0 10px;}
.iframe{border:none;}
</style>
</head>
<body>
<script type="text/javascript">
function logout(){
	var url="${ctx}/system/logout";
	window.location.href=url;
}
</script>
<div id="main_div">
	<div id="title_div" style="height:50px" class="header">
		<table style="width:100%" border="0">
		<tr>
		<%-- <td align="left" width="50px">
			<img alt="帕斯婷" src="${ctx }/images/logo.png" height="50px" width="50px">
		</td> --%>
		<td>
		<p align="center">
		帕斯婷OA系统管理
		</p>
		</td>
		<td align="right" width="200px">
		欢迎您：${user.userName}<button name="logout"  onclick="logout();">退出</button>
		</td>
		
		</tr>
		</table>
	</div>

	<div id="body_div" class="MainContainer">
		<div id="navigate_div" class="sidebar">
			<table>
				<tr><td><a href="${ctx}/system/dept/list" target="iframe_content">部门管理</a></td></tr>
				<tr><td><a href="${ctx}/system/user/list" target="iframe_content">用户管理</a></td></tr>
				<tr><td><a href="${ctx}/system/role/list" target="iframe_content">角色管理</a></td></tr>
				<tr><td><a href="${ctx}/workflow/listdeploy" target="iframe_content">流程管理</a></td></tr>
			</table>
		</div>
		<div id="content_div" class="content_div">
			<div class="content">
			<iframe class="iframe" id="iframe_content" name="iframe_content" src="${ctx }/system/dept/list" 
				width="740px" height="640px" ></iframe>
			</div>
		</div>
	</div>
</div>
</body>
</html>