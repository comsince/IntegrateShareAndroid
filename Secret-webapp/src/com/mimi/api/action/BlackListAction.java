package com.mimi.api.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.mimi.core.service.impl.BlackListServiceImpl;
import com.mimi.model.BlackList;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BlackListAction extends ActionSupport implements ModelDriven<BlackList>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	private BlackListServiceImpl blackListServiceImpl;
    
	private BlackList black;

	public BlackList getModel() {
		// TODO Auto-generated method stub
		return black;
	}

}
