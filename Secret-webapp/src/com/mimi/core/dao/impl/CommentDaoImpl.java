package com.mimi.core.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.mimi.model.Comment;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class CommentDaoImpl extends HibernateDaoSupport
  
{
  public void addOneComment(Comment comment)
  {
    getHibernateTemplate().save(comment);
  }

  public void deleteCommentById(String commentId) {
    Assert.notNull(commentId, "deleteCommentById>sequenceId");
    Comment comment = new Comment();
    comment.setCommentId(commentId);
    getHibernateTemplate().delete(comment);
  }

  public Comment queryCommentById(String commentId) {
    Assert.notNull(commentId, "deleteCommentById>commentKey");
    Comment comment = new Comment();
    comment = (Comment)getHibernateTemplate().get(Comment.class, commentId);
    return comment;
  }

  public Page queryCommentByPage( Comment comment, Page page)
  {
    Criteria criteria = mapingParam( comment);
    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
    criteria.setMaxResults(page.size);
    criteria.addOrder(Order.desc(page.getOrder()));
    page.setDataList(criteria.list());
    return page;
  }

  public void updateComment(Comment comment)
  {
    getHibernateTemplate().update(comment);
  }

  public int queryCommentAmount( Comment comment) {
    Criteria criteria = mapingParam( comment);
    return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
  }

  private Criteria mapingParam(Comment comment)
  {
    Criteria criteria = getSession().createCriteria(Comment.class);
    if (!SystemUtil.isEmpty(comment.getMatterId()))
    {
      criteria.add(Restrictions.eq("matterId", comment.getMatterId()));
    }
     
    if (!SystemUtil.isEmpty(comment.getUserId()))
    {
      criteria.add(Restrictions.eq("userId",comment.getUserId()));
    }
    if (!SystemUtil.isEmpty(comment.getAlias()))
    {
      criteria.add(Restrictions.eq("alias",comment.getAlias()));
    }
    return criteria;
  }

public void deleteCommentByMatterId(String matterId) {
	Query query = getSession().createSQLQuery("delete from t_comment where matterId=?");
	query.setString(0, matterId);
	query.executeUpdate();
}

 
}