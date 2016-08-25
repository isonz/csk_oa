<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>

<title>流程图</title>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
</head>
<body>
<jsp:include flush="true" page="/WEB-INF/view/jsp/common/listTitle.jsp"></jsp:include>
<div style="position: absolute;">
<div style="position:relative ;top: 0px;left:0px;width: 100%;">
<c:if test="${taskid!=null}">
<img  src="${ctx}/workflow/viewimagetaskid?taskid=${taskid}">
</c:if>
<c:if test="${taskid==null}">
<img   src="${ctx}/workflow/viewimage?businessKey=${businessKey}">
</c:if>
<div style="position: absolute;border:1px solid red;top:${zb.y}px;left:${zb.x}px;width:${zb.width}px;height:${zb.height}px"></div>
</div>


<script type="text/javascript">
var x = ${zb.x};
var y= ${zb.y};
var width=${zb.width};
var clientWidth = document.body.clientWidth;
var scrollx = x - (clientWidth - width)/2; 
window.onload=function(){
	scroll(scrollx,y - 5);
}
</script>
</body>
 </html>