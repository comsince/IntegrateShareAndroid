package com.mimi.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.model.User;
import com.mimi.util.SystemUtil;


public class UserDaoImpl extends HibernateDaoSupport{

	public void addOneUser(User User) {
		getHibernateTemplate().save(User);
	}

	public void deleteUserById(String userId) {
		Assert.notNull(userId, "deleteUserById>sequenceId");
		User user = new User();
		user.setUserId(userId);
		getHibernateTemplate().delete(user);
	}

	 

	public User queryUser(String userId) {
		Assert.notNull(userId, "queryUser>UserKey");
		Criteria c = getSession().createCriteria(User.class);
		c.add(Restrictions.eq("userId", userId));
		return (User) c.uniqueResult();
	}

	public User queryUserByAlias(String alias) {
		Assert.notNull(alias, "queryUserByAlias>alias");
		Criteria c = getSession().createCriteria(User.class).add(
				Restrictions.eq("alias", alias));
		return (User) c.uniqueResult();
	}

	public void updateUser(User user) {

		getHibernateTemplate().merge(user);
	}


	public boolean aliasIsSingle(String alias,String userId) {
		Criteria c = getSession().createCriteria(User.class).add(Restrictions.eq("alias", alias)).add(Restrictions.ne("userId", userId));
		List<User> list=c.list();
		if (!list.isEmpty()) {
			return false;
		}
		return true;
	}

	public User queryByIMEI(String imei) {
		Criteria c = getSession().createCriteria(User.class).add(Restrictions.eq("imei", imei));
		return (User) c.uniqueResult();
	}

	public Page queryUserList(User user, Page page) {
		 
		Criteria criteria = mapingParam( user);
	    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
	    criteria.setMaxResults(page.size);
	    criteria.addOrder(Order.desc(page.getOrder()));
	    try{
	      page.setDataList(criteria.list());
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return page;
	}
	public int queryUserAmount(User user) {
		 Criteria criteria = mapingParam( user);
		 return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
	}
	private Criteria mapingParam(User user)
	  {
	    Criteria criteria = getSession().createCriteria(User.class);
	    if (!SystemUtil.isEmpty(user.getAlias()))
	    {
	      criteria.add(Restrictions.eq("alias", user.getAlias()));
	    }
	    if (!SystemUtil.isEmpty(user.getUserId()))
	    {
	      criteria.add(Restrictions.eq("userId",user.getUserId()));
	    }
	    if (!SystemUtil.isEmpty(user.getImei()))
	    {
	      criteria.add(Restrictions.eq("imei",user.getImei()));
	    }
	    return criteria;
	  }



}
