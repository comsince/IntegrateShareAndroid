package com.mimi.core.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mimi.core.jdbc.JDBCUtil;
import com.mimi.model.Comment;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class CommentDaoImpl  
  
{
  public void addOneComment(Comment comment)
  {
	    Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		 
		String sql = "insert into T_COMMENT(commentId,userId,matterId,content,alias,timestamp,rank) values(?,?,?,?,?,?,?)";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, comment.getCommentId());
			ps.setString(2, comment.getUserId());
			ps.setString(3, comment.getMatterId());
			ps.setString(4, comment.getContent());
			ps.setString(5, comment.getAlias());
			ps.setString(6, comment.getTimestamp());
			ps.setInt(7, comment.getRank());
			ps.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps);
		}
  }

  public void deleteCommentById(String commentId) {
	    Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		 
		String sql = "delete from T_COMMENT where commentId = ?";
		try {
		    ps = con.prepareStatement(sql);
			ps.setString(1, commentId);
			ps.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps);
		}
  }

  public Comment queryCommentById(String commentId) {
    /*Assert.notNull(commentId, "deleteCommentById>commentKey");
    Comment comment = new Comment();
    comment = (Comment)getHibernateTemplate().get(Comment.class, commentId);
    return comment;*/
	  return null;
  }

  public Page queryCommentByPage( Comment comment, Page page)
  {
	    List<Comment> dataList = new ArrayList<Comment>();
	  
	    Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		StringBuffer sql = new StringBuffer("select * from T_COMMENT where 1 = 1 ");
		if(!SystemUtil.isEmpty(comment.getMatterId()))
		{
			sql.append(" and matterId = '"+comment.getMatterId()+"'");
		}
		if(!SystemUtil.isEmpty(comment.getUserId()))
		{
			sql.append(" and userId = '"+comment.getUserId()+"'");
		}
		 
		sql.append(" order by "+page.getOrder() +" desc");
		sql.append(" limit "+(page.getCurrentPage() - 1) * page.size+","+page.size);
		try {
		    ps = con.prepareStatement(sql.toString());
			set = ps.executeQuery();
			while(set.next()){
				Comment t = mapingComment(set);
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

  public void updateComment(Comment comment)
  {
   // getHibernateTemplate().update(comment);
  }

  public int queryCommentAmount( Comment comment) {
	  Connection con = JDBCUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet set = null;
		StringBuffer sql = new StringBuffer("select count(*) from T_COMMENT where 1 = 1 ");
		if(!SystemUtil.isEmpty(comment.getMatterId()))
		{
			sql.append(" and matterId = '"+comment.getMatterId()+"'");
		}
		if(!SystemUtil.isEmpty(comment.getUserId()))
		{
			sql.append(" and userId = '"+comment.getUserId()+"'");
		}
		 
		try {
		    ps = con.prepareStatement(sql.toString());
			set = ps.executeQuery();
			while(set.next()){
				return set.getInt(1);
			}
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}finally{
			JDBCUtil.close(con, ps,set);
		}
		return 0;
  }
  private Comment mapingComment(ResultSet set) throws SQLException
  {
	  Comment comment = new Comment();
	  comment.setUserId(set.getString("userId"));
	  comment.setAlias(set.getString("alias"));
	  comment.setContent(set.getString("content"));
	  comment.setMatterId(set.getString("matterId"));
	  comment.setCommentId(set.getString("commentId"));
	  comment.setTimestamp(set.getString("timestamp"));
	  comment.setRank(set.getInt("rank"));
	return comment;
  }
  

 
}