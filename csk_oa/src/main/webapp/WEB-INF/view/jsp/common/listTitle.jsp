<%@ page language="java" contentType="text/html; charset=UTF-8"	 pageEncoding="UTF-8"%>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/default.jsp"></jsp:include>
<style>
<!--
.title{
	display: block;
    float: left;
    position: relative;
    height: 25px;
    width: 80px;
    margin: 5px;
     
    text-decoration: none;
    font-weight: bold;
    line-height: 30px;
    text-align: center;
}

a:VISITED {
	color:blue;
}

body{
	margin: 0px;
}
-->
</style>

<div style="background-color:gray;width:100%;height: 25px;position: fixed;top: 0;z-index: 99;border-bottom: 1px solid green; padding-bottom: 10px;">
<a class="title" href="${ctx}/portalindex">首页</a>
<a class="title" href="javascript:location.reload(true);" >刷新</a>
<a class="title" href="javascript:history.go(-1);">返回</a> 
<!-- <a href="#" onclick="self.location=document.referrer;">返回</a> -->

</div>
<div style="height: 35px;">

</div>
