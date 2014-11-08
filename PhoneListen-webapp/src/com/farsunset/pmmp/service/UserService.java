/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.farsunset.pmmp.service;

import java.util.HashMap;
import java.util.List;

import com.farsunset.framework.web.Page;
import com.farsunset.pmmp.model.User;

/** 
 * Business service interface for the user management.
 *
 * @author farsunset (3979434@qq.com)
 */
public interface UserService {

    public void update(User user);
    
    public void save(User user);
    
    
    public List<User> getAllUsers();

    public User getUserByAccount(String account);

  
	public   Page queryUserList(User user, Page page);
	
	public HashMap<String, Object> getLogin(String account,String password);

	public void delete(String account);

	public List<User>  queryUserList(User user);

	 
}
