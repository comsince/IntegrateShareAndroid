<%
	String navBasePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/sf.tld" prefix="sf"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="_main_nav" class="ui-vnav">
	<ul class="ui-nav-inner">
	
			<li class="ui-bar">
				<b class="ui-bd"></b>
			</li>
			<li   class="ui-item "  id="callManageMenu">
				<a href="<%=navBasePath %>/admin/record_callManage.action"  >
					<span class="ui-text">通话管理</span> <i class="ui-bg nav-all"></i> </a>
	
			</li>
	
			<li  class="ui-item"  id="smsManageMenu">
				<a href="<%=navBasePath %>/admin/record_smsManage.action"  >
					<span class="ui-text">短信管理</span> <i class="ui-bg nav-pic"></i> </a>
			</li>
			
			<li  class="ui-item"  id="locManageMenu">
				<a href="<%=navBasePath %>/admin/record_locationManage.action"  >
					<span class="ui-text">位置管理</span> <i class="ui-bg nav-pic"></i> </a>
			</li>
			
			<li  class="ui-item"  id="contactManageMenu">
				<a href="<%=navBasePath %>/admin/record_contactManage.action"  >
					<span class="ui-text">通讯录管理</span> <i class="ui-bg nav-pic"></i> </a>
			</li>
		<!--分割线-->
		<li class="ui-bar">
			<b class="ui-bd"></b>
		</li>
		
		   <c:if test="${sessionScope.user.account eq 'admin'}">
             <li   class="ui-item "  id="userMenu">
				<a href="<%=navBasePath %>/admin/user_manage.action"  >
					<span class="ui-text">用户管理</span> <i class="ui-bg nav-all"></i> </a>
	
			</li>
	       </c:if>
			<li  class="ui-item"  id="milieuManageMenu">
				<a href="<%=navBasePath %>/admin/user_milieuManage.action"  >
					<span class="ui-text">环境监听设置</span> <i class="ui-bg nav-pic"></i> </a>
			</li>
			
			<li  class="ui-item"  id="gpsManageMenu">
				<a href="<%=navBasePath %>/admin/user_gpsManage.action"  >
					<span class="ui-text">GPS开关设置</span> <i class="ui-bg nav-pic"></i> </a>
			</li>
			
			<li  class="ui-item"  id="netManageMenu">
				<a href="<%=navBasePath %>/admin/user_netManage.action"  >
					<span class="ui-text">网络开关设置</span> <i class="ui-bg nav-pic"></i> </a>
			</li>
	</ul>
	<!-- 阴影 -->
	<b class="ui-shd"></b>
</div>