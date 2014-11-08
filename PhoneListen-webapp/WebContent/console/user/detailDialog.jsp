	<%@ page language="java" pageEncoding="utf-8"%>
	
<script type="text/javascript">
function showDetailDialog(obj)
{
   doShowDialog("detailDialog");
   $('#Daccount').text(obj.account);
   $('#Dname').text(obj.name);
   $('#Dmobile').text(obj.mobile);
   $('#Dtelephone').text(obj.telephone);
   $('#Dbirthday').text(obj.birthday);
   if(obj.sex=='1')
   {
    $('#Dsex').text('男');
   }
   if(obj.sex=='0')
   {
    $('#Dsex').text('女');
   }
   if(obj.icon !='' &&obj.icon!=undefined )
   {
     $('#Dicon').attr('src','<%=basePath %>/'+obj.icon);
   }
   $('#Dorg').text(obj.org);
   $('#Ddepartment').text(obj.department);
   $('#Dalias').text(obj.alias);
   $('#Dsignature').text(obj.signature);
   $('#DcreateTime').text(getDateTime(obj.createTime));
}
 
</script>
	<div class="panel panel-primary gdialog" id="detailDialog"
						style="display: none; width: 400px; position: absolute;">
						<div class="panel-heading">
							员工信息
							<a class="close" onclick="doHideDialog('detailDialog')">&times;</a>
						</div>
						<div class="panel-body">
							<ul class="list-group">
							 <li class="list-group-item" id="Diconthumb">
								 <ul style="width: 80%;">
								 <li style="line-height: 30px;">
								   <span>身份证号:</span><span style="float: right;" id="Daccount"></span>
								 </li>
								 <li style="line-height: 30px;">
								   <span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</span>
								   <span style="float: right;" id="Dname"></span>
								 </li>
								  <li style="line-height: 30px;">
							      <span>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:</span><span style="float: right;" id="Dsex"></span>
							     </li>
								 </ul>
							   <div class="thumbnail" style="position: absolute;top: 25px;right: 10px;"><img width="50px" style="height: 50px;" id="Dicon"/></div>

							 </li>
							
							 <li class="list-group-item">
							   <span>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称:</span><span style="float: right;" id="Dalias"></span>
							 </li>
							 <li class="list-group-item">
							   <span>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机:</span><span style="float: right;" id="Dmobile"></span>
							 </li>
							 <li class="list-group-item">
							   <span>座&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机:</span><span style="float: right;" id="Dtelephone"></span>
							 </li>		 
						  	 <li class="list-group-item">
							   <span>生&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日:</span><span style="float: right;" id="Dbirthday"></span>
							 </li>		 
							 <li class="list-group-item">
							   <span>所属单位:</span><span style="float: right;" id="Dorg"></span>
							 </li>
							 <li class="list-group-item">
							   <span>所属科室:</span><span style="float: right;" id="Ddepartment"></span>
							 </li>
							 <li class="list-group-item">
							   <span>个性签名:</span><div style="word-break: break-all;" id="Dsignature"></div>
							 </li>
							 <li class="list-group-item">
							   <span>登记时间:</span><span style="float: right;" id="DcreateTime"></span>
							 </li>
						</div>
					</div>
</div>