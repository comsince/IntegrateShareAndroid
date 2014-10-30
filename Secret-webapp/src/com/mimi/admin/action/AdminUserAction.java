package com.mimi.admin.action;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import com.mimi.core.service.UserService;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Page;
import com.mimi.model.User;
import com.mimi.util.SystemUtil;
import com.opensymphony.xwork2.ModelDriven;
public class AdminUserAction extends SuperAction implements ModelDriven<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user = new User();
	
	@Autowired
	private UserService userServiceImpl;
	
	public void setUserServiceImpl(UserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}
	 
	 
	 
	public String manage() throws IOException
	{
		 String pageIndex = request.getParameter("currentPage");
		 int currentPage = 1;
		 if(!SystemUtil.isEmpty(pageIndex))
		 {
			 currentPage = Integer.parseInt(pageIndex);
		 }
		Page page =  new Page();
		page.setCurrentPage(currentPage);
		page.setOrder("userId");
		if(user==null)
			user = new User();
		 
		userServiceImpl.queryUserList(user, page);
		request.setAttribute("page",page);
		return "manage";
	}
	 
	  public String search() throws IOException
		{
			 
		   // String alias = request.getParameter("user.alias");
		   // String imei = request.getParameter("user.imei");
			Page page =  new Page();
			page.setOrder("userId");
			if(user==null)
				user = new User();
			//user.setAlias(alias);
			//user.setImei(imei);
			userServiceImpl.queryUserList(user, page);
			request.setAttribute("page",page);
			request.setAttribute("user",user);
			return "manage";
		}
	  
	  
	  public void delete() throws IOException
		 {
			 String userId = request.getParameter("userId");
			 userServiceImpl.deleteUserById(userId);
			 response.getWriter().print(1);
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
