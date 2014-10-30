package com.mimi.api.action;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ModelDriven;
import com.mimi.core.service.MessageService;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Message;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;

public class APIMessageAction extends SuperAction implements ModelDriven<Message>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	private MessageService messageServiceImpl;
    
	private Message message;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void setMessageServiceImpl(MessageService messageServiceImpl) {
		this.messageServiceImpl = messageServiceImpl;
	}
	
	 
	 
	 
	  public void queryJsonList() throws Exception
	  {
		 String pageIndex = request.getParameter("currentPage");
		 int currentPage = 1;
		 if(!SystemUtil.isEmpty(pageIndex))
		 {
			 currentPage = Integer.parseInt(pageIndex);
		 }
		if(message==null)
			message = new Message();
	    Page page = new Page();
	    page.setCurrentPage(Integer.valueOf(currentPage));
	     
	    page = this.messageServiceImpl.queryMessageByPage( this.message, page);
	    response.setContentType("text/plain;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.print(new Gson().toJson(page));
	  }
	
	
	 
 
	 public void delete() throws IOException
	 {
		 String messageId = request.getParameter("messageId");
		 message = messageServiceImpl.queryMessageById(messageId);
		// if(message.getSenderId().longValue() == user.getUserId().longValue())
		 {
			 messageServiceImpl.deleteMessageById(message.getMessageId());
		 }
		  response.getWriter().print(1);
	 }
	 
     public String newMessageList() throws Exception
	  {
		 String pageIndex = request.getParameter("currentPage");
		 int currentPage = 1;
		 if(!SystemUtil.isEmpty(pageIndex))
		 {
			 currentPage = Integer.parseInt(pageIndex);
		 }
		 if(message==null)
				message = new Message();
	//	message.setReceiveId(super.user.getUserId());
		//message.setStatus(Constants.Common.STATUS_2);
	    Page page = new Page();
	    page.setCurrentPage(Integer.valueOf(currentPage));
	    page = this.messageServiceImpl.queryMessageByPage(message, page);
	    request.setAttribute("page", page);
	    return "message_panel";
	  }
	public Message getModel() {
		// TODO Auto-generated method stub
		return message;
	}

}
