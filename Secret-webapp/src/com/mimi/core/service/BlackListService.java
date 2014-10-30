package com.mimi.core.service;

import com.mimi.model.BlackList;
import com.mimi.model.Page;

public abstract interface BlackListService
{
  public abstract BlackList addOneBlackList(BlackList black);

  public abstract Page queryBlackListByPage(byte type, Page page);

  public abstract void deleteBlackListById(Integer id);

  public abstract BlackList queryByIP(String ip);
  public abstract BlackList queryByAccount(String account);
  public abstract void updateBlackList(BlackList black);

  public abstract int queryBlackListAmount(byte paramByte);
}