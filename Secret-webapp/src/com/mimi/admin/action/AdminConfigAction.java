package com.mimi.admin.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mimi.core.service.impl.ConfigServiceImpl;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Config;
import com.opensymphony.xwork2.ModelDriven;

public class AdminConfigAction extends SuperAction implements ModelDriven<Config>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	private ConfigServiceImpl configServiceImpl;
    
	private Config config = new Config();

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setConfigServiceImpl(ConfigServiceImpl configServiceImpl) {
		this.configServiceImpl = configServiceImpl;
	}
	 
	  public String manage() throws Exception
	  {
		  List<Config> dataList =  configServiceImpl.queryAllConfig();
		  request.setAttribute("dataList", dataList);
		  request.setAttribute("count", dataList == null ?0:dataList.size());
		  return "manage";
	  }
	  public String search() throws Exception
	  {
		  List<Config> dataList =  configServiceImpl.queryConfigByDomain(config.getDomain());
		  HashMap<String,String> map = configServiceImpl.queryDomainList();
		  request.setAttribute("map", map);
		  request.setAttribute("dataList", dataList);
		  request.setAttribute("count", dataList == null ?0:dataList.size());
		  return "manage";
	  }
	 
 
	  public String save() throws IOException  
		{
		  config = new Config();
		 config.setDomain(request.getParameter("domain"));
		 config.setKey(request.getParameter("key"));
		 config.setValue(request.getParameter("value"));
		  try{
		    configServiceImpl.saveConfig(config);
		  }catch(Exception e)
		  {
			  e.printStackTrace();
			  response.getWriter().print(0);
			  return null;
		  }
		  response.getWriter().print(1);
		  return null;
		}
	  public String delete() throws IOException  
		{
		 
		  try{
			   configServiceImpl.deleteConfig(request.getParameter("sequenceId"));
			  }catch(Exception e)
			  {
				  e.printStackTrace();
				  response.getWriter().print(0);
				  return null;
			  }
			  response.getWriter().print(1);
			  return null;
		}
	
	public Config getModel() {
		  
		return config;
	}

	public Config getConfig() {
		return config;
	}
    
}
