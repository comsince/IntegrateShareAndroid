	<%@ page language="java" pageEncoding="utf-8"%>
	<script type="text/javascript">
     function doAddUser()
	{
		    var account = $('#Aaccount').val();
		    var name = $('#Aname').val();
		    var mobile = $('#Amobile').val();
		    if($.trim(account)=='')
		    {
		       showETip("请填账号!");
		       return;
		    }
		    if($.trim(name)=='')
		    {
		       showETip("请填姓名!");
		       return;
		    }
		    if($.trim(mobile)=='')
		    {
		       showETip("请填监听手机号!");
		       return;
		    }
		    showProcess('正在保存，请稍后......');
		    $.post("<%=basePath%>/admin/user_add.action", {mobile:$('#Amobile').val(),account:account,name:name},
			   function(data){
			   
			      hideProcess();
			      if(data=='1')
			      {
			       showETip("账号:"+account+"已经存在!");
			      }
			      else
			      {
			         showSTip("添加成功");
			         doHideDialog('AddUserDialog');
			         window.location.href=$('#searchForm').attr('action');
			      }
			      
		     });
		}
		  
   </script>
 	<div class="panel panel-primary gdialog" id="AddUserDialog"
						style="display: none; width: 400px; position: absolute;">
						<div class="panel-heading">
							添加用户
							<a class="close" onclick="doHideDialog('AddUserDialog')">&times;</a>
						</div>
						<div class="panel-body">
						    
						    <div class="alert alert-info" id="importAlert">
						             <b>提示:</b> 新添加的用户默认密码为 123456</br>
						    </div>
							<form role="form">
								<div class="form-group">
									<label for="Aaccount">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>账号:
									</label>
									<input type="text" class="form-control" id="Aaccount"
										maxlength="18" style="display: inline; width: 280px;" />
								</div>
								<div class="form-group" style="margin-top: 20px;">
									<label for="Aname">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>姓名:
									</label>
									<input type="text" class="form-control" id="Aname"
										maxlength="10" style="display: inline; width: 280px;" />
								</div>
								<div class="form-group" style="margin-top: 20px;">
									<label for="Amobile">
										 <font color="red">*</font>监听号码:
									</label>
									<input type="text" class="form-control" id="Amobile"
										maxlength="11" style="display: inline; width: 280px;" />
								</div>
								 
								<div class="form-group" style="margin-top: 20px;">
									<center>
										<button type="button" class="btn btn-success btn-lg"
											onclick="doAddUser()">
											&nbsp;&nbsp;&nbsp;&nbsp;保&nbsp;&nbsp;&nbsp;&nbsp;存&nbsp;&nbsp;&nbsp;&nbsp;
										</button>
									</center>
								</div>
							</form>
						</div>
					</div>