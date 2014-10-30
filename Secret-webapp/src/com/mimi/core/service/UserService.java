package com.mimi.core.service;

import com.mimi.model.Page;
import com.mimi.model.User;

public abstract interface UserService
{
	public void addOneUser(User User);

	public void deleteUserById(String userId);

	public User queryUser(String userId);

	public User queryUserByAlias(String alias);
	
	public   Page queryUserList(User user, Page page);
	  
	public boolean aliasIsSingle(String alias,String userId);

	public void updateUser(User User);


	public int queryUserAmount();
	
	public User login(User user);

	public User queryByIMEI(String imei);

}