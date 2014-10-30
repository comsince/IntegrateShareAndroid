<%@ page language="java"  pageEncoding="UTF-8"  %>
  <%
String tpath = request.getContextPath();
String tbasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+tpath;
%>

    <link rel="shortcut icon" href="<%=tbasePath%>/page/global/favicon.ico"/>
     <link rel="stylesheet" type="text/css" href="<%=tbasePath%>/theme/css/ThemeBlue.css"/>
    <link rel="stylesheet" type="text/css" href="<%=tbasePath%>/theme/css/button.css"/>
	<link rel="stylesheet" type="text/css" href="<%=tbasePath%>/theme/css/global.css"/>
	<link rel="stylesheet" type="text/css" href="<%=tbasePath%>/theme/css/ucenter.css"/>
	<link rel="stylesheet" type="text/css" href="<%=tbasePath%>/theme/css/form.css"/>
    <script type="text/javascript" src="<%=tbasePath%>/theme/jquery-1.8.3.min.js"></script>
    <div id="header">
	    <div class="logobanner">
			    <div class="wrap clearfix">
			        
			        <div class="topnav">
			        	 
                    </div>
			
			    </div>
		</div>
		<div class="tapmain" id="TOP_NAV">
		<div class="wrap">
	        <div class="nav clearfix">
	            <div class="alignleft">
	             <h3 id="TopTitleText" style="margin-top: 0px;">管理平台</h3>
	            </div>
	
	            <div class="alignright top-menu">
	            </div>
	        </div>
	    </div>
		</div>
	</div>