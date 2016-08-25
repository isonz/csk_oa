<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
<title>OA首页</title>
<style type="text/css">
	div{
		margin:20 10;
		background-color:yellow;
		
	}
	a{
		
		text-decoration: none;
		font-size: 24px;
	}
	/* a:link{color:blue} */
	a:visited{color:blue}
</style>
</head>
<body>
<div><a href="${ctx}/workflow/viewtasklist">查看待办任务</a></div>
<div><a href="${ctx}/extraworkapply/tolist">加班申请</a></div>
<div><a href="${ctx}/leaveapplication/tolist">请假申请</a></div>
<div><a href="${ctx}/comptimeapply/tolist">调班调休申请</a></div>
<div><a href="${ctx}/shoppingapply/tolist">购买物品申请</a></div>
<div><a href="${ctx}/talentdemandapply/tolist">用人需求申请表</a></div>

</body>
</html>
