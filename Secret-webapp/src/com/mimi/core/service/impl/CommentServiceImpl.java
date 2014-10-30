package com.mimi.core.service.impl;


import com.mimi.core.dao.impl.CommentDaoImpl;
import com.mimi.core.dao.impl.MatterDaoImpl;
import com.mimi.core.service.CommentService;
import com.mimi.model.Comment;
import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class CommentServiceImpl
  implements CommentService
{
  private CommentDaoImpl commentDao;
  
  private MatterDaoImpl matterDao;
  

  public void setCommentDao(CommentDaoImpl commentDao) {
	this.commentDao = commentDao;
}

public void setMatterDao(MatterDaoImpl matterDao) {
	this.matterDao = matterDao;
}

public void addOneComment(Comment comment)
  {

    	Matter matter = matterDao.queryMatterById(comment.getMatterId());
        comment.setCommentId(SystemUtil.getSequenceId());
        comment.setRank(matter.getRecount() + 1);
        comment.setTimestamp(String.valueOf(System.currentTimeMillis()));
        matter.setRecount(comment.getRank());
        matter.setLastretime(comment.getTimestamp());
        this.commentDao.addOneComment(comment);
        matterDao.updateMatter(matter);
     
  }

  public Page queryCommentByPage(Comment comment, Page page)
  {
    int count = this.commentDao.queryCommentAmount(comment);
    page.setCount(Integer.valueOf(count));
    if(page.getCount()==0)
    {
    	return page;
    }
    return this.commentDao.queryCommentByPage(comment, page);
  }

  public void deleteCommentById(String commentId)
  {
    this.commentDao.deleteCommentById(commentId);
  }

  public Comment queryCommentById(String commentId)
  {
    Comment comment = this.commentDao.queryCommentById(commentId);

    return comment;
  }
   /**
    * 
    */
  public void updateComment(Comment comment)
  {
    this.commentDao.updateComment(comment);
  }


	@Override
	public int queryCommentAmount(Comment comment) {
		// TODO Auto-generated method stub
		return 0;
	}
}