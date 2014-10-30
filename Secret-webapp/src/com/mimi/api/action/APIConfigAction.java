package com.mimi.api.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mimi.core.service.impl.ConfigServiceImpl;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Config;
import com.mimi.model.Response;
import com.opensymphony.xwork2.ModelDriven;

public class APIConfigAction extends SuperAction implements ModelDriven<Config>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Logger log = Logger.getLogger(APICommentAction.class);
	@Autowired
	private ConfigServiceImpl configServiceImpl;
    
	private Config config = new Config();

	 
	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfigServiceImpl(ConfigServiceImpl configServiceImpl) {
		this.configServiceImpl = configServiceImpl;
	}
	 
	public String queryConfig() throws IOException 
	{
		Response response = new Response();
		List<Config> list = null;
		try
		{
			  list =  configServiceImpl.queryConfigByDomain(config.getDomain());
		}catch(Exception e)
		{
			//e.printStackTrace();
			response.setKey(9);
			log.error(e.getMessage(), e);
		}
		response.setDataList(list);
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		out.print(new Gson().toJson(response));
		return null;
	}

 
	public Config getModel() {
		// TODO Auto-generated method stub
		return config;
	}

}
