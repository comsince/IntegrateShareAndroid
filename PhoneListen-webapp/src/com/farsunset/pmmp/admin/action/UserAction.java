package com.farsunset.pmmp.admin.action;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.farsunset.framework.tools.StringUtil;
import com.farsunset.framework.web.SuperAction;
import com.farsunset.pmmp.model.User;
import com.farsunset.pmmp.service.UserService;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction extends SuperAction implements ModelDriven<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user = new User();
	@Autowired
	private UserService userServiceImpl;

	public void setUserServiceImpl(UserService UserServiceImpl) {
		this.userServiceImpl = UserServiceImpl;
	}

	public void add() throws IOException {

		User u = userServiceImpl.getUserByAccount(user.getAccount());
		if(u!=null)
		{
			response.getWriter().print("1");
			return ;
		}
		userServiceImpl.save(user);
		response.getWriter().print("0");
	}

	public String toLogin() {

		return "login_view";
	}

	public void changeStatus() throws IOException {

		try {
			String account = request.getParameter("account");
			// //User u = userServiceImpl.queryUserByAccount(account);
			// u.setStatus(request.getParameter("status"));
			// userServiceImpl.updateUser(u);
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print(0);
		}
	}

	public String manage() throws IOException {

		userServiceImpl.queryUserList(user, page);
		request.setAttribute("page", page);
		request.setAttribute("user", user);
		return "manage";
	}

	public String gpsManage() throws IOException {

		User loginUser  = (User) session.getAttribute("user");
		if(!loginUser.getAccount().equals("admin"))
		{
			user.setMobile(user.getMobile());
		}
		userServiceImpl.queryUserList(user, page);
		request.setAttribute("page", page);
		request.setAttribute("user", user);
		return "gpsManage";
	}
	
	public String netManage() throws IOException {

		User loginUser  = (User) session.getAttribute("user");
		if(!loginUser.getAccount().equals("admin"))
		{
			user.setMobile(user.getMobile());
		}
		userServiceImpl.queryUserList(user, page);
		request.setAttribute("page", page);
		request.setAttribute("user", user);
		return "netManage";
	}
	
	public String milieuManage() throws IOException {

		User loginUser  = (User) session.getAttribute("user");
		if(!loginUser.getAccount().equals("admin"))
		{
			user.setMobile(user.getMobile());
		}
		userServiceImpl.queryUserList(user, page);
		request.setAttribute("page", page);
		request.setAttribute("user", user);
		return "milieuManage";
	}
	
	
	public String modify() throws Exception {

		User original = userServiceImpl.getUserByAccount(user.getAccount());
		
		if (StringUtil.isNotEmpty(user.getName())) {
			original.setName(user.getName());
		}
		if (StringUtil.isNotEmpty(user.getMobile())) {
			original.setMobile(user.getMobile());
		}
		 
		if (StringUtil.isNotEmpty(user.getGps())) {
			original.setGps(user.getGps());
		}
		
		if (StringUtil.isNotEmpty(user.getNet())) {
			original.setNet(user.getNet());
		}
		
		if (StringUtil.isNotEmpty(user.getDuration())) {
			original.setDuration(user.getDuration());
		}
		
		if (StringUtil.isNotEmpty(user.getMilieu())) {
			original.setMilieu(user.getMilieu());
		}
		
		userServiceImpl.update(original);
		response.getWriter().print(0);
		return null;
	}
	
	

	public void delete() throws IOException {
		String account = request.getParameter("account");
	    userServiceImpl.delete(account);
		response.getWriter().print(1);
	}

	public String logout() throws IOException {
		session.invalidate();
		return "loginFailed";
	}
	
	 
	
	public String login() throws IOException {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		user = userServiceImpl.getUserByAccount(account);
		if(user==null || !user.getPassword().equals(DigestUtils.md5Hex(password)))
		{
			
			session.setAttribute("message","账号在或密码不正确");
			return "loginFailed";
		}
		
		session.setAttribute("user", user);
		return "loginSuccess";
		
	}

	public String modifyPassword() throws Exception {

		response.setContentType("text/json;charset=UTF-8");

		User original = userServiceImpl.getUserByAccount(user.getAccount());
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		if (original.getPassword().endsWith(oldPassword)) {
			original.setPassword(newPassword);
			userServiceImpl.update(original);
			response.getWriter().print(200);
		} else {
			response.getWriter().print(403);
		}
		
		return null;
	}

	 
	 
	public User getModel() {
		// TODO Auto-generated method stub
		return user;
	}
}
