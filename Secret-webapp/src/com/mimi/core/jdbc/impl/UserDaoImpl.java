package com.mimi.core.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mimi.core.jdbc.JDBCUtil;
import com.mimi.model.Page;
import com.mimi.model.User;


public class UserDaoImpl{

	public void addOneUser(User user) {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into T_USER(userId,alias,password,imei,status,createTime) values(?,?,?,?,?,?)";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, user.getUserId());
			ps.setString(2, user.getAlias());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getImei());
			ps.setString(5, user.getStatus());
			ps.setString(6, user.getCreateTime());
			ps.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps);
		}
	 
	}

	public void deleteUserById(String userId) {
		    Connection con = JDBCUtil.getConnection();
			PreparedStatement ps = null;
			 
			String sql = "delete from T_COMMENT where userId = ?";
			try {
			    ps = con.prepareStatement(sql);
				ps.setString(1, userId);
				ps.execute();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				JDBCUtil.close(con, ps);
			}
	}

	 

	public User queryUser(String userId) {
		User user = null;
		Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		String sql = "select * from T_USER where userId = ?";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			set = ps.executeQuery();
			while(set.next()){
				user = mapingUser(set);
				 break;
			}
			
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps,set);
		}
		return user;
	}

	public User queryUserByAlias(String alias) {
		User user = null;
		Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		String sql = "select * from T_USER where alias = ?";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, alias);
			set = ps.executeQuery();
			while(set.next()){
				user = mapingUser(set);
				 break;
			}
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps,set);
		}
		return user;
	}

	public void updateUser(User user) {

		Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "update  T_USER set alias=?,password=?,status=? where userId=?";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, user.getAlias());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getStatus());
			ps.setString(4, user.getUserId());
			ps.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps);
		}
	}


	public boolean aliasIsSingle(String alias,String userId) {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		String sql = "select * from T_USER where alias = ? and userId!=?";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, alias);
			ps.setString(2, userId);
			set = ps.executeQuery();
			while(set.next()){
				return true;
			}
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps,set);
		}
		return false;
	}

	public User queryByIMEI(String imei) {
		User user = null;
		Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		String sql = "select * from T_USER where imei = ?";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, imei);
			set = ps.executeQuery();
			while(set.next()){
				user = mapingUser(set);
				 break;
			}
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps,set);
		}
		return user;
	}

	public Page queryUserList(User user, Page page) {
		 
		/*List<User> list = new ArrayList<User>();
		Criteria criteria = mapingParam( user);
	    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
	    criteria.setMaxResults(page.size);
	    criteria.addOrder(Order.desc(page.getOrder()));
	    try{
	      page.setDataList(criteria.list());
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return page;*/
		return null;
	}
	public int queryUserAmount(User user) {
		/* Criteria criteria = mapingParam( user);
		 return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());*/
		return 2;
	}
	private User mapingUser(ResultSet set) throws SQLException
	  {
		User user = new User();
		user.setUserId(set.getString("userId"));
		user.setAlias(set.getString("alias"));
		user.setPassword(set.getString("password"));
		user.setImei(set.getString("imei"));
		user.setStatus(set.getString("status"));
		user.setCreateTime(set.getString("createTime"));
		return user;
	  }



}
