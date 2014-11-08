<%@ page language="java" pageEncoding="UTF-8"%>
 
<%
	String headerBasePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
	
	Object user = session.getAttribute("user");
	if(user == null)
	{
	
	  session.setAttribute("message","登录过期，请重新登录!");
	  response.sendRedirect(headerBasePath+"/console/login.jsp");
	}
%>
<script type="text/javascript">
  function showUserMenu()
  {
      if($('#_main_face_menu').is(":hidden"))
      {
         $('#_main_face_menu').fadeIn();
        $('.user-avatar').addClass('user-avatar-hover');
      }else
      {
         $('#_main_face_menu').hide();
         $('.user-avatar').removeClass('user-avatar-hover');
      }
      
  }
  
  
  function showModifyPassword()
  {
      doShowDialog("modifyPasswordDialog");
  }
  
  $(function(){
     $(".user-avatar").mouseenter(function(){
       $(".user-avatar").addClass('user-avatar-hover');
       $('#_main_face_menu').fadeIn();
     });
     
     $("#_main_face_menu").mouseleave(function(){
       $(".user-avatar").removeClass('user-avatar-hover');
       $('#_main_face_menu').fadeOut();
     });
 
  });
  function doModifyPassword()
  {
  
		    var oldPassword = $('#oldPassword').val();
		    var newPassword = $('#newPassword').val();
		    if($.trim(oldPassword)=='')
		    {
		       showETip("请输入当前密码!");
		       return;
		    }
		    if($.trim(newPassword)=='')
		    {
		       showETip("请输入新密码!");
		       return;
		    }
		    showProcess('正在修改，请稍后......');
		    $.post("<%=headerBasePath%>/cgi/user_modifyPassword.action", {account:'${sessionScope.user.account}',newPassword:newPassword,oldPassword:oldPassword},
			   function(data){
			   
			      hideProcess();
			      if(data.code==200)
			      {
			        showSTip("修改成功");
			        doHideDialog("modifyPasswordDialog");
			      }
			      
			      if(data.code==403)
			      {
			          showETip("当前密码不正确!");
			      }
		     });
  
  }
</script>


<div id="_main_fixed_header" class="header-fixed">
<a class="logo" href="http://www.weiyun.com/?WYTAG=appbox.disk.logo" target="_blank"></a>
	<!-- 头部 -->
	<div id="_main_header_banner" class="header">
	 
		<div id="_main_header_cnt" class="header-cnt">
			<div class="logo" style="left: -200px;"> </div>
			<!-- 头像 -->
			<div id="_main_face" data-no-selection="" class="header-right" style="cursor: pointer;" onclick="showUserMenu();">
				<span class="user-info"> <span
					class="user-avatar" style="width: 80px;"> <img
							src="<%=headerBasePath %>/resource/img/admin_default_head.png"><i
						class="ui-arr"></i> </span> </span>
			</div>
			<!-- 头像下方的菜单 -->
			<div id="_main_face_menu" style="display:none;"  class="ui-pop ui-pop-user" >
				<div class="ui-pop-head">
					<span id="_main_nick_name" class="user-nick" style="text-align: center;font-weight: bold;color: #555168;font-size: 18px;">${sessionScope.user.name}</span>
                   	<span class="user-nick" style="line-height: 10px;text-align: center;font-weight: bold;color: #3B20BB;font-size: 12px;">${sessionScope.user.account}</span>
                   
				</div>

				<ul class="ui-menu">
					<li>
						<a id="_main_pwd_locker" data-tj-action="btn-adtag-tj"
							data-tj-value="50002" href="javascript:doShowDialog('modifyPasswordDialog')"><i
							class="icon-pwd"></i>修改密码 </a>
					</li>

					<li>
						<a id="_main_logout" href="<%=headerBasePath %>/admin/user_logout.action"><i class="icon-exit"></i>退出</a>
					</li>
					<li>
						<a id="_main_logout" href="javascript:doShowDialog('aboutDialog')"><i class="icon-fedbk"></i>关于</a>
					</li>
				</ul>
				<i class="ui-arr"></i>
				<i class="ui-arr ui-tarr"></i>
			</div>
		</div>
	</div>

	<!--web的导航在左侧-->

</div>
 
<div class="panel panel-primary gdialog" id="modifyPasswordDialog" style="display: none;width: 400px;position: absolute;">
		  <div class="panel-heading">修改密码
		  <a class="close"  onclick="doHideDialog('modifyPasswordDialog')">&times;</a>
		  </div>
		  <div class="panel-body">
		   <form role="form">
			 
			  <div class="form-group">
			    <label for="Aname"><font color="red">*</font>旧密码:</label>
			    <input type="password" class="form-control"  id="oldPassword" maxlength="10" style="display: inline;width: 295px;height: 40px;"/>
			  </div>
			  <div class="form-group">
			    <label for="Aname"><font color="red">*</font>新密码:</label>
			    <input type="password" class="form-control"  id="newPassword" maxlength="10" style="display: inline;width: 295px;height: 40px;"/>
			  </div>
			   <div class="form-group">
			       <center>
			       <button type="button" style="width: 200px;font-weight: bold;" class="btn btn-success btn-lg" onclick="doModifyPassword()">修 改</button>
			       </center>
			   </div>
			</form>
		  </div>
</div>

<div class="panel panel-primary gdialog" id="aboutDialog" style="display: none;width: 400px;position: absolute;">
		  <div class="panel-heading">关于
		  <a class="close"  onclick="doHideDialog('aboutDialog')">&times;</a>
		  </div>
		  <div class="panel-body">
		      <ul class="list-group">
				  <li class="list-group-item">亲爱的用户，为了更好的使用体验，以及更稳定更高效更流畅的使用，请您不要使用老旧的IE浏览器，建议您使用，最新版本的IE10，谷歌浏览器，火狐浏览器等支持html5的最新浏览器.</li>
				  <li class="list-group-item">如果您在使用过程中遇到任何问题，请联系开发者，感谢您的使用.祝您工作顺利</li>
				  <li class="list-group-item">Email:3979434@qq.com</li>
				  <li class="list-group-item">QQ:3979434</li>
				  <li class="list-group-item">电话:13914714427</li>
				</ul>
		  </div>
</div>


<div id="global_mask" style="display: none; position: absolute; top: 0px; left: 0px; z-index: 998; background-color: rgb(190, 209, 216); opacity: 0.5; width: 100%; height: 100%; overflow: hidden; background-position: initial initial; background-repeat: initial initial;"></div>
 