package com.mimi.admin.action;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import com.mimi.core.service.CommentService;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Comment;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;
import com.opensymphony.xwork2.ModelDriven;

public class AdminCommentAction extends SuperAction implements ModelDriven<Comment>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
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
	
	 
	
	 
	  public String manage() throws Exception
	  {
		  
		 String pageIndex = request.getParameter("currentPage");
		 int currentPage = 1;
		 if(!SystemUtil.isEmpty(pageIndex))
		 {
			 currentPage = Integer.parseInt(pageIndex);
		 }
		if(comment==null)
			comment = new Comment();
	    Page page = new Page();
	    page.setOrder("timestamp");
	    page.setCurrentPage(Integer.valueOf(currentPage));
	    commentServiceImpl.queryCommentByPage(comment, page);
		request.setAttribute("page",page);
		request.setAttribute("comment",comment);
	    return "manage";
	  }
	
	
	  public String search() throws IOException
		{
			 
			Page page =  new Page();
			page.setOrder("timestamp");
			if(comment==null)
				comment = new Comment();
			 
			commentServiceImpl.queryCommentByPage(comment, page);
			request.setAttribute("page",page);
			request.setAttribute("comment",comment);
			return "manage";
		}
 
	 public void delete() throws IOException
	 {
		 String commentId = request.getParameter("commentId");
		 commentServiceImpl.deleteCommentById(commentId);
		  response.getWriter().print(1);
	 }
	 
	public Comment getModel() {
		// TODO Auto-generated method stub
		return comment;
	}
  
	 
    
}
