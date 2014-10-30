package com.mimi.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mimi.model.BlackList;
import com.mimi.model.Matter;
import com.mimi.model.Page;

public class BlackListDaoImpl extends HibernateDaoSupport  {

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
		BlackList black = null;
		 Criteria criteria = getSession().createCriteria(BlackList.class);
		 criteria.add(Restrictions.eq("sourceIp",ip));
		 List<BlackList> list = criteria.list();
		 if(list.size()>0)
		 {
			return (BlackList)list.get(0);
		 }
		return black;
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
