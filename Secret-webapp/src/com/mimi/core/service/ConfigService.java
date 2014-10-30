package com.mimi.core.service;

import java.util.HashMap;
import java.util.List;

import com.mimi.model.Config;

public abstract interface ConfigService
{


  public abstract Config queryConfigById(String sequenceId);

  public abstract void saveConfig(Config paramConfig);
  public abstract List<Config>  queryConfigByDomain(String domain);
  public abstract List<Config>  queryAllConfig();
  public void deleteConfig(String sequenceId) ;
  public abstract void update(Config config); 
  public HashMap<String,String> queryDomainList();
}