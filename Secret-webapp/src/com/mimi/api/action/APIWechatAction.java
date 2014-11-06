package com.mimi.api.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mimi.common.Constants;
import com.mimi.core.service.MatterService;
import com.mimi.core.service.WeChatProcessService;
import com.mimi.core.util.SimpleXmlReaderUtil;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.model.wechat.GeneralReceiveMessage;
import com.mimi.model.wechat.TextResponseMessage;
import com.mimi.model.wechat.req.BaseMessage;
import com.mimi.model.wechat.req.EventMessage;
import com.mimi.model.wechat.req.ReqTextMessage;
import com.mimi.model.wechat.resp.TextMessage;
import com.mimi.util.MessageUtil;
import com.mimi.util.SignUtil;

public class APIWechatAction extends SuperAction {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(APIMatterAction.class);
	@Autowired
	private MatterService matterServiceImpl;

	@Autowired
	private WeChatProcessService weChatProcessServiceImpl;
	
	public void setMatterServiceImpl(MatterService matterServiceImpl) {
		this.matterServiceImpl = matterServiceImpl;
	}

	public void setWeChatProcessServiceImpl(WeChatProcessService weChatProcessServiceImpl) {
		this.weChatProcessServiceImpl = weChatProcessServiceImpl;
	}

	public String connectWechat() throws IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戮
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		String result = "";
		// 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
		if (echostr != null && echostr.length() > 1) {
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				result = echostr;
			}
			response.getWriter().print(result);
		} else {
			response.setContentType("text/xml;charset=UTF-8");
			response.getWriter().print(processRequest(request));
		}
		return null;
	}

	private String getHotTextSecret() {
		String content = "";
		Page page = new Page();
		page.setOrder("timestamp");
		PARAM_MAP.put("order", "timestamp");
		page.setCurrentPage(1);
		PARAM_MAP.put("currentPage", "1");
		try {
			PARAM_MAP.put("type", Constants.Common.TYPR_1);
			matterServiceImpl.queryMatterByPage(this.PARAM_MAP, page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (page.getDataList() != null && page.getDataList().size() > 0) {
			int current = new Random().nextInt(page.getDataList().size());
			Matter matter = (Matter) page.getDataList().get(current);
			content = matter.getContent();
		}
		if(content != null && content.length() >300){
			content = content.substring(0, 300);
		} 
		if(content == null || "".equals(content)){
			content = "网络超时，请重新输入";
		}
		return content;
	}
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			BaseMessage baseMsg = MessageUtil.parseXmltoObject(request);
			String msgType = baseMsg.getMsgType();
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 回复文本消息
				respMessage = weChatProcessServiceImpl.processTextMsg((ReqTextMessage) baseMsg);
				if(respMessage == null){
					TextMessage responseMsg = weChatProcessServiceImpl.responseTextMsg((ReqTextMessage) baseMsg);
					responseMsg.setContent(getHotTextSecret());
					respMessage = MessageUtil.textMessageToXml(responseMsg);
				}
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = ((EventMessage) baseMsg).getEvent();
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respMessage = weChatProcessServiceImpl.processFollowMsg((EventMessage) baseMsg);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("response msg: "+respMessage);
		return respMessage;
	}

}
