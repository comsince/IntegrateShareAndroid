package com.mimi.core.service.impl;

import java.util.List;

import com.mimi.common.Constants;
import com.mimi.core.dao.impl.MessageDaoImpl;
import com.mimi.core.service.MessageService;
import com.mimi.model.Message;
import com.mimi.model.Page;

public class MessageServiceImpl
  implements MessageService
{
  private MessageDaoImpl messageDao;

  

  public void setMessageDao(MessageDaoImpl messageDao) {
	this.messageDao = messageDao;
}

public void addMessage(Message message)
  {

    message.setStatus(Constants.Common.STATUS_0);
    message.setMessageId(String.valueOf(System.currentTimeMillis()));
    message.setTimestamp(String.valueOf(System.currentTimeMillis()));
    try{
       this.messageDao.addOneMessage(message);
    }catch(Exception e)
    {
    	e.printStackTrace();
    }
  }

  public Page queryMessageByPage(Message message, Page page)
  {
    int count = this.messageDao.queryMessageAmount(message);
    page.setCount(Integer.valueOf(count));
    if(page.getCount()==0)
    {
    	return page;
    }
    return this.messageDao.queryMessageByPage(message, page);
  }

  public void deleteMessageById(String messageId)
  {
    this.messageDao.deleteMessageById(messageId);
  }

  public Message queryMessageById(String messageId)
  {
    Message message = this.messageDao.queryMessageById(messageId);

    return message;
  }

  public void updateMessage(Message message)
  {
    this.messageDao.updateMessage(message);
  }

@Override
public int queryMessageAmount(Message message) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public List<Message> queryMessageList(Message message) {
	// TODO Auto-generated method stub
	return this.messageDao.queryMessageList(message);
}

}