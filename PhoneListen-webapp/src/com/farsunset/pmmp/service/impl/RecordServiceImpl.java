package com.farsunset.pmmp.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.farsunset.framework.web.Page;
import com.farsunset.pmmp.dao.RecordDaoImpl;
import com.farsunset.pmmp.model.Record;
import com.farsunset.pmmp.service.RecordService;

/**
 * 
 * @author 3979434
 */
public class RecordServiceImpl implements RecordService {
	protected final Log log = LogFactory.getLog(getClass());

	@Resource
	private RecordDaoImpl recordDao;

	public List<Record> getAllRecords() {
		return recordDao.getAll();
	}
 
	public void setRecordDao(RecordDaoImpl recordDao) {
		this.recordDao = recordDao;
	}
	
	public void save(Record record) {
		record.setGid(String.valueOf(System.currentTimeMillis()));
		 
		recordDao.save(record);
	}
	
	public Page queryRecordList(Record record, Page page) {
		 int count = this.recordDao.queryRecordAmount(record);
		 page.setCount(Integer.valueOf(count));
		 if(page.getCount()==0)
		 {
		    	return page;
		 }
		  return this.recordDao.queryRecordList(record, page);
	}
	
	public void delete(String gid) {
		Record u = new Record();
		u.setGid(gid);
		recordDao.delete(u);
	}


	public Record getById(String gid) {
		// TODO Auto-generated method stub
		return recordDao.get(gid);
	}

	@Override
	public List<Record> queryRecordList(Record record) {
		// TODO Auto-generated method stub
		return recordDao.queryRecordList(record);
	}

	 

}