package com.mimi.core.service;

import java.util.List;

import com.mimi.model.Message;
import com.mimi.model.Page;

public abstract interface MessageService
{
	  public abstract void addMessage(Message message);

	  public abstract void deleteMessageById(String messageId);

	  public abstract Message queryMessageById(String messageId);

	  public abstract void updateMessage(Message message);

	  public abstract int queryMessageAmount( Message message);

	  public abstract List<Message> queryMessageList(Message message);
	  
	  public abstract Page queryMessageByPage(Message message, Page page);
}