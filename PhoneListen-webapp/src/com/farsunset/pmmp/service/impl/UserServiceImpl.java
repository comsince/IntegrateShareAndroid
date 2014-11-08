package com.farsunset.pmmp.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.farsunset.framework.web.Page;
import com.farsunset.pmmp.common.Constants;
import com.farsunset.pmmp.dao.UserDaoImpl;
import com.farsunset.pmmp.model.User;
import com.farsunset.pmmp.service.UserService;

/**
 * 
 * @author 3979434
 */
public class UserServiceImpl implements UserService {

	protected final Log log = LogFactory.getLog(getClass());

	@Resource
	private UserDaoImpl userDao;

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return userDao.getAll();
	}
	 

	public User getUserByAccount(String account) {

		return userDao.queryByAccount(account);

	}

	public void update(User user) {
		userDao.update(user);
	}

	 

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	 

	@Override
	public HashMap<String, Object> getLogin(String account, String password) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		User target = userDao.queryByAccount(account);
		if(target==null)
		{
			map.put("error", "账号不存在!");
			map.put("code", 404);
		}else
		{
			if(password.equals(target.getPassword()) ||DigestUtils.md5Hex(password).equals(target.getPassword()))
			{
				map.put("code", 200);
				map.put("user", target);
			}else
			{
				map.put("code", 403);
				map.put("error", "账号或者密码不正确!");
			}
		}
		return map;
	}

	public static void main(String[] a)
	{
		System.out.print(DigestUtils.md5Hex("123456"));
	}
	@Override
	public void save(User user) {
		 
		if(user.getPassword()==null)
		{
		  user.setPassword(DigestUtils.md5Hex(Constants.Common.NORMAL_PASSWORD));
		}
		user.setCreateTime(String.valueOf(System.currentTimeMillis()));
		userDao.save(user);
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


	@Override
	public void delete(String account) {
		User u = new User();
		u.setAccount(account);
		userDao.delete(u);
		
	}


	@Override
	public List<User> queryUserList(User user) {
		// TODO Auto-generated method stub
		return userDao.queryUserList(user);
	}


 
}
