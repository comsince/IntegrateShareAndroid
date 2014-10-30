package com.mimi.core.web;


import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
public class VerifictionFilter extends StrutsPrepareAndExecuteFilter{

	    private FilterConfig filterConfig;
  
	 public void init(FilterConfig config) throws ServletException {
		  this.filterConfig = config;
	}

	 
	   private boolean doVerifiction(ServletRequest request, ServletResponse response) throws IOException, ServletException
	   {
		   String[] paths = ((HttpServletRequest)request).getRequestURI().split("/");
		   String url =paths[paths.length-1];
		   if(url.indexOf("jsp") != -1)
		   {
			   return true;
		   }
		   Object user = ( (HttpServletRequest)request).getSession().getAttribute("user");
		   if(null == user)
		   {
			   String targetPath = ((HttpServletRequest)request).getParameter("targetPath");
			   if(null != targetPath)
			   {
				      Enumeration<String>  dataMap = ((HttpServletRequest)request).getParameterNames();
				      while(dataMap.hasMoreElements())
				      {
				    	  String key = dataMap.nextElement();
				    	  String value = ((HttpServletRequest)request).getParameter(key);
				    	  ((HttpServletRequest)request).getSession().setAttribute( key,value);
				      }
				      if(targetPath.equals("true"))
				      {
				    	  ((HttpServletRequest)request).getSession().setAttribute( "targetPath",((HttpServletRequest)request).getRequestURI());
				      }
					 
			   }
			    String path = filterConfig.getServletContext().getInitParameter("website")+"/user/user_toLogin.php";
			    ((HttpServletRequest)request).getSession().setAttribute("css","tip_blue");
			    ((HttpServletRequest)request).getSession().setAttribute("message",URLDecoder.decode("请先登录,登录后将转到您操作的页面!", "UTF-8"));
			    ((HttpServletResponse)response).sendRedirect(path);
			   return false;
		   }
		  /* if(url.startsWith("user_") && !url.equals("user_login"))
		   {
			   WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		       BlackListServiceImpl	blackListServiceImpl =(BlackListServiceImpl) context.getBean("blackListServiceImpl");
		       String ip = HTTPUtil.getIP((HttpServletRequest)request);
		       BlackList  black= blackListServiceImpl.queryByIP(ip);
		       if(null!= black){
		    	   Response returnMsg = new Response();
		    	   returnMsg.setKey(Constants.Number.INT_403);
		    	   if(url.startsWith("matter_doAddMatter"))
			       {
		    		   response.getWriter().print(new Gson().toJson(returnMsg));
			       }
		    	   if(url.startsWith("matter_doAddPICMatter"))
			       {
		    		   String path = filterConfig.getServletContext().getInitParameter("website")+"/response.jsp";
		    		 ( (HttpServletRequest)request).getSession().setAttribute("response", new Gson().toJson(returnMsg));
		    		  ( (HttpServletResponse)response).sendRedirect(path);
			       }
		    	   return false;
		       }
		      
		   }*/
		   
		   return true;
	   }
	   
	   
	    public void doFilter(ServletRequest request, ServletResponse response,
	           FilterChain chain) {
	    	
	       
	           try {
	        	   if(	doVerifiction(request,response))
	    	       {
	        		   chain.doFilter(request, response);
	    	       }else
	    	       {
	    	    	   return;
	    	       }
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
	    }
	 
	    public void destroy() {
	       this.filterConfig = null;
	    }

		
}
