<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/SuperTag.tld"  prefix="ui" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>  
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/theme/css/context.css"/>
		 
	</head>
	<body>

		<%@include file="top.jsp" %>
		<div class="wrap">
			<div style="overflow: hidden;">
			     <%@include file="menu.jsp"%>
	            <div class="rightcontent"> 
	             </div>
        </div>
        <%@include file="foot.jsp" %>
		 <script>
		  $('#myMatterMenu').addClass('active');
		</script>
	</body>
</html>
