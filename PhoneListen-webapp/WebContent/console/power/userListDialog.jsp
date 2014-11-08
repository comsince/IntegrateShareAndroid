	<%@ page language="java" pageEncoding="utf-8"%>
	
<script type="text/javascript">
function showUserListDialog()
{ 
   doShowDialog("userListDialog");
   doSearchUser();
}

function doSearchUser(){

   showProcess("正在加载，请稍后.....");
   $.post("<%=basePath%>/cgi/user_search.action", {name:$('#Sname').val(),acccount:$('#Sacccount').val(),org:$('#Sorg').val(),department:$('#Sdepartment').val()},
      function(data){
         $('#userTableList').empty();
	     hideProcess();
		 for(var i = 0;i<data.data.length;i++)
		 {
		   var user = data.data[i];
	       adduserTableRow(user);
		 }
    });
}

function adduserTableRow(user)
{
           var t="<tr id='u"+user.account+"'><td></td><td></td><td></td><td></td><td></td></tr>";
	       var add ="<button type='button' class='btn btn-success btn-sm' style='padding: 5px;'>设为管理员</button>";
	       var normalAdmin ="<span class='label label-info'>普通管理员</span>";
	       var superAdmin ="<span class='label label-primary'>超级管理员</span>";
	       
              $(t).appendTo('#userTableList');
		       $('#u'+user.account).find('td').eq(0).text(user.account);
		       $('#u'+user.account).find('td').eq(1).text(user.name);
		       $('#u'+user.account).find('td').eq(2).text(user.org);
		       $('#u'+user.account).find('td').eq(3).text(user.department);
		       if(user.status==999)
		       {
		         $('#u'+user.account).find('td').eq(4).html(superAdmin);
		       }
		       if(user.status==998)
		       {
		         $('#u'+user.account).find('td').eq(4).html(normalAdmin);
		       }
		       if(user.status==0)
		       {
		         $('#u'+user.account).find('td').eq(4).html(add);
		         $('#u'+user.account).find('td').find('button').bind("click", function(){
                    setAdmin(user,998);
                 });
		       }
}
 
</script>
	<div class="gdialog panel panel-primary " id="userListDialog"
						style="display: none; width: 900px; position: absolute;margin-top: -70px;z-index: 1001;">
						<div class="panel-heading">
							<span id="titleText">选择用户</span>
							<a class="close" onclick="doHideDialog('userListDialog')">&times;</a>
						</div>
						<div class="panel-body" style="overflow-y: scroll;max-height: 500px;padding: 5px;">
						   
							<table style="margin: 5px;width: 100%;margin-top: 10px;" class="utable">
							<thead>
								<tr class="tableHeader">
									<th width="25%">
										身份证号
									</th>
									<th width="15%">
										姓名
									</th>
									<th width="20%">
										单位
									</th>
									<th width="20%">
										科室
									</th>
									<th width="20%">
										操作
									</th>
								</tr>
								<tr>

									<td>
										<input id="Saccount" type="text"
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>

									<td>
										<input id="Sname" type="text" 
											class="form-control"
											style="width: 100%;  font-size: 14px;" />
									</td>
									<td>
									  <select class="form-control"   id="Sorg" style=" font-size: 12px;">
									  <option></option>
									  </select>
									</td>

									<td>
									    <select class="form-control"  id="Sdepartment" style=" font-size: 12px;">
									    <option></option>
									    </select>
									</td>

									<td>
										<button type="submit" class="btn btn-primary btn-sm" onclick="doSearchUser()">查询</button>
									</td>
								</tr>
								</thead>
								<tbody id="userTableList">
								</tbody>
								</table>
					    </div>
					
	</div>