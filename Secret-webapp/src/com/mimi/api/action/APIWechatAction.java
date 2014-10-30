package com.mimi.api.action;

import java.io.IOException;

import com.mimi.core.web.SuperAction;
import com.mimi.util.SignUtil;

public class APIWechatAction extends SuperAction {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    public String connectWechat() throws IOException
    {
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
            if(SignUtil.checkSignature(signature, timestamp, nonce)){
                result = echostr;
            }
        } else {  
            //正常的微信处理流程  
            //result = new WechatProcess().processWechatMag(xml);  
        } 
        response.getWriter().print(result);
        return null;
    }


}
