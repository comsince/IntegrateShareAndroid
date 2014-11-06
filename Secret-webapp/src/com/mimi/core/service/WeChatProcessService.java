package com.mimi.core.service;

import com.mimi.model.wechat.resp.TextMessage;


public abstract interface WeChatProcessService {

	public abstract String processTextMsg(TextMessage textMsg);
}
