package com.mimi.model.wechat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 * 
 * 微信消息格式说明
 * 文本消息
 *  <xml> 
      <URL> <![CDATA[http://secretsvn.duapp.com/api/wechat_connectWechat.php]]> </URL> 
      <ToUserName> <![CDATA[comsince]]> </ToUserName> 
      <FromUserName> <![CDATA[comisnce]]> </FromUserName>
      <CreateTime>1234234</CreateTime>
      <MsgType> <![CDATA[text]]> </MsgType> 
      <Content> <![CDATA[abc]]> </Content>
      <MsgId>234</MsgId> 
    </xml>
    
    notice： 通过simpleframework解析出来的带有空格的字符，注意在比较的时候留意
 * */
@Root(name="xml")
public class GeneralReceiveMessage extends BaseMessage {
	@Element(name="Content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return super.toString()+"--Content--"+content;
	}
}
