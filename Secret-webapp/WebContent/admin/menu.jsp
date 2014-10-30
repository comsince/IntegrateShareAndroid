 <%@ page language="java" pageEncoding="UTF-8"%>
   <%
String mpath = request.getContextPath();
String mbasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+mpath;
%>
 <div class="leftnav">
	 <ul >
		<li id="userMenu"><a href="<%=mbasePath%>/admin/user_manage.php">用户管理</a></li>
		<li id="matterMenu"><a href="<%=mbasePath%>/admin/matter_manage.php">主题管理</a></li>
	    <li id="commentMenu"><a href="<%=mbasePath%>/admin/comment_manage.php">评论管理</a></li>
	    <li id="configMenu"><a href="<%=mbasePath%>/admin/config_manage.php">配置管理</a></li>
	 </ul>
 </div> 