package com.mimi.core.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.jdbc.Work;

import com.mimi.core.dao.impl.CommentDaoImpl;
import com.mimi.core.dao.impl.MatterDaoImpl;
import com.mimi.core.service.MatterService;
import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class MatterServiceImpl implements MatterService
{
  private MatterDaoImpl matterDao;
  private CommentDaoImpl commentDao;
  

  public void setMatterDao(MatterDaoImpl matterDao) {
	this.matterDao = matterDao;
}

public void setCommentDao(CommentDaoImpl commentDao) {
	this.commentDao = commentDao;
}

public void addOneMatter(Matter matter)
  {

    matter.setMatterId(SystemUtil.getSequenceId());
    matter.setTimestamp(String.valueOf(System.currentTimeMillis()));
    this.matterDao.addOneMatter(matter);
  }

  public Page queryMatterByPage(Matter matter, Page page)
  {
    int count = this.matterDao.queryMatterAmount(matter);
    page.setCount(Integer.valueOf(count));
    if(page.getCount()==0)
    {
    	return page;
    }
    return this.matterDao.queryMatterByPage(matter, page);
  }

  public List<Matter> querySelfMatterList(String userId)
  {
   
    return this.matterDao.querySelfMatterList(userId);
  }
  
  
  public void deleteMatterById(String matterId)
  {
    this.matterDao.deleteMatterById(matterId);
    commentDao.deleteCommentByMatterId(matterId);
  }

  public Matter queryMatterById(String matterId)
  {
    Matter matter = this.matterDao.queryMatterById(matterId);

    return matter;
  }

  public void updateMatter(Matter matter)
  {
    this.matterDao.updateMatter(matter);
  }


	@Override
	public int queryMatterAmount(Matter matter) {
		// TODO Auto-generated method stub
		return 0;
	}

	 
	@Override
	public List<Matter> queryFllowedMatterList(final String userId) {
		final List<Matter> dataList = new ArrayList<Matter>();
		final Set<String> ids = new HashSet<String>();
		matterDao.getMSession().doWork(new Work(){
			@Override
			public void execute(Connection con) throws SQLException {
			  String sql = "select m.* from T_MATTER m,t_comment c where c.matterId = m.matterId and m.userId!=? and c.userId =?";
			  PreparedStatement ps = con.prepareStatement(sql);
			  ps.setString(1, userId);
			  ps.setString(2, userId);
			  ResultSet set = ps.executeQuery();
			  while(set.next())
			  {
				   if(ids.contains(set.getString("matterId")))
					   continue;
				   Matter m = new Matter();
				   m.setAlias(set.getString("alias"));
				   m.setUserId(set.getString("userId"));
				   m.setContent(set.getString("content"));
				   m.setMatterId(set.getString("matterId"));
				   m.setRecount(set.getInt("recount"));
				   m.setTimestamp(set.getString("timestamp"));
				   
				   m.setLastretime(set.getString("lastretime"));
				   m.setType(set.getString("type"));
				   m.setFileType(set.getString("fileType"));
				   m.setFile(set.getString("file"));
				   m.setVote(set.getString("vote"));
				   ids.add(m.getMatterId());
				   dataList.add(m);
			  }
			}
		});
		return dataList;
	}

	@Override
	public Page queryMatterByPage(HashMap<String, String> map, Page page) {
		int count = this.matterDao.queryMatterAmount(map);
	    page.setCount(Integer.valueOf(count));
	    if(page.getCount()==0)
	    {
	    	return page;
	    }
	    return this.matterDao.queryMatterByPage(map, page);
	}
}