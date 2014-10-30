package com.mimi.admin.action;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.mimi.core.service.MatterService;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.util.SystemUtil;
import com.opensymphony.xwork2.ModelDriven;

public class AdminMatterAction extends SuperAction implements ModelDriven<Matter>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	private MatterService matterServiceImpl;
    
	private Matter matter = new Matter();
    
	public Matter getMatter() {
		return matter;
	}

	public void setMatter(Matter matter) {
		this.matter = matter;
	}

	public void setMatterServiceImpl(MatterService matterServiceImpl) {
		this.matterServiceImpl = matterServiceImpl;
	}
	
	 
	public String manage() throws IOException
	{
		 String pageIndex = request.getParameter("currentPage");
		 int currentPage = 1;
		 if(!SystemUtil.isEmpty(pageIndex))
		 {
			 currentPage = Integer.parseInt(pageIndex);
		 }
		Page page =  new Page();
		page.setCurrentPage(currentPage);
		page.setOrder("timestamp");
		if(matter==null)
			matter = new Matter();
		try{
		matterServiceImpl.queryMatterByPage(matter,page);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("page",page);
		return "manage";
	}
	
	public String search() throws IOException
	{
		 
		Page page =  new Page();
		page.setCurrentPage(1);
		page.setOrder("timestamp");
		if(matter==null)
			matter = new Matter();
		matterServiceImpl.queryMatterByPage(matter,page);
		request.setAttribute("page",page);
		request.setAttribute("matter",matter);
		return "manage";
	}
	
	 public void delete() throws IOException
	 {
		 String matterId = request.getParameter("matterId");
		 matterServiceImpl.deleteMatterById(matterId);
		 response.getWriter().print(1);
	 }
	 
	public Matter getModel() {
		// TODO Auto-generated method stub
		return matter;
	}
   
}
