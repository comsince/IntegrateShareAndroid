	<%@ page language="java" pageEncoding="utf-8"%>
	
<script type="text/javascript">
var modifyed = false;
function showPowerDialog(obj)
{
   $('#holdPowers').empty();
   $('#missingPowers').empty();
   doShowDialog("powerDialog");
   $('#Paccount').val(obj.account);
   $.post("<%=basePath%>/admin/power_list.action", {},
	 function(data){
		 for(var i = 0;i<data.length;i++)
		 {
		   var power =data[i];
	       
	       
	       var h="<li id='h"+power.index+"' class='list-group-item'>"+power.name+" <button type='button' onclick='removePower("+power.index+")'  style='padding: 5px;float: right;margin-top: -5px;' class='btn btn-danger'>取消</button></li>";
	       var m="<li id='m"+power.index+"' class='list-group-item'>"+power.name+" <button type='button' onclick='addPower("+power.index+")' style='padding: 5px;float: right;margin-top: -5px;' class='btn btn-success'>添加</button></li>";
	       
	       if(obj.power.substr(power.index,1)=='1')
	       {
	         $(h).appendTo('#holdPowers');
	         
	       }else
	       {
	         $(m).appendTo('#missingPowers');
	         
	       }
		 }	    
			      
	 });
}


function addPower(index)
{
   
		   showProcess("正在保存，请稍后");
		   $.post("<%=basePath%>/admin/power_setPower.action", {account:$('#Paccount').val(),index:index,powerValue:'1'},
			 function(data){
			   hideProcess();
			   showSTip("保存成功!")
			   $('#m'+index).appendTo('#holdPowers');
			   $('#m'+index).hide().fadeIn();
			   $('#m'+index).find("button").text("取消");
			   $('#m'+index).find("button").removeClass("btn-success").addClass("btn-danger");
			   $('#m'+index).find("button").unbind("click");
			   $('#m'+index).find("button").one("click", function(){
			            removePower(index);
			   });
			   $('#m'+index).attr('id','h'+index);
			   
			   modifyed = true;
			 });
	 
}

function removePower(index)
{

		  showProcess("正在保存，请稍后");
		  $.post("<%=basePath%>/admin/power_setPower.action", {account:$('#Paccount').val(),index:index,powerValue:'0'},
			 function(data){
			      hideProcess();
			      showSTip("保存成功!");
			      $('#h'+index).appendTo('#missingPowers');
				  $('#h'+index).hide().fadeIn();
				  $('#h'+index).find("button").text("添加");
				  $('#h'+index).find("button").removeClass("btn-danger").addClass("btn-success");
				  $('#h'+index).find("button").unbind("click");
				  $('#h'+index).find("button").one("click", function(){
				       addPower(index);
				  });
				  $('#h'+index).attr('id','m'+index);
				  modifyed = true;
			 });
 
}
 function backToManage()
		 {
		     doHideDialog('powerDialog');
		     if(modifyed)
		     {
		       showProcess('正在加载，请稍后......');
		        window.location.href = "<%=basePath %>/admin/power_manage.action";
		     }
		     
		 }
</script>
	<div class="panel panel-primary gdialog" id="powerDialog"
						style="display: none; width: 650px; position: absolute;">
						<div class="panel-heading">
							权限分配
							<a class="close" onclick="backToManage()">&times;</a>
						</div>
						<div class="panel-body">
						<input type="hidden" id="Paccount"/>
						 <div class="panel panel-success" 
							style="width: 300px;float: left;">
							<div class="panel-heading">已有权限</div>
							 <div class="panel-body" style="padding: 5px;">
								<ul class="list-group" id="holdPowers"> </ul>
							   </div>
					      </div> 
						 <div class="panel panel-default" 
							style="width: 300px;float: right;">
							<div class="panel-heading">未有权限</div>
							 <div class="panel-body" style="padding: 5px;">
								<ul class="list-group" id="missingPowers"></ul>
							   </div>
					      </div> 
					</div>
</div>