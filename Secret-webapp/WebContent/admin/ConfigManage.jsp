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
		<script type="text/javascript">
	       function doDelete(id)
		   {
		      $.post("<%=basePath%>/admin/config_delete.php", {sequenceId:id},
			   function(data){
			     $('#'+id).fadeOut().fadeIn().fadeOut();
		       });
		   }
		   
		    function doSave()
		   {
		      $.post("<%=basePath%>/admin/config_save.php", {domain:$('#A_domain').val(),key:$('#A_key').val(),value:$('#A_value').val()},
			   function(data){
			      if(data=='0')
			      {
			         alert("添加失败");
			       }else
			       {
			         var t = "<tr id='"+data+"'><td>"+$('#A_domain').val()+"</td><td>"+$('#A_key').val()+"</td><td>"+$('#A_value').val()+"</td><td><a href='javascript:doDelete("+data+")'>删除</a></td></tr>";
			         $(t).appendTo('#configTable').fadeOut().fadeIn();
			       }
			       
			      
		       });
		   }
		</script>
	</head>
	<body>

		<%@include file="top.jsp"%>
		<div class="wrap">
			<div style="overflow: hidden;">
			     <%@include file="menu.jsp"%>
	            <div class="rightcontent"> 
	            <div class="controlpanel">
		            <form action="<%=basePath%>/admin/config_search.php" method="post" id="searchForm">
		            <input type="hidden" name="currentPage" id="currentPage"/>
		            <label>域</label><input type="text" name="config.domain"  value="${config.domain }"/>
		            <a class="simple_button" style="margin-left: 50px;" onclick="document.getElementById('searchForm').submit()">查询</a>
		            </form>
	            </div>
	            <div style="height: 60px;line-height: 60px;border: 1px solid #14A7D5;;width: 99%;">
		             
		            <input type="hidden" name="currentPage" id="currentPage"/>
		            <label style="padding-left: 10px;">域</label><input type="text" id="A_domain" style="height:35px;width:150px;" />
		            <label style="padding-left: 10px;">键</label><input type="text" id="A_key"  style="height:35px;width:150px;"/>
		            <label style="padding-left: 10px;">值</label><input type="text" id="A_value"  style="height:35px;width:500px;"/>
		            <a class="simple_button" style="margin-left: 15px;" onclick="doSave()">添加</a>
	            </div>
		           <table style="margin-top: 30px;" class="utable" id="configTable">
	            	<thead>
		                <tr>
		                    <th width="15%">域</th>
		                    <th width="15%">键</th>
		                    <th width="60%">值</th>
		                    <th width="10%">操作</th>
		                </tr>
	                </thead>
	                <tbody id="firstpublist">
	                    <c:forEach var="config" items="${dataList}">          
		                <tr id="${config.sequenceId}">	
		                   <td>${config.domain }</td>
		                   <td>${config.key }</td>
		                   <td>${config.value }</td>
		                   <td>
			                   <a href="javascript:doDelete(${config.sequenceId})">删除</a>
		                   </td>
		                </tr>
		                </c:forEach>
		                
	                </tbody>
	                <tfoot>
	                  <c:if test="${count==0}">
		                 <tr>	
		                   <td colspan="4">暂无记录</td>
		                </tr>
		              </c:if>
	                </tfoot>
	            </table>
	            </div>
	        </div>
        </div>
        <%@include file="foot.jsp" %>
		 <script>
		  $('#configMenu').addClass('active');
		</script>
	</body>
</html>
