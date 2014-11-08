	<%@ page language="java" pageEncoding="utf-8"%>
	
<script type="text/javascript">
function showModifyDialog(obj)
{
   doShowDialog("modifyDialog");
   $('#Maccount').val(obj.account);
   $('#accountText').text(obj.account);
   $('#Mname').val(obj.name);
   $('#Mmobile').val(obj.mobile);
}

function modifyUserInfoCallback()
{

     hideProcess();
     showSTip("保存成功!");
     doHideDialog('modifyDialog')
     document.getElementById('searchForm').submit();
}

function doModifyUserInfo()
{

   showProcess("正在保存，请稍后......");
   document.getElementById('modifyUserInfoForm').submit();
}

 
</script>
	<div class="panel panel-primary gdialog" id="modifyDialog"
						style="display: none; width: 400px; position: absolute;">
						<div class="panel-heading">
							修改用户信息
							<a class="close" onclick="doHideDialog('modifyDialog')">&times;</a>
						</div>
						<div class="panel-body">
							<form id="modifyUserInfoForm" target="modifyIframe" method="post"  action="user_modify.action" >
							<input type="hidden"  id="Maccount" name="account"/>
								<div class="form-group">
									<label for="Aaccount">
										<font color="red">*</font>账号:
									</label>
									<div id="accountText" style="font-size: 20px;text-align: center; width: 75%; display: inline-block;color: green;"/>
									 
								</div>
								<div class="form-group" style="margin-top: 10px;">
									<label for="Aname">
										<font color="red">*</font>姓名:
									</label>
									<input type="text" class="form-control" id="Mname" name="name"
										maxlength="10" style="display: inline; width: 295px;" />
								</div>
								<div class="form-group">
									<label for="Amobile">
										 <font color="red">*</font>监听手机号:
									</label>
									<input type="text" class="form-control" id="Mmobile" name="mobile"
										maxlength="11" style="display: inline; width: 255px;" />
								</div>
							 
								<div class="form-group">
									<center>
										<button type="button" class="btn btn-success btn-lg"
											onclick="doModifyUserInfo()">
											&nbsp;&nbsp;&nbsp;&nbsp;保&nbsp;&nbsp;&nbsp;&nbsp;存&nbsp;&nbsp;&nbsp;&nbsp;
										</button>
									</center>
								</div>
							</form>
							<iframe style="width: 0px;height: 0px;display: none;" id="modifyIframe" name="modifyIframe"></iframe>
						</div>
					</div>
</div>