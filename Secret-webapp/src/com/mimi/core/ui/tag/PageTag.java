package com.mimi.core.ui.tag;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.mimi.model.Page;
import com.mimi.util.SystemUtil;
public class PageTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Page page;
	private String style;
	public int doStartTag(){   
		return EVAL_BODY_INCLUDE;   
	} 
	public int doEndTag() {
		if(page.getCountPage()==1)
		{
			return Tag.EVAL_PAGE;
		}
		JspWriter out = pageContext.getOut();
		try {
			if(SystemUtil.isEmpty(style))
			{
				out.println("<div class=\"clear\" style=\"margin:0 auto;height: 50px; width: 400px;padding-top: 20px;\">");
			}
			else
			{
				out.println("<div class=\"clear\" style=\"margin:0 auto;height: 50px;;padding-top: 20px;"+style+"\">");
			}
			
			
				out.println("<ul>");
				out.println("<li style='float: left;'>共"+page.getCount()+"条记录,"+page.getCountPage()+"页</li>");
				if(!page.isFristPage())
				{
					out.println("<li> <label  class='simple_button small page_button' onclick='gotoPage(1)'>首页</label></li>");
					out.println("<li> <label class='simple_button small page_button' onclick='gotoPage("+ (page.getCurrentPage()-1)+")'>上页</label></li>");
				}
				
				for(int i = page.getCurrentPage()-3;i<page.getCurrentPage()+3&&i<=page.getCountPage();i++)
				{
					if(i<1)
					{
						continue;
					}
					if(i==page.getCurrentPage())
					{
						out.println("<li> <label class='page_button_selected'>"+i+"</label></li>");
					}else
					{
						out.println("<li> <label class='simple_button small page_button' onclick='gotoPage("+i+")'>"+i+"</label></li>");
					}
					
				}
				if(!page.isLastPage())
				{
					out.println("<li> <label class='simple_button small page_button' onclick='gotoPage("+ (page.getCurrentPage()+1)+")'>下页</label></li>");
					out.println("<li> <label class='simple_button small page_button' onclick='gotoPage("+page.getCountPage()+")'>尾页</label></li>");
				}
					out.println("</ul>");
		  out.println("</div>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return Tag.EVAL_PAGE;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	 
}
