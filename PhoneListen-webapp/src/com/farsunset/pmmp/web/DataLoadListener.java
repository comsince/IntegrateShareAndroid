package com.farsunset.pmmp.web;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.codec.digest.DigestUtils;

import com.farsunset.framework.container.ContextHolder;
import com.farsunset.pmmp.common.Constants;
import com.farsunset.pmmp.model.User;
import com.farsunset.pmmp.service.UserService;

public class DataLoadListener  implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		DataMap.loadPowerMap();
		
		UserService userService = (UserService) ContextHolder.getBean("userServiceImpl");
		User admin = userService.getUserByAccount("admin");
		if(admin==null)
		{
			admin = new User();
			admin.setAccount("admin");
			admin.setName("超级管理员");
			admin.setPassword(DigestUtils.md5Hex("admin"));
			admin.setPower(Constants.Common.SUPER_POWWER);
			userService.save(admin);
		}
		
		 

	}

	 
		
}
