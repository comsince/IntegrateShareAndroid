package com.comsince.knowledge.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


public class SoapUtil {
	private static int TimeOut = 120000;

	/**
	 * 发送SOAP请求，函数需要进一步提取参数，以便能进行函数通用适配 调用.net请求发送
	 * 
	 * @param soapURL
	 *            soapURL请求地址
	 * @param outPutSoapObject
	 *            发送出去的请求对象
	 * @param soapAction
	 *            soap post 请求的地址
	 * */
	public static SoapObject sendSoapRequest(String soapURL, SoapObject outPutSoapObject, String soapAction) {
		SoapObject resultSoapObject = null;
		// 装载evelope,设置好SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// 增加.net支持
		envelope.dotNet = true;
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		envelope.bodyOut = outPutSoapObject;

		HttpTransportSE androidHttpTransport = new HttpTransportSE(soapURL, 120000);
		androidHttpTransport.debug = true;

		try {
			androidHttpTransport.call(soapAction, envelope);
			// 获取返回结果
			resultSoapObject = (SoapObject) envelope.getResponse();
			//resultSoapObject = (SoapObject) envelope.bodyIn;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultSoapObject;
	}

}
