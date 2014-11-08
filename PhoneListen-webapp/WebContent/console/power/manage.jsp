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
		<title>掌上家园-管理系统</title>
		<!--[if IE]>
    <script type="text/javascript">
        var path = '/disk/p3p_index.action';
        if(/[\?&]qzone(=[^&#]*)?(&|$)/.test(location.search) && location.pathname !== path){
            location.replace(path + location.search + location.hash);
        }
    </script>
<![endif]-->


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
		     
		       var setting = {hint:"确定取消这个管理员吗?",
		                    onConfirm:function(){
		                          showProcess('正在保存，请稍后......');
						          $.post("<%=basePath%>/admin/power_cancelAdmin.action", {account:id},
								   function(data){
								      hideProcess();
								      showSTip("取消成功");
						              $('#'+id).fadeOut().fadeIn().fadeOut();
						              doHideConfirm();
							     });
		                    }};
		     
		     doShowConfirm(setting);
		  }
		  
		  function setSuperAdmin(obj)
		  {
		     
		      var setting = {hint:"确定设置["+obj.name+"]为超级管理员吗?",
		                    onConfirm:function(){
		                          showProcess('正在保存，请稍后......');
						          $.post("<%=basePath%>/admin/power_setSuperAdmin.action", {account:obj.account},
								   function(data){
								      showSTip("设置成功");
								      doHideDialog("userListDialog");
						              hideProcess();
						              window.location.href="<%=basePath%>/admin/power_manage.action";
						              doHideConfirm();
							     });
		                    }};
		     
		     doShowConfirm(setting);
		  }
		  function setAdmin(obj)
		  {
		     
		     var setting = {hint:"确定设置["+obj.name+"]为管理员吗?",
		                    onConfirm:function(){
		                           showProcess('正在保存，请稍后......');
						          $.post("<%=basePath%>/admin/power_setAdmin.action", {account:obj.account},
								   function(data){
								      showSTip("设置成功");
								      doHideDialog("userListDialog");
						              hideProcess();
						              doHideConfirm();
						              window.location.href="<%=basePath%>/admin/power_manage.action";
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
					管理员管理
					<div class="btn-group" style="margin-top:-7px;float: right;">
						<button type="button" class="btn btn-success"
							onclick="showUserListDialog()">
							添加管理员
						</button>
					</div>
				</div>
				<div class="panel-body" style="padding: 5px;">
					<form action="<%=basePath%>/admin/user_manage.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" />
						<table style="margin: 5px;width: 100%;" class="utable">

							<thead>
								<tr class="tableHeader">
									<th width="15%">账号</th>
									<th width="15%">名称</th>
									<th width="10%">类型</th>
									<th width="40%">权限</th>
									<th width="20%">操作</th>
								</tr>
							</thead>
							<tbody id="adminUserList">

								<c:forEach var="user" items="${adminList}">
								  <c:if test="${user.account ne sessionScope.user.account}">
									<tr id="${user.account}" status="${user.status}">
										<td>${user.account }</td>
										<td>${user.name }</td>
										<td>
											<c:if test="${user.status == 999}">
											<img src="<%=basePath%>/resource/img/icon_super_admin.png" style="width: 25px;"/>
											超级管理员
											</c:if>
											<c:if test="${user.status == 998}">
											<img src="<%=basePath%>/resource/img/icon_admin.png" style="width: 25px;"/>
											普通管理员
											</c:if>
											
											<c:if test="${user.status == 997}">
											<img src="<%=basePath%>/resource/img/icon_group_admin.png" style="width: 25px;"/>
											兴趣组管理员
											</c:if>
										</td>
										<td>
										  <c:if test="${user.status == 999}">所有权限</c:if>
										  <c:if test="${user.status == 998}">${sf:powers(user.power)}</c:if>
										</td>
										<td>
											<c:if test="${user.status == 998}">
												<div class="btn-group btn-group-xs">
												  <button type="button" class="btn btn-info" style="padding: 5px;" onclick="showPowerDialog(${user})">权限</button>
												  <button type="button" class="btn btn-primary" style="padding: 5px;" onclick="setSuperAdmin(${user})">超级管理员</button>
												  <button type="button" class="btn btn-danger"  style="padding: 5px;" onclick="doDelete('${user.account}')">取消管理员</button>
												</div>
	                                       </c:if>
										</td>
									</tr>
								</c:if>
								</c:forEach>
								 

							</tbody>
						</table>
					</form>

				</div>
			</div>
			
			
			<%@include file="addDialog.jsp"%>
			<%@include file="powerDialog.jsp"%>
            <%@include file="userListDialog.jsp"%>
		<script>
		           $('#powerMenu').addClass('current');
		           $.post("<%=basePath%>/cgi/department_list.action", {},
				   function(data){
				      for(var i = 0 ; i<data.data.length;i++)
				      {
				         if(data.data[i].type==1)
				         {
				            $('#Sdepartment').append("<option value='"+data.data[i].name+"'>"+data.data[i].name+"</option>");
				         }
				         if(data.data[i].type==0)
				         {
				           $('#Sorg').append("<option value='"+data.data[i].name+"'>"+data.data[i].name+"</option>");
				         }
				      }
			      });
		</script>
	</body>
</html>
