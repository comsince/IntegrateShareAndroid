<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
		     var setting = {hint:"删除后无法恢复,确定删除这个记录吗?",
		                    onConfirm:function(){
		                      $.post("<%=basePath%>/admin/record_delete.action", {gid:id},
							  function(data){
							      showSTip("删除成功");
					              $('#'+id).fadeOut().fadeIn().fadeOut();
					              doHideConfirm();
						      });
		                     
		                    }};
		     
		     doShowConfirm(setting);
		  }
		  
		  function doExport()
		  {
		  
		  
		     $('#searchForm').attr("action","<%=basePath%>/admin/record_exportCallRecord.action");
		     $('#searchForm').attr("target","exportIframe");
		     
		     document.all.searchForm.submit();
		     $('#searchForm').attr("action","<%=basePath%>/admin/record_callManage.action");
		     $('#searchForm').removeAttr("target");
		     
		  }
		  
		  
		</script>
	</head>
	<body class="web-app ui-selectable">


		<%@include file="../header.jsp"%>

		<%@include file="../nav.jsp"%>

		<div id="mainWrapper">
			<div class="panel panel-default">
				<div class="panel-heading">
					通话记录管理
					<div class="btn-group" style="margin-top:-7px;float: right;">
					   <button type="button" class="btn btn-info" onclick="doShowDialog('exportDialog')">
								导出通话记录
						</button>
					</div>
				</div>
				<div class="panel-body" style="padding: 5px;">
					<form action="<%=basePath%>/admin/record_callManage.action" method="post"
						id="searchForm" style="padding: 0px;">
						<input type="hidden" name="currentPage" id="currentPage" />
						<table style="margin: 5px;" class="utable">

							<thead>
								<tr class="tableHeader">
									<th width="15%">目标手机号</th>
									<th width="10%">类型</th>
									<th width="15%">对方手机号</th>
									<th width="10%">对方姓名</th>
									<th width="15%">通话时间</th>
									<th width="15%">结束时间</th>
									<th width="10%">通话时长</th>
									<th width="10%">录音文件</th>
									<th width="20%">操作</th>
								</tr>
								<tr>

									<td style="padding: 2px;">
									   <c:if test="${sessionScope.user.account eq 'admin'}">
										<input name="menumber" value="${record.menumber }" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
										</c:if>
									</td>

									 
                                    <td style="padding: 2px;">
										<select style="width: 100%;  font-size: 14px;padding: 0px;" class="form-control" name="status" id="Sstatus" >
									    <option></option>
										<option value="1">主叫</option>
										<option value="2">被叫</option>
									</select>
									</td>
									
									<td>
										<input name="henumber" value="${record.henumber }" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>
									
									<td>
										<input name="hename" value="${record.hename }" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>

									<td></td>
									<td></td>
									<td></td>
                                    <td></td>
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
											${record.menumber }
										</td>
										 
										<td>
											<c:if test="${record.status eq '1'}">主叫</c:if>
											<c:if test="${record.status eq '2'}">被叫</c:if>
										</td>
										<td>
											${record.henumber }
										</td>
										 
										<td>
											${record.hename }
										</td>
										<td>
										<c:if test="${record.beginTime eq null}">
										   <span class="label label-danger">未接通</span>
										</c:if>
										<c:if test="${record.beginTime ne null}">
										    <ui:datetime timestamp="${record.beginTime }"/>
										</c:if>
										</td>
										<td>
											<ui:datetime timestamp="${record.endTime }"/>
										</td>
										<td>
										    <c:if test="${record.beginTime ne null &&record.endTime ne null}">
										    
										      <fmt:formatNumber var="duration" value="${(record.endTime - record.beginTime) / 1000}" pattern="#"/>
											  ${duration}秒
											</c:if>
										</td>
										<td>
										    <c:if test="${record.content ne null}">
											    <div class="btn-group btn-group-xs">
										             <a target="_blank" class="btn btn-success"  style="padding: 5px;" href="<%=basePath%>/${record.content }">下载录音</a>
											    </div>
										    </c:if>
										</td>
										 
										<td>
											
											<div class="btn-group btn-group-xs">
											  <button type="button" class="btn btn-danger"  style="padding: 5px;" onclick="doDelete('${record.gid}')">删除</button>
											</div>
	 
										</td>
									</tr>
								</c:forEach>
								<c:if test="${page.count==0}">
									<tr>
										<td colspan="9">
											暂无记录
										</td>
									</tr>
								</c:if>

							</tbody>
							<tfoot>
								<tr>
									<td colspan="9">
										<ui:page page="${page}"></ui:page>
									</td>
								</tr>
							</tfoot>
						</table>
					</form>

				</div>
			</div>
			
			<%@include file="detailDialog.jsp"%>
			<%@include file="exportDialog.jsp"%>

		<script>
		       $('#callManageMenu').addClass('current');
		       $('#Sstatus').val('${record.status}');
		</script>
	</body>
</html>
