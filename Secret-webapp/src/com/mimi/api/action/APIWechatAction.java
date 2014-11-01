package com.mimi.api.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mimi.common.Constants;
import com.mimi.core.service.MatterService;
import com.mimi.core.util.SimpleXmlReaderUtil;
import com.mimi.core.web.SuperAction;
import com.mimi.model.Matter;
import com.mimi.model.Page;
import com.mimi.model.wechat.GeneralReceiveMessage;
import com.mimi.model.wechat.TextResponseMessage;
import com.mimi.util.SignUtil;

public class APIWechatAction extends SuperAction {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(APIMatterAction.class);
	@Autowired
	private MatterService matterServiceImpl;

	public void setMatterServiceImpl(MatterService matterServiceImpl) {
		this.matterServiceImpl = matterServiceImpl;
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
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			System.out.println("Weixin post xml " + sb.toString());
			response.setContentType("text/xml;charset=UTF-8");
			processWechatReceiveMsg(sb.toString());
		}
		return null;
	}

	/**
	 * 解析微信请求信息
	 * */
	private String processWechatReceiveMsg(String message) {
		String result = null;
		GeneralReceiveMessage generalRecMsg = null;
		try {
			generalRecMsg = SimpleXmlReaderUtil.getInstance().readXmlFromString(message, GeneralReceiveMessage.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgType = generalRecMsg.getMsgType();
		if ("text".equals(msgType)) {
			result = receiveTextMsg(generalRecMsg);
		} else if ("image".equals(msgType)) {

		} else if ("voice".equals(msgType)) {

		} else if ("video".equals(msgType)) {

		} else if ("location".equals(msgType)) {

		} else if ("link".equals(msgType)) {

		}
		return result;
	}

	private String receiveTextMsg(GeneralReceiveMessage generalRecMsg) {
		StringBuffer sb = new StringBuffer();
		TextResponseMessage textResMsg = new TextResponseMessage();
		textResMsg.setToUserName(generalRecMsg.getToUserName());
		textResMsg.setFromUserName(generalRecMsg.getFromUserName());
		textResMsg.setMsgType(generalRecMsg.getMsgType());
		textResMsg.setCreateTime(new Date().getDate());
		textResMsg.setContent(getHotTextSecret());
		textResMsg.setMsgId(generalRecMsg.getMsgId());
		try {
			SimpleXmlReaderUtil.getInstance().writeXml(textResMsg, response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getHotTextSecret() {
		String content = null;
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
			Matter matter = (Matter) page.getDataList().get(0);
			content = matter.getContent();
		}
		return content;
	}

}
