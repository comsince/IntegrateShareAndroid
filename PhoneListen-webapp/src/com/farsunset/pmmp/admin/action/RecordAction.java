package com.farsunset.pmmp.admin.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.farsunset.framework.tools.StringUtil;
import com.farsunset.framework.web.SuperAction;
import com.farsunset.pmmp.common.Constants;
import com.farsunset.pmmp.model.Record;
import com.farsunset.pmmp.model.User;
import com.farsunset.pmmp.service.RecordService;
import com.opensymphony.xwork2.ModelDriven;

public class RecordAction extends SuperAction implements ModelDriven<Record> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Record record = new Record();
	@Autowired
	private RecordService recordServiceImpl;

	public void setRecordServiceImpl(RecordService RecordServiceImpl) {
		this.recordServiceImpl = RecordServiceImpl;
	}

	 
	public String callManage() throws IOException {

		User user  = (User) session.getAttribute("user");
		if(!user.getAccount().equals("admin"))
		{
			record.setMenumber(user.getMobile());
		}
		record.setType(Constants.Common.TYPR_1);
		recordServiceImpl.queryRecordList(record, page);
		request.setAttribute("page", page);
		request.setAttribute("record", record);
		return "CALL_MANAGE";
	}

	public void exportCallRecord() throws IOException {

		User user  = (User) session.getAttribute("user");
		if(!user.getAccount().equals("admin"))
		{
			record.setMenumber(user.getMobile());
		}
		record.setType(Constants.Common.TYPR_1);
		List<Record>  list=recordServiceImpl.queryRecordList(record);
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("盘点计划数据");
		sheet.setDisplayGridlines(true);
		sheet.setDefaultColumnWidth(7);
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("目标手机号");
		row.createCell(1).setCellValue("通话类型");
		row.createCell(2).setCellValue("对方手机号");
		row.createCell(3).setCellValue("对方姓名");
		row.createCell(4).setCellValue("通话时间");
		row.createCell(5).setCellValue("结束时间");
		row.createCell(6).setCellValue("通话时长");
		int index=0;
		for(Record r:list)
		{
			row = sheet.createRow(++index);
			row.createCell(0).setCellValue(r.getMenumber());
			if(r.getStatus().equals("1"))
			{
				row.createCell(1).setCellValue("主叫");
			}
			if(r.getStatus().equals("2"))
			{
				row.createCell(1).setCellValue("被叫");
			}
			
			row.createCell(2).setCellValue(r.getHenumber());
			row.createCell(3).setCellValue(r.getHename());
			
			if(r.getBeginTime() !=null)
			{
				row.createCell(4).setCellValue(StringUtil.transformDateTime(Long.parseLong(r.getBeginTime())));
				row.createCell(6).setCellValue((Long.parseLong(r.getEndTime()) -Long.parseLong(r.getBeginTime()))/1000+"秒");
			}else
			{
				row.createCell(4).setCellValue("未接通");
				row.createCell(6).setCellValue("0秒");
			}
			row.createCell(5).setCellValue(StringUtil.transformDateTime(Long.parseLong(r.getEndTime())));
			
		}
		
		response.setHeader("Content-Disposition", "attachment;filename="
				.concat(String.valueOf(URLEncoder.encode("通话记录.xls", "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		wb.write(response.getOutputStream());
		
		
	}
	
	public String smsManage() throws IOException {

		record.setType(Constants.Common.TYPR_2);
		recordServiceImpl.queryRecordList(record, page);
		request.setAttribute("page", page);
		request.setAttribute("record", record);
		return "SMS_MANAGE";
	} 
	
	public String contactManage() throws IOException {

		record.setType(Constants.Common.TYPR_4);
		recordServiceImpl.queryRecordList(record, page);
		request.setAttribute("page", page);
		request.setAttribute("record", record);
		return "CONTACT_MANAGE";
	} 
	
	
	public String locationManage() throws IOException {

		record.setType(Constants.Common.TYPR_3);
		recordServiceImpl.queryRecordList(record, page);
		request.setAttribute("page", page);
		request.setAttribute("record", record);
		return "LOCATION_MANAGE";
	} 
	public void delete() throws IOException {
	    recordServiceImpl.delete(record.getGid());
		response.getWriter().print(1);
	}
 
	public Record getModel() {
		// TODO Auto-generated method stub
		return record;
	}
 
}