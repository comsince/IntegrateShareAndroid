package com.farsunset.pmmp.api.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.farsunset.framework.web.SuperAction;
import com.farsunset.pmmp.model.Record;
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

	 
	public String save() throws IOException {
		 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		try {
			
			if (request instanceof MultiPartRequestWrapper) {
				MultiPartRequestWrapper pr = (MultiPartRequestWrapper) request;
				if (pr.getFiles("file") != null && pr.getFiles("file").length > 0) {
					File file = pr.getFiles("file")[0];
					String path = ServletActionContext.getServletContext().getRealPath("recordFile");
					File des = new File(path + "/" + System.currentTimeMillis()+".amr");
					FileUtil.copyFile(file, des);
					record.setContent("recordFile/" + des.getName());
				}
			}
			recordServiceImpl.save(record);
		} catch (Exception e) {
			map.put("code", 500);
			e.printStackTrace();
		}
		
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().print(map.get("code"));
		return null;
	}
	
	 
	public Record getModel() {
		// TODO Auto-generated method stub
		return record;
	}
 
}