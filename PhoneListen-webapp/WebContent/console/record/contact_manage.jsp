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
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
			<div class="panel panel-default">
				<div class="panel-heading">
					通讯录 
				</div>
				<div class="panel-body" style="padding: 5px;">
					<form action="<%=basePath%>/admin/record_contactManage.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" />
						<table style="width: 100%;" class="utable">

							<thead>
								<tr class="tableHeader">
									<th width="40%">联系人号码</th>
									<th width="40%">联系人姓名</th>
									<th width="20%"></th>
								</tr>
								<tr>

									<td style="padding: 2px;">
										<input name="henumber" value="${record.henumber }" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>
									
									<td>
										<input name="hename" value="${record.hename }" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>

									<td >
										<button type="submit" class="btn btn-primary btn-sm">
											查询
										</button>
									</td>
								</tr>
							</thead>
							<tbody id="checkPlanList">
							
								<c:forEach var="record" items="${page.dataList}">
									<tr id="${record.gid}">
										<td>
											${record.henumber }
										</td>
										 
									 
										<td>
											${record.hename }
										</td>
										 
										<td>
										</td>
									</tr>
								</c:forEach>
								 
							</tbody>
							<tfoot>
								<tr>
									<td colspan="7">
										<ui:page page="${page}"></ui:page>
									</td>
								</tr>
							</tfoot>
						</table>
					</form>

				</div>
			</div>
			
			<%@include file="detailDialog.jsp"%>

		<script>
		       $('#contactManageMenu').addClass('current');
		     
		</script>
	</body>
</html>
