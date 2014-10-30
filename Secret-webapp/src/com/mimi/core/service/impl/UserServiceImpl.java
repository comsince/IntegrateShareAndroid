package com.mimi.core.service.impl;

import org.apache.log4j.Logger;

import com.mimi.core.dao.impl.UserDaoImpl;
import com.mimi.core.service.UserService;
import com.mimi.model.Page;
import com.mimi.model.User;
import com.mimi.util.MD5;

public class UserServiceImpl implements UserService {

	private static final Logger log=Logger.getLogger(UserServiceImpl.class);

	private UserDaoImpl userDao;

	//@Transactional(propagation=Propagation.REQUIRED)
	public void addOneUser(User User){
		log.info("一个用户注册!");
		userDao.addOneUser(User);
	}


	 

	//@Transactional(propagation=Propagation.REQUIRED)
	public void deleteUserById(String userId) {
		userDao.deleteUserById(userId);

	}

	//@Transactional(readOnly=true)
	public User queryUser(String userId) {
		User User=userDao.queryUser(userId);

		return User;
	}

 

	//@Transactional(propagation=Propagation.REQUIRED)
	public void updateUser(User User) {
		userDao.updateUser(User);
	}


	
	//@Transactional(propagation=Propagation.REQUIRED)
	public User login(User user) {
		log.info("一个用户登陆!");
		String password=user.getPassword();
		user=queryUserByAlias(user.getAlias());
		if(user !=null)
		{
			
			if(!MD5.getMD5Encoding(password).equals(user.getPassword()))
			{
				return null;
			}
			
		}
		return user;
	}




	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}




	@Override
	public boolean aliasIsSingle(String alias,String userId) {
		return this.userDao.aliasIsSingle( alias,userId);
	}




	@Override
	public User queryByIMEI(String imei) {
		// TODO Auto-generated method stub
		return userDao.queryByIMEI(imei);
	}




	@Override
	public User queryUserByAlias(String alias) {
		// TODO Auto-generated method stub
		return userDao.queryUserByAlias(alias);
	}




	@Override
	public Page queryUserList(User user, Page page) {
		 int count = this.userDao.queryUserAmount(user);
		    page.setCount(Integer.valueOf(count));
		    if(page.getCount()==0)
		    {
		    	return page;
		    }
		    return this.userDao.queryUserList(user, page);
	}



	//@Transactional(readOnly=true)
	public int queryUserAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

 

}
