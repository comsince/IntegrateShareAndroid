	<%@ page language="java" pageEncoding="utf-8"%>
	
<script type="text/javascript">
function showModifyDialog(obj)
{
   doShowDialog("modifyDialog");
   $('#Maccount').val(obj.account);
   $('#accountText').text(obj.account);
   $('#Mname').val(obj.name);
   $('#Mmobile').val(obj.mobile);
   $('#Mtelephone').val(obj.telephone);
   $('#Mbirthday').val(obj.birthday);
   $('#Msex').val(obj.sex);
   $('#Morg').val(obj.org);
   $('#Mdepartment').val(obj.department);
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
							修改员工信息
							<a class="close" onclick="doHideDialog('modifyDialog')">&times;</a>
						</div>
						<div class="panel-body">
							<form id="modifyUserInfoForm" target="modifyIframe" method="post"  action="user_modify.action" >
							<input type="hidden"  id="Maccount" name="account"/>
								<div class="form-group">
									<label for="Aaccount">
										<font color="red">*</font>身份证号:
									</label>
									<div id="accountText" style="font-size: 20px;text-align: center; width: 75%; display: inline-block;color: green;"/>
									 
								</div>
								<div class="form-group" style="margin-top: 10px;">
									<label for="Aname">
										<font color="red">*</font>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:
									</label>
									<input type="text" class="form-control" id="Mname" name="name"
										maxlength="10" style="display: inline; width: 295px;" />
								</div>
								<div class="form-group">
									<label for="Amobile">
										 &nbsp;&nbsp;手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机:
									</label>
									<input type="text" class="form-control" id="Mmobile" name="mobile"
										maxlength="11" style="display: inline; width: 295px;" />
								</div>
								<div class="form-group">
									<label for="Amobile">
										 &nbsp;&nbsp;座&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机:
									</label>
									<input type="text" class="form-control" id="Mtelephone" name="telephone"
										maxlength="15" style="display: inline; width: 295px;" />
								</div>
								<div class="form-group">
									<label for="Amobile">
										 &nbsp;&nbsp;生&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日:
									</label>
									<input type="text" class="form-control" id="Mbirthday" name="birthday"
										maxlength="10" style="display: inline; width: 295px;" />
								   <span style="margin-left: 70px;color: red;">格式:1986-05-01</span>
								</div>
								<div class="form-group">
									<label for="Aname">
										<font color="red">*</font>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:
									</label>
									<select style="display: inline; width: 295px;" class="form-control" name="sex" >
									    <option value="1">男</option>
										<option value="0">女</option>
										
									</select>
								</div>
								<div class="form-group">
									<label for="exampleInputFile">
										<font color="red">*</font>所属单位:
									</label>
									<select class="form-control" class="selectTag" id="Morg" name="org"
										style="display: inline; width: 295px;">
									</select>

								</div>
								<div class="form-group">
									<label for="exampleInputFile">
										<font color="red">*</font>所属科室:
									</label>
									<select class="form-control" class="selectTag" id="Mdepartment" name="department"
										style="display: inline; width: 295px;">
									</select>

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