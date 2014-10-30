package com.mimi.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mimi.model.Config;

public class ConfigDaoImpl extends HibernateDaoSupport
{

  public Config queryConfigById(String sequenceId) {
    Config config = new Config();
    config = (Config)getHibernateTemplate().get(Config.class, sequenceId);
    return config;
  }


  public void saveConfig(Config config)
  {
     getHibernateTemplate().merge(config);
      
  }

  


public List<Config> queryAllConfig() {
	 Criteria criteria = getSession().createCriteria(Config.class);
	 return criteria.list();
	  
}


public List<Config> queryConfigByDomain(String domain) {
	 Criteria criteria = getSession().createCriteria(Config.class).add(Restrictions.eq("domain", domain));
	 return criteria.list();
	  
}
 
public void deleteConfig(String sequenceId) {
	Config config = new Config();
	config.setSequenceId(sequenceId);
	 getHibernateTemplate().delete(config);
}


public void update(Config config) {

	  getHibernateTemplate().update(config);
}
}