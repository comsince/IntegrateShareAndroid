	<%@ page language="java" pageEncoding="utf-8"%>
 
  <div class="panel panel-primary gdialog" id="exportDialog"
						style="display: none; width: 500px; position: absolute;">
						<div class="panel-heading">
							导出通话记录
							<a class="close" onclick="doHideDialog('exportDialog')">&times;</a>
						</div>
						<div class="panel-body">
						    <div class="alert alert-info" id="importAlert">
						             <b>提示:</b> 将会导出当前查询条件下的所以通话记录为Excel文件，</br>
						    </div>
							<center>
									<button type="button" id="importButton" style="width: 120px;margin-top: 10px;" class="btn btn-success btn-lg" onclick="doExport()">确定导出</button>
						   </center>
						<iframe style="width: 0px;height: 0px;display: none;" id="exportIframe" name="exportIframe"></iframe>		
						</div>
			</div>
	</div>