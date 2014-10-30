package com.mimi.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.mimi.model.Message;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class MessageDaoImpl extends HibernateDaoSupport
  
{
  public void addOneMessage(Message message)
  {
    getHibernateTemplate().save(message);
  }

  public void deleteMessageById(String messageId) {
    Assert.notNull(messageId, "deleteMessageById>sequenceId");
    Message message = new Message();
    message.setMessageId(messageId);
    getHibernateTemplate().delete(message);
  }

  public Message queryMessageById(String messageId) {
    Assert.notNull(messageId, "deleteMessageById>messageKey");
    Message message = new Message();
    message = (Message)getHibernateTemplate().get(Message.class, messageId);
    return message;
  }

  public Page queryMessageByPage( Message message, Page page)
  {
    Criteria criteria = mapingParam( message);
    criteria.setFirstResult((page.getCurrentPage() - 1) * page.size);
    criteria.setMaxResults(page.size);
    criteria.addOrder(Order.desc("createTime"));
    page.setDataList(criteria.list());
    return page;
  }

  public void updateMessage(Message message)
  {
    getHibernateTemplate().update(message);
  }

  public int queryMessageAmount( Message message) {
    Criteria criteria = mapingParam( message);
    return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
  }

  private Criteria mapingParam(Message message)
  {
    Criteria criteria = getSession().createCriteria(Message.class);
    if (!SystemUtil.isEmpty(message.getSenderId()))
    {
      criteria.add(Restrictions.eq("senderId", message.getSenderId()));
    }
    if (!SystemUtil.isEmpty(message.getStatus()))
    {
      criteria.add(Restrictions.eq("status", message.getStatus()));
    }
    if ( null != message.getReceiveId())
    {
      criteria.add(Restrictions.eq("receiveId",message.getReceiveId()));
    }
    return criteria;
  }

public List<Message> queryMessageList(Message message) {
	Criteria criteria = mapingParam( message);
	criteria.addOrder(Order.desc("timestamp"));
	return criteria.list();
}

 
}