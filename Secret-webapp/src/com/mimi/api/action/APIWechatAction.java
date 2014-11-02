package com.mimi.api.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
			/*StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			System.out.println("Weixin post xml " + sb.toString());*/
			/*String responseStr = "<xml>"+  
                    "<ToUserName>ljlong</ToUserName>"+  
                    "<FromUserName>comsince</FromUserName>"+  
                    "<CreateTime>12345678</CreateTime>"+  
                    "<MsgType>text</MsgType>"+  
                    "<Content>测试消息</Content>"+  
                    "<MsgId>123456789</MsgId>"+  
                    "</xml>";
			response.setContentType("text/xml;charset=UTF-8");
			response.getWriter().print(responseStr);*/
			responseMsg();
			//processWechatReceiveMsg(sb.toString());
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
                        "<FuncFlag>0</FuncFlag>"+  
                        "</xml>";               
              
            if(null!=keyword&&!keyword.equals(""))  
            {  
                String msgType = "text";  
                String contentStr = "Welcome to wechat world!";  
                String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);  
                response.getWriter().print(resultStr);  
            }else{  
                response.getWriter().print("Input something...");  
            }  
  
        }else {  
            response.getWriter().print("");  
        }  
    }  
	

}
