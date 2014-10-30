package com.mimi.core.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mimi.core.jdbc.JDBCUtil;
import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class MatterDaoImpl  
  
{
  public void addOneMatter(Matter matter)
  {
	  Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into T_MATTER(matterId,userId,content,alias,timestamp,lastretime,type,recount,file,fileType,vote) values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, matter.getMatterId());
			ps.setString(2, matter.getUserId());
			ps.setString(3, matter.getContent());
			ps.setString(4, matter.getAlias());
			ps.setString(5, matter.getTimestamp());
			ps.setString(6, matter.getLastretime());
			ps.setString(7, matter.getType());
			ps.setInt(8, matter.getRecount());
			ps.setString(9, matter.getFile());
			ps.setString(10, matter.getFileType());
			ps.setString(11, matter.getVote());
			ps.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps);
		}
  }

  public void deleteMatterById(String matterId) {
	    Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "delete from T_MATTER where matterId = ?";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, matterId);
			ps.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps);
		}
  }

  public Matter queryMatterById(String matterId) {
       return null;
  }

  public Page queryMatterByPage( Matter matter, Page page)
  {
	   List<Matter> dataList = new ArrayList<Matter>();
	  
	    Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		StringBuffer sql = new StringBuffer("select * from T_MATTER where 1 = 1 ");
		if(!SystemUtil.isEmpty(matter.getType()))
		{
			sql.append(" and type = '"+matter.getType()+"'");
		}
		if(!SystemUtil.isEmpty(matter.getMatterId()))
		{
			sql.append(" and matterId = '"+matter.getMatterId()+"'");
		}
		if(!SystemUtil.isEmpty(matter.getUserId()))
		{
			sql.append(" and userId = '"+matter.getUserId()+"'");
		}
		sql.append(" order by "+page.getOrder() +" desc");
		sql.append(" limit "+(page.getCurrentPage() - 1) * page.size+","+page.size);
		try {
		    ps = con.prepareStatement(sql.toString());
			set = ps.executeQuery();
			while(set.next()){
				Matter t = mapingMatter(set);
				dataList.add(t);
			}
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps,set);
		}
		page.setDataList(dataList);
		
    return page;
  }
  
  public void updateMatter(Matter matter)
  {
   // getHibernateTemplate().update(matter);
  }

  public int queryMatterAmount( Matter matter) {
	  Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		StringBuffer sql = new StringBuffer("select count(*)  from T_MATTER where 1 = 1 ");
		if(!SystemUtil.isEmpty(matter.getType()))
		{
			sql.append(" and type = '"+matter.getType()+"'");
		}
		if(!SystemUtil.isEmpty(matter.getMatterId()))
		{
			sql.append(" and matterId = '"+matter.getMatterId()+"'");
		}
		if(!SystemUtil.isEmpty(matter.getUserId()))
		{
			sql.append(" and userId = '"+matter.getUserId()+"'");
		}
		try {
		    ps = con.prepareStatement(sql.toString());
			set = ps.executeQuery();
			while(set.next()){
				Matter t = mapingMatter(set);
				return set.getInt(1);
			}
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps,set);
		}
		return 0;
  }

  private Matter mapingMatter(ResultSet set) throws SQLException
  {
	  Matter matter = new Matter();
	  matter.setUserId(set.getString("userId"));
	  matter.setAlias(set.getString("alias"));
	  matter.setContent(set.getString("content"));
	  matter.setLastretime(set.getString("lastretime"));
	  matter.setMatterId(set.getString("matterId"));
	  matter.setTimestamp(set.getString("timestamp"));
	  
	  matter.setRecount(set.getInt("recount"));
	  matter.setType(set.getString("type"));
	  matter.setFile(set.getString("file"));
	  matter.setFileType(set.getString("fileType"));
	  matter.setVote(set.getString("vote"));
	return matter;
  }
 
 
}