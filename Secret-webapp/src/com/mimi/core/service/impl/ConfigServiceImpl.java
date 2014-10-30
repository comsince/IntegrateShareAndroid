package com.mimi.core.service.impl;

import java.util.HashMap;
import java.util.List;

import com.mimi.core.dao.impl.ConfigDaoImpl;
import com.mimi.core.service.ConfigService;
import com.mimi.model.Config;
import com.mimi.util.SystemUtil;

public class ConfigServiceImpl
  implements ConfigService
{
  private ConfigDaoImpl configDao;


   
  public void setConfigDao(ConfigDaoImpl configDao) {
	this.configDao = configDao;
}


public Config queryConfigById(String sequenceId) {
	    return configDao.queryConfigById(sequenceId);
	  }


	  public void saveConfig(Config config)
	  {
		   config.setSequenceId(SystemUtil.getSequenceId());
		   configDao.saveConfig(config);
	  }

	@Override
	public List<Config>  queryAllConfig() {
		 return configDao.queryAllConfig();
	}
	 
	@Override
	public List<Config>  queryConfigByDomain(String domain) {
		// TODO Auto-generated method stub
		return configDao.queryConfigByDomain(domain);
	}
	public void deleteConfig(String sequenceId) {
		configDao.deleteConfig(sequenceId);
	}
	@Override
	public void update(Config config) {
		configDao.update(config);
	}


	@Override
	public HashMap<String,String> queryDomainList() {
		List<Config> list = configDao.queryAllConfig();
		HashMap<String,String>  map = new HashMap<String,String> ();
		for(Config obj:list)
		{
			map.put(obj.getDomain(), obj.getDomain());
		}
		return map;
	}
}