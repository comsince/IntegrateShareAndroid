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
			//responseMsg();
			response.getWriter().print(processRequest(request));
			//processWechatReceiveMsg(readStreamParameter(request.getInputStream()));
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
		System.out.println("weixin message:"+generalRecMsg.toString());
		String msgType = generalRecMsg.getMsgType();
		if (msgType.trim().equals("text")) {
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
		String [] formatString = new String[4];
		formatString[0] = "//<![CDATA[%1$s]]//>";
		formatString[1] = "<![CDATA[%1$s]]>";
		formatString[2] = "<![CDATA[%1$s]]>";
		formatString[3] = "<![CDATA[%1$s]]>";
		TextResponseMessage textResMsg = new TextResponseMessage();
		textResMsg.setToUserName(formatString[0].format(formatString[0], generalRecMsg.getToUserName()));
		textResMsg.setFromUserName(formatString[1].format(formatString[1], generalRecMsg.getFromUserName()));
		textResMsg.setMsgType(formatString[2].format(formatString[2], generalRecMsg.getMsgType()));
		textResMsg.setCreateTime(new Date().getTime()+"");
		textResMsg.setContent(formatString[3].format(formatString[3],getHotTextSecret()));
		textResMsg.setMsgId(generalRecMsg.getMsgId());
		System.out.println("weixin message:"+textResMsg.toString());
		try {
			SimpleXmlReaderUtil.getInstance().writeXml(textResMsg, response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
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
	 * 从输入流读出string，以供字符串反序列化
	 * */
	private String readStreamParameter(InputStream in){
		StringBuilder buffer = new StringBuilder();  
        BufferedReader reader=null;  
        try{  
            reader = new BufferedReader(new InputStreamReader(in));  
            String line=null;  
            while((line = reader.readLine())!=null){  
                buffer.append(line);  
            }  
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            if(null!=reader){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return buffer.toString();
	}
	
	
	public void responseMsg() throws IOException{  
        String postStr=null;  
        try{  
            postStr=this.readStreamParameter(request.getInputStream());  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        //System.out.println(postStr);  
        if (null!=postStr&&!postStr.isEmpty()){  
            Document document=null;  
            try{  
                document = DocumentHelper.parseText(postStr);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(null==document){  
                response.getWriter().print("");;  
                return;
            }
            Element root=document.getRootElement();
            String fromUsername = root.elementText("FromUserName");  
            String toUsername = root.elementText("ToUserName");  
            String keyword = root.elementTextTrim("Content");  
            String time = new Date().getTime()+"";
            String textTpl = "<xml>"+  
                        "<ToUserName><![CDATA[%1$s]]></ToUserName>"+  
                        "<FromUserName><![CDATA[%2$s]]></FromUserName>"+  
                        "<CreateTime>%3$s</CreateTime>"+  
                        "<MsgType><![CDATA[%4$s]]></MsgType>"+  
                        "<Content><![CDATA[%5$s]]></Content>"+  
                        //"<FuncFlag>0</FuncFlag>"+  
                        "</xml>";               
              
            if(null!=keyword&&!keyword.equals(""))  
            {
                String msgType = "text";
                String contentStr = getHotTextSecret();
                String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);
                response.getWriter().print(resultStr);
            }else{  
                response.getWriter().print("请输入任意字符串");  
            }  
        }else {  
            response.getWriter().print("");  
        }  
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
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType").trim();
			//消息内容
			String content = requestMap.get("Content");
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 回复文本消息
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setContent(content);
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				respMessage = weChatProcessServiceImpl.processTextMsg(textMessage);
				if(respMessage == null){
					textMessage.setContent(getHotTextSecret());
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				System.out.println("response msg: "+respMessage);
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢您的关注！";
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
		return respMessage;
	}

}
