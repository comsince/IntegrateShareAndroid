package com.farsunset.pmmp.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.farsunset.framework.hibernate.HibernateBaseDao;
import com.farsunset.framework.tools.StringUtil;
import com.farsunset.framework.web.Page;
import com.farsunset.pmmp.model.Record;

public class RecordDaoImpl extends HibernateBaseDao<Record>{

	public Page queryRecordList(Record record, Page page) {
		 
		Criteria criteria = mapingParam( record);
	    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
	    criteria.setMaxResults(page.size);
	    criteria.addOrder(Order.desc("endTime"));
	    page.setDataList(criteria.list());
	    
	    return page;
	}
	public int queryRecordAmount(Record model) {
		 Criteria criteria = mapingParam( model);
		 return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
	}
	private Criteria mapingParam(Record model)
	{
	    Criteria criteria = getSession().createCriteria(Record.class);
	   
	    if (!StringUtil.isEmpty(model.getType()))
	    {
	      criteria.add(Restrictions.eq("type", model.getType() ));
	    }
	    if (!StringUtil.isEmpty(model.getStatus()))
	    {
	      criteria.add(Restrictions.eq("status", model.getStatus() ));
	    }
	    if (!StringUtil.isEmpty(model.getMenumber()))
	    {
	      criteria.add(Restrictions.eq("menumber", model.getMenumber() ));
	    }
	    if (!StringUtil.isEmpty(model.getHenumber()))
	    {
	      criteria.add(Restrictions.eq("henumber", model.getHenumber() ));
	    }
	     
	    return criteria;
	 }
	public List<Record> queryRecordList(Record record) {
		Criteria criteria = mapingParam( record);
	    criteria.addOrder(Order.desc("endTime"));
		return criteria.list();
	}

  

}