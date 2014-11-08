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
		
		
		
		  function doDelete(id)
		  {
		     var setting = {hint:"删除后无法恢复,确定删除这个用户吗?",
		                    onConfirm:function(){
		                      $.post("<%=basePath%>/admin/user_delete.action", {account:id},
							  function(data){
							      showSTip("删除成功");
					              $('#'+id).fadeOut().fadeIn().fadeOut();
					              doHideConfirm();
						      });
		                     
		                    }};
		     
		     doShowConfirm(setting);
		  }
		</script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
			<div class="panel panel-default">
				<div class="panel-heading">
					用户管理
					<div class="btn-group" style="margin-top:-7px;float: right;">
					    <button type="button" class="btn btn-success"
								onclick="doShowDialog('AddUserDialog','slide_in_left')">
								添加用户
					   </button>
					</div>
				</div>
				<div class="panel-body" style="padding: 5px;">
					<form action="<%=basePath%>/admin/user_manage.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" />
						<table style="margin: 5px;" class="utable">

							<thead>
								<tr class="tableHeader">
								    <th width="10%">账号</th>
									<th width="10%">姓名</th>
									<th width="11%">手机号</th>
									<th width="11%">添加时间</th>
									<th width="20%">操作</th>
								</tr>
								<tr>

									<td style="padding: 2px;">
										<input name="account" value="${user.account }" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>

									<td>
										<input name="name" type="text" value="${user.name }"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>
                                      
									<td>
										<input name="mobile" value="${user.mobile }" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>

									 
									<td>
									   
									</td>

									<td >
										<button type="submit" class="btn btn-primary btn-sm">
											查询
										</button>
									</td>
								</tr>
							</thead>
							<tbody id="checkPlanList">

								<c:forEach var="user" items="${page.dataList}">
								   <c:if test="${user.account ne 'admin'}">
									<tr id="${user.account}">
										<td>
											${user.account }
										</td>
										<td>
											${user.name }
										</td>
										<td>
											${user.mobile }
										</td>
										<td>
											 <ui:datetime timestamp="${user.createTime }"/>
										</td>
										<td>
										 
											
											<div class="btn-group btn-group-xs">
											  <button type="button" class="btn btn-info" style="padding: 5px;" onclick="showModifyDialog(${user})">修改</button>
											  <button type="button" class="btn btn-danger"  style="padding: 5px;" onclick="doDelete('${user.account}')">删除</button>
											</div>
	 
										</td>
									</tr>
								</c:if>
								</c:forEach>
								<c:if test="${page.count==0}">
									<tr>
										<td colspan="5">
											暂无记录
										</td>
									</tr>
								</c:if>

							</tbody>
							<tfoot>
								<tr>
									<td colspan="5">
										<ui:page page="${page}"></ui:page>
									</td>
								</tr>
							</tfoot>
						</table>
					</form>

				</div>
			</div>
			
			
			<%@include file="modifyDialog.jsp"%>
			<%@include file="addDialog.jsp"%>
			<%@include file="detailDialog.jsp"%>

		<script>
		       $('#userMenu').addClass('current');
		     
		</script>
	</body>
</html>
