package com.mimi.api.action;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ModelDriven;
import com.mimi.core.service.CommentService;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Comment;
import com.mimi.model.Response;
import com.mimi.util.SystemUtil;

public class APICommentAction extends SuperAction implements ModelDriven<Comment>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(APICommentAction.class);
	
	@Autowired
	private CommentService commentServiceImpl;
	 
	private Comment comment = new Comment();

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public void setCommentServiceImpl(CommentService commentServiceImpl) {
		this.commentServiceImpl = commentServiceImpl;
	}
	
	public String publish() throws IOException
	{
		 Response rsp = new Response();
		 try{
		 commentServiceImpl.addOneComment(comment);
		 }catch(Exception e)
		 {
			 rsp.setKey(9);
			 log.error(e.getMessage(), e);
			 rsp.setMessage(e.getMessage());
		 }
		 response.setContentType("text/json;charset=UTF-8");
		 response.getWriter().print(new Gson().toJson(rsp));
		 return null;
	}
	
	 
	  public void queryJsonList() throws Exception
	  {
		Response rsp = new Response();
		 
	    page.setOrder(SystemUtil.isEmpty(PARAM_MAP.get("order"))?"timestamp":PARAM_MAP.get("order"));
	    try{
	    	page = this.commentServiceImpl.queryCommentByPage( this.comment, page);
		}catch(Exception e)
		{
		      rsp.setKey(9);
		      log.error(e.getMessage(), e);
		      rsp.setMessage(e.getMessage());
		}
	    rsp.setPage(page);
	    response.setContentType("text/json;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.print(new Gson().toJson(rsp));
	  }
	
	
	 
 
	 public void delete() throws IOException
	 {
		 String commentId = request.getParameter("commentId");
		 comment = commentServiceImpl.queryCommentById(commentId);
		 {
			 commentServiceImpl.deleteCommentById(comment.getCommentId());
		 }
		  response.getWriter().print(1);
	 }
	 
	public Comment getModel() {
		// TODO Auto-generated method stub
		return comment;
	}

	 
    
}
