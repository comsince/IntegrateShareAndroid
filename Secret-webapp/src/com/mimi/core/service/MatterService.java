package com.mimi.core.service;

import java.util.HashMap;
import java.util.List;

import com.mimi.model.Matter;
import com.mimi.model.Page;

public abstract interface MatterService
{
	  public abstract void addOneMatter(Matter matter);

	  public abstract void deleteMatterById(String matterId);

	  public abstract Matter queryMatterById(String matterId);

	  public abstract void updateMatter(Matter matter);

	  public abstract int queryMatterAmount( Matter matter);

	  public abstract Page queryMatterByPage(Matter matter, Page paramPage);
	  
	  public abstract Page queryMatterByPage(HashMap<String,String> map, Page page);
	 public abstract List<Matter> queryFllowedMatterList(String userId);
	 
	 List<Matter> querySelfMatterList(String userId);
}