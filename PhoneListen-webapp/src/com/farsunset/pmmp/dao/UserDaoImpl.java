package com.farsunset.pmmp.dao;

 
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.farsunset.framework.hibernate.HibernateBaseDao;
import com.farsunset.framework.tools.StringUtil;
import com.farsunset.framework.web.Page;
import com.farsunset.pmmp.model.User;


public class UserDaoImpl extends HibernateBaseDao<User>{

	 

	public User queryByAccount(String account) {
		Criteria c = getSession().createCriteria(User.class);
		c.add(Restrictions.eq("account", account));
		return (User) c.uniqueResult();
	}

	 

	public Page queryUserList(User User, Page page) {
		 
		Criteria criteria = mapingParam( User);
	    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
	    criteria.setMaxResults(page.size);
	    criteria.addOrder(Order.desc("createTime"));
	    page.setDataList(criteria.list());
	    
	    return page;
	}
	public int queryUserAmount(User model) {
		 Criteria criteria = mapingParam( model);
		 return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
	}
	private Criteria mapingParam(User model)
	{
	    Criteria criteria = getSession().createCriteria(User.class);
	   
	    if (!StringUtil.isEmpty(model.getAccount()))
	    {
	      criteria.add(Restrictions.eq("account", model.getAccount()));
	    }
	    if (!StringUtil.isEmpty(model.getName()))
	    {
	    	criteria.add(Restrictions.eq("name", model.getName()));
	    }
	    if (!StringUtil.isEmpty(model.getMobile()))
	    {
	    	criteria.add(Restrictions.eq("mobile", model.getMobile()));
	    }
	    return criteria;
	 }

	 

	public List<User> queryAllUsers() {
		Criteria criteria = getSession().createCriteria(User.class);
		//criteria.add(Restrictions.lt("status",500));
		return criteria.list();
	}

 
	public List<User> queryUserList(User user) {
		Criteria criteria =mapingParam( user);
		return criteria.list();
	}


 

}
