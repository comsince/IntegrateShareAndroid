package com.mimi.core.service.impl;

import com.mimi.core.dao.impl.BlackListDaoImpl;
import com.mimi.core.service.BlackListService;
import com.mimi.model.BlackList;
import com.mimi.model.Page;

public class BlackListServiceImpl implements BlackListService {

  private 	BlackListDaoImpl blackListDao;
      

	public BlackList addOneBlackList(BlackList black) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page queryBlackListByPage(byte type, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteBlackListById(Integer id) {
		// TODO Auto-generated method stub

	}

	public BlackList queryByIP(String ip) {
		// TODO Auto-generated method stub
		return blackListDao.queryByIP(ip);
	}

	public BlackList queryByAccount(String account) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateBlackList(BlackList black) {
		// TODO Auto-generated method stub

	}

	public int queryBlackListAmount(byte paramByte) {
		// TODO Auto-generated method stub
		return 0;
	}

}
