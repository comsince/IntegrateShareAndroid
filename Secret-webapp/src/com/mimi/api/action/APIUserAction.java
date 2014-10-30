package com.mimi.api.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mimi.common.Constants;
import com.mimi.core.service.UserService;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Response;
import com.mimi.model.User;
import com.mimi.util.MD5;
import com.mimi.util.SystemUtil;
import com.opensymphony.xwork2.ModelDriven;
public class APIUserAction extends SuperAction implements ModelDriven<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(APIMatterAction.class);
	private User user = new User();
	
	@Autowired
	private UserService userServiceImpl;
	

	public void setUserServiceImpl(UserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}
	 
	public String register() throws IOException
	{
		
		String alias = request.getParameter("alias");
		Response rsp = new Response();
		try{
			User u = userServiceImpl.queryUserByAlias(alias);
			if(u!=null)
			{
				rsp.setKey(1);
				return null;
			}
			user.setAlias(alias);
			user.setStatus(Constants.Common.STATUS_1);
			user.setUserId(SystemUtil.getSequenceId());
			user.setPassword(MD5.getMD5Encoding("000000"));
			user.setCreateTime(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
			userServiceImpl.addOneUser(user) ;
			rsp.setUser(user);
			
	 	}catch(Exception e)
	 	{
	 		rsp.setKey(9);
	 		log.error(e.getMessage(), e);
	 		rsp.setMessage(e.getMessage());
	 	}
	 	response.setContentType("text/json;charset=UTF-8");
	 	response.getWriter().print(new Gson().toJson(rsp));
		return null;
	}

	public String login() throws IOException
	{
		 
		
		Response rsp = new Response();
		User target = userServiceImpl.login(user);
		if(target == null)
		{
			rsp.setKey(2);
		}
		response.setContentType("text/json;charset=UTF-8");
		rsp.setUser(target) ;
		response.getWriter().print(new Gson().toJson(rsp));
		
		return null;
	}
	
	public String count() throws IOException
	{
		int count = userServiceImpl.queryUserAmount();
		response.getWriter().print(count);
		return null;
	}
	
	public String modifyPassword() throws IOException
	{
		Response rsp = new Response();
		User target = userServiceImpl.queryUser(user.getUserId());
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		if(!target.getPassword().equals(MD5.getMD5Encoding(oldPassword)))
		{
			rsp.setKey(2);
		}
		target.setPassword(MD5.getMD5Encoding(newPassword));
		userServiceImpl.updateUser(target);
		response.getWriter().print(rsp);
		return null;
	}
	
	public String modifyAlias() throws IOException
	{
		String alias = request.getParameter("user.alias");
		Response rsp = new Response();
		User active = userServiceImpl.queryUser(user.getUserId());
		active.setAlias(alias);
		userServiceImpl.updateUser(active);
		response.getWriter().print(new Gson().toJson(rsp));
		return null;
	} 
	 
	public void getByUserId() throws IOException
	{
		String userId = request.getParameter("userId");
		User target = userServiceImpl.queryUser(userId);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().print(new Gson().toJson(target));
	}
	 
	public void getByIMEI() throws IOException
	{
		Response rsp = new Response();
		User target = userServiceImpl.queryByIMEI(user.getImei());
		response.setContentType("text/json;charset=UTF-8");
		rsp.setUser(target) ;
		response.getWriter().print(new Gson().toJson(rsp));
	}
	public User getModel() {
		// TODO Auto-generated method stub
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
    
}
