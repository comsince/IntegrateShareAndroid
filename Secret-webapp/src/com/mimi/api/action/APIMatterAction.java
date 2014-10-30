package com.mimi.api.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mimi.common.Constants;
import com.mimi.core.service.MatterService;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Matter;
import com.mimi.model.Response;
import com.opensymphony.xwork2.ModelDriven;

public class APIMatterAction extends SuperAction implements ModelDriven<Matter>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(APIMatterAction.class);
	
	@Autowired
	private MatterService matterServiceImpl;
    
	private Matter matter = new Matter();
    
	 
	public void setMatter(Matter matter) {
		this.matter = matter;
	}

	public Matter getMatter() {
		return matter;
	}

	public void setMatterServiceImpl(MatterService matterServiceImpl) {
		this.matterServiceImpl = matterServiceImpl;
	}
	
	 private static HashMap<String,String> dirMap = new HashMap<String,String>();;
	    static{
	    	
	    	dirMap.put("1", "fileImage");
	    	dirMap.put("2", "fileVoice");
	    	dirMap.put("3", "fileGif");
	    	dirMap.put("4", "fileVideo");
	    }
	
	public String publish() throws IOException
	{
		matter.setType(Constants.Common.TYPR_1);
		matter.setVote("00000000000000000000");
		try{
			if(ServletActionContext.getRequest() instanceof MultiPartRequestWrapper){
				MultiPartRequestWrapper pr = (MultiPartRequestWrapper)ServletActionContext.getRequest();
			    if(pr.getFiles("file")!=null&&pr.getFiles("file").length > 0)
				{
						String uuid=UUID.randomUUID().toString().replaceAll("-", "");
					    File file = pr.getFiles("file")[0];
					    String dir = dirMap.get(matter.getFileType());
					    String path = ServletActionContext.getServletContext().getRealPath(dir);
					    File des = new File(path+"/"+uuid);
					    FileUtil.copyFile(file, des);
					    matter.setFile(dir+"/"+uuid);
				 }
			}	
			matterServiceImpl.addOneMatter(matter);
		}catch(Exception e)
		{
			//e.printStackTrace();
			log.error(e.getMessage(), e);
			response.getWriter().print(new Gson().toJson(9));
			return null;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().print(new Gson().toJson(matter));
		return null;
	}
	public String fadeBack() throws IOException
	{
		Response rsp = new Response();
		matter.setType(Constants.Common.TYPR_9);
		matter.setAlias("反馈");
		try{
		matterServiceImpl.addOneMatter(matter);
		}catch(Exception e)
		{
			//e.printStackTrace();
			rsp.setKey(9);
			log.error(e.getMessage(), e);
			rsp.setMessage(e.getMessage());
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().print(new Gson().toJson(rsp));
		return null;
	}
	public String doVote() throws IOException
	{
		Response rsp = new Response();
		Integer key  = Integer.parseInt(request.getParameter("key"));
		try{
			
			matter = matterServiceImpl.queryMatterById(matter.getMatterId());
			StringBuffer vote = new StringBuffer (matter.getVote());
			String value = vote.substring((key-1)*4, key*4);
			
			int n =Integer.parseInt(value,16)+1;
			String v = "000"+Integer.toHexString(n);
			vote.setCharAt((key-1)*4, v.charAt(v.length()-4));
			vote.setCharAt((key-1)*4+1, v.charAt(v.length()-3));
			vote.setCharAt((key-1)*4+2, v.charAt(v.length()-2));
			vote.setCharAt((key-1)*4+3, v.charAt(v.length()-1));
			matter.setVote(vote.toString());
		    matterServiceImpl.updateMatter(matter);
	 	}catch(Exception e)
	 	{
	 		//e.printStackTrace();
	 		rsp.setKey(9);
	 		log.error(e.getMessage(), e);
	 		rsp.setMessage(e.getMessage());
	 	}
	 	rsp.setMatter(matter);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().print(new Gson().toJson(rsp));
		return null;
	}
	 
	public String list() throws IOException
	{
		 
		try{
			PARAM_MAP.put("type",Constants.Common.TYPR_1);
			matterServiceImpl.queryMatterByPage(this.PARAM_MAP,page);
		 }catch(Exception e)
		 {
		 		log.error(e.getMessage(), e);
		 }
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().print(new Gson().toJson(page.getDataList()));
		return null;
	}
	 
	
	public String myMatterList() throws IOException
	{
		
		response.setContentType("text/json;charset=UTF-8");
		List<Matter>  list = matterServiceImpl.querySelfMatterList(matter.getUserId());
		
		response.getWriter().print(new Gson().toJson(list));
		 
		return null;
	}
	
	public String myFllowList() throws IOException
	{
		response.setContentType("text/json;charset=UTF-8");
		List<Matter> list  = matterServiceImpl.queryFllowedMatterList(matter.getUserId());
		response.getWriter().print(new Gson().toJson(list));
		return null;
	}
	 public String detailed() throws IOException
	 {
		 
		 matter = matterServiceImpl.queryMatterById(matter.getMatterId());
		 
		 response.setContentType("text/json;charset=UTF-8");
		 response.getWriter().print(new Gson().toJson(matter));
		 return null;
	 }
	 
	 public void delete() throws IOException
	 {
		    Response rsp = new Response();
			 try{
				 matterServiceImpl.deleteMatterById(matter.getMatterId());
			 }catch(Exception e)
			 {
			 		//e.printStackTrace();
			 		rsp.setKey(9);
			 		log.error(e.getMessage(), e);
			 		rsp.setMessage(e.getMessage());
			 }
			 rsp.setMatter(matter);
			 response.setContentType("text/json;charset=UTF-8");
			 response.getWriter().print(new Gson().toJson(rsp.getKey()));
	 }
	 
	public Matter getModel() {
		// TODO Auto-generated method stub
		return matter;
	}

}
