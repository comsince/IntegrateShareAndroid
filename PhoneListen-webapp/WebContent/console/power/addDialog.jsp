	<%@ page language="java" pageEncoding="utf-8"%>
	<script type="text/javascript">
     function doAddAdmin()
	{
		    var account = $('#Aaccount').val();
		    if($.trim(account)=='')
		    {
		       showETip("请填管理员账号!");
		       return;
		    }
		    var name = $('#Aname').val();
		    if($.trim(name)=='')
		    {
		      showETip("请填管理员名称!");
		      return;
		       
		    }else
		    {
		      
		      name = "管理员-"+name;
		    }
		    showProcess('正在保存，请稍后......');
		    $.post("<%=basePath%>/admin/power_addAdmin.action", {status:$('#Astatus').val(),account:account,name:name},
			   function(data){
			   
			      hideProcess();
			      if(data=='1')
			      {
			       showETip("工号:"+account+"已经存在!");
			      }
			      else
			      {
			         showSTip("添加成功");
			         doHideDialog('AddUserDialog');
			         window.location.href="<%=basePath%>/admin/power_manage.action";
			      }
			      
		     });
		}
		  
   </script>
 	<div class="panel panel-primary gdialog" id="AddUserDialog"
						style="display: none; width: 400px; position: absolute;">
						<div class="panel-heading">
							添加管理员
							<a class="close" onclick="doHideDialog('AddUserDialog')">&times;</a>
						</div>
						<div class="panel-body">
							<form role="form">
								<div class="form-group">
									<label for="Aaccount">
										<font color="red">*</font>账号:
									</label>
									<input type="text" class="form-control" id="Aaccount"
										maxlength="18" style="display: inline; width: 295px;" />
								</div>
								<div class="form-group">
									<label for="Aaccount">
										<font color="red">*</font>名称:
									</label>
									<input type="text" class="form-control" id="Aname"
										maxlength="18" style="display: inline; width: 295px;" />
								</div>
								<div class="form-group">
									<label for="Aname">
										<font color="red">*</font>类型:
									</label>
									<select style="display: inline; width: 295px;" id="Astatus" class="form-control" name="status" >
									    <option value="998">普通管理员</option>
										<option value="999">超级管理员</option>
										
									</select>
								</div>
								<div class="form-group">
									<center>
										<button type="button" style="width: 120px;" class="btn btn-success btn-lg"
											onclick="doAddAdmin()">
											保 存
										</button>
									</center>
								</div>
							</form>
						</div>
					</div>