package com.mimi.core.web;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.mimi.model.Page;
import com.mimi.util.SystemUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SuperAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public HttpServletRequest request;
	public HttpServletResponse response;
	public HttpSession session;
	public Page page;
	public HashMap<String,String> PARAM_MAP =  new HashMap<String,String> ();
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	public HttpServletResponse getResponse() {
		return  ServletActionContext.getResponse();
	}
	public HttpSession getSession() {
		return  ServletActionContext.getRequest().getSession();
	}
	 
	public void validate()  
	{
		if(null==request)
		request = ServletActionContext.getRequest();
		if(null==response)
		response = ServletActionContext.getResponse();
		if(null==session)
		session = request.getSession();
		page = new Page();
		String pageIndex = request.getParameter("currentPage");
		int currentPage = 1;
		if (!SystemUtil.isEmpty(pageIndex)) {
			currentPage = Integer.parseInt(pageIndex);
		}
		page.setCurrentPage(currentPage);
		
		PARAM_MAP.clear();
		Enumeration<String>  dataMap = request.getParameterNames();
	    while(dataMap.hasMoreElements())
	    {
	    	  String key = dataMap.nextElement();
	    	  String value = ((HttpServletRequest)request).getParameter(key);
	    	  PARAM_MAP.put(key, value);
	    }
	}
	
	 

}
