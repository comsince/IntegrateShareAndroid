package com.mimi.core.service;

import com.mimi.model.wechat.req.EventMessage;
import com.mimi.model.wechat.req.ReqTextMessage;
import com.mimi.model.wechat.resp.TextMessage;


public abstract interface WeChatProcessService {
	
	public abstract TextMessage responseTextMsg(ReqTextMessage textMsg);

	public abstract String processTextMsg(ReqTextMessage textMsg);
	
	public abstract String processFollowMsg(EventMessage eventMsg);
}
