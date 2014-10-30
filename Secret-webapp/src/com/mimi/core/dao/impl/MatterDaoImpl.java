package com.mimi.core.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.mimi.common.Constants;
import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class MatterDaoImpl extends HibernateDaoSupport
  
{
  public void addOneMatter(Matter matter)
  {
    getHibernateTemplate().save(matter);
  }

  public void deleteMatterById(String matterId) {
    Assert.notNull(matterId, "deleteMatterById>sequenceId");
    Matter matter = new Matter();
    matter.setMatterId(matterId);
    getHibernateTemplate().delete(matter);
  }

  public Matter queryMatterById(String matterId) {
    Assert.notNull(matterId, "queryMatterById>matterId");
    Matter matter = new Matter();
    matter = (Matter)getHibernateTemplate().get(Matter.class, matterId);
     
    return matter;
  }

  public Page queryMatterByPage( Matter matter, Page page)
  {
    Criteria criteria = mapingParam( matter);
    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
    criteria.setMaxResults(page.size);
    criteria.addOrder(Order.desc(page.getOrder()));
    page.setDataList(criteria.list());
     
    return page;
  }
  
  public void updateMatter(Matter matter)
  {
    getHibernateTemplate().update(matter);
  }

  public int queryMatterAmount( Matter matter) {
    Criteria criteria = mapingParam( matter);
    return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
  }

  private Criteria mapingParam(Matter matter)
  {
    Criteria criteria = getSession().createCriteria(Matter.class);
    if (!SystemUtil.isEmpty(matter.getType()))
    {
      criteria.add(Restrictions.eq("type", matter.getType()));
    }
    if (!SystemUtil.isEmpty(matter.getUserId()))
    {
      criteria.add(Restrictions.eq("userId",matter.getUserId()));
    }
    if (!SystemUtil.isEmpty(matter.getMatterId()))
    {
      criteria.add(Restrictions.eq("matterId",matter.getMatterId()));
    }
    return criteria;
  }

  private Criteria mapingParam(HashMap<String, String> map )
  {
    Criteria criteria = getSession().createCriteria(Matter.class);
    if (!SystemUtil.isEmpty(map.get("type")))
    {
      criteria.add(Restrictions.eq("type",map.get("type")));
    }
    if (!SystemUtil.isEmpty(map.get("userId")))
    {
      criteria.add(Restrictions.eq("userId",map.get("userId")));
    }
    if (!SystemUtil.isEmpty(map.get("matterId")))
    {
      criteria.add(Restrictions.eq("matterId",map.get("matterId")));
    }
    if (!SystemUtil.isEmpty(map.get("fileType")))
    {
      criteria.add(Restrictions.eq("fileType",map.get("fileType")));
    }
    return criteria;
  }
  
  
 public Session getMSession()
 {
	 return this.getSession();
 }

public int queryMatterAmount(HashMap<String, String> map) {
	Criteria criteria = mapingParam( map);
    return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
}

public Page queryMatterByPage(HashMap<String, String> map, Page page) {
	    Criteria criteria = mapingParam( map);
	    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
	    criteria.setMaxResults(page.size);
	    criteria.addOrder(Order.desc(map.get("order")));
	    page.setDataList(criteria.list());
	     
	    return page;
}

public List<Matter> querySelfMatterList(String userId) {
	Criteria criteria = getSession().createCriteria(Matter.class);
    criteria.add(Restrictions.eq("userId",userId));
    criteria.add(Restrictions.eq("type", Constants.Common.TYPR_1));
    criteria.addOrder(Order.desc("timestamp"));
	return criteria.list();
}
 
}