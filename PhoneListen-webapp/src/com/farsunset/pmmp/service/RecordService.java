package com.farsunset.pmmp.service;
import java.util.List;

import com.farsunset.framework.web.Page;
import com.farsunset.pmmp.model.Record;

 public  interface RecordService {
    
    public void save(Record record);
    
     
    public List<Record> getAllRecords();

    public Record getById(String gid);
    
	public   Page queryRecordList(Record record, Page page);
	public   List<Record>  queryRecordList(Record record);
	public void delete(String gid);
}