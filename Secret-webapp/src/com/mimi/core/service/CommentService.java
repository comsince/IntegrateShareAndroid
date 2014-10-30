package com.mimi.core.service;

import com.mimi.model.Comment;
import com.mimi.model.Page;

public abstract interface CommentService
{
	  public abstract void addOneComment(Comment comment);

	  public abstract void deleteCommentById(String commentId);

	  public abstract Comment queryCommentById(String commentId);

	  public abstract void updateComment(Comment comment);

	  public abstract int queryCommentAmount( Comment comment);

	  public abstract Page queryCommentByPage(Comment comment, Page page);
}