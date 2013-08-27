package com.comsince.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.bouncycastle.util.encoders.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.comsince.knowledge.entity.BaiduDevMusic;
import com.comsince.knowledge.entity.BaiduDevMusicList;
import com.comsince.knowledge.entity.NetMusicList;
import com.comsince.knowledge.lrcutil.BaiduLrc;
import com.comsince.knowledge.utils.AndroidUtil;
import com.comsince.knowledge.utils.BaiduCloudSaveUtil;
import com.comsince.knowledge.utils.FileUtil;
import com.comsince.knowledge.utils.HttpDownloader;
import com.comsince.knowledge.utils.HttpTool;
import com.comsince.knowledge.utils.MCrypt;
import com.comsince.knowledge.utils.SimpleXmlReaderUtil;
import com.comsince.knowledge.utils.SoapUtil;
import com.comsince.test.xmlMode.Impda;

public class testUtiljunit extends AndroidTestCase {
  public String TAG = "TESTJUNIT";

   public void testutils(){
	   Log.d("test", "test Msg!");
   }
   public void testFileUtil() throws IOException{
	   InputStream is = this.mContext.getAssets().open("conf/session.xml");
	   String fileName = AndroidUtil.getSDCardRoot()+"wsd/session1.xml";
	   Log.d(TAG, fileName);
	   FileUtil.writeToFile(is, fileName);   
   }
   
   public void textCreateDir(){
	   //成功
	   FileUtil.creatSDDir("test/text");
   }
   
   public void testSimpleXmlReader() throws Exception{
	   SimpleXmlReaderUtil simpleXmlReader = new SimpleXmlReaderUtil();
	   String[] xmlPath = this.mContext.getAssets().list("conf/impda.xml");
	   Log.d(TAG, "code Path:"+xmlPath.length);
	   Impda impda = (Impda) simpleXmlReader.readXml(AndroidUtil.getSDCardRoot()+"wsd/conf/impda.xml", Impda.class);
	   Log.d(TAG, "impdaLanServer : "+impda.getValue("impdaLanServer"));
   }
   
   public void testSimpleXmlWrite(){
	   SimpleXmlReaderUtil simpleXmlReaderUtil = new SimpleXmlReaderUtil();
	   String outPutPath = AndroidUtil.getSDCardRoot()+"wsd/conf/impdatest.xml";
	   Impda impda = new Impda();
	   impda.setValue("impdaLanServer", "impdaLanServer:192.168.1.1");
	   impda.setValue("impdaWanServer", "impdaWanServer:192.168.2.1");
	   simpleXmlReaderUtil.writeXml(impda, outPutPath);
   }
   
   public void testReadSounds() throws Exception{
	   SimpleXmlReaderUtil simpleXmlReaderUtil = new SimpleXmlReaderUtil();
	   InputStream in = this.mContext.getAssets().open("conf/sounds.xml");
	   NetMusicList sounds = simpleXmlReaderUtil.readXmlFromInputStream(in, NetMusicList.class);
	   Log.d(TAG, "music : "+sounds.getNetMusics().get(0).getName());
   }
   
   public void testDevapi() throws Exception{
	   SimpleXmlReaderUtil simpleXmlReaderUtil = new SimpleXmlReaderUtil();
	   //InputStream in = this.mContext.getAssets().open("conf/baidudev.xml");
	   String decPath = "http://mp3.baidu.com/dev/api/?tn=getinfo&ct=0&ie=utf-8&word="+BaiduLrc.EncodeName("我相信")+"&format=xml";
	   InputStream in = HttpTool.getStream(decPath, null, null, HttpTool.GET);
	   BaiduDevMusicList list = simpleXmlReaderUtil.readXmlFromInputStream(in, BaiduDevMusicList.class);
	   Log.d(TAG, "music : "+list.getBaiduDevMusics().get(0).getSong_id());
   }
   
   public void testTingApi() throws Exception{
	   SimpleXmlReaderUtil simpleXmlReaderUtil = new SimpleXmlReaderUtil();
	   //InputStream in = this.mContext.getAssets().open("conf/baidudev.xml");
	 String decPath = "http://mp3.baidu.com/dev/api/?tn=getinfo&ct=0&ie=utf-8&word="+BaiduLrc.EncodeName("我相信")+"&format=xml";
	   InputStream in = HttpTool.getStream(decPath, null, null, HttpTool.GET);
	   BaiduDevMusicList list = simpleXmlReaderUtil.readXmlFromInputStream(in, BaiduDevMusicList.class);
	   //获取第一个music
	   BaiduDevMusic music = list.getBaiduDevMusics().get(0);
	   String songid = music.getSong_id();
	   //String songjsonurl = BaiduLrc.BAIDU_TING_API_PATH.replace("<songIds>", BaiduLrc.EncodeName(songid));
	  String songjsonur = "http://ting.baidu.com/data/music/links?songIds=7300733";
	  // String songjson = HttpTool.getresponse(songjsonurl, null, null, HttpTool.GET);
	   String songjson = HttpTool.getString(songjsonur, null, null, HttpTool.GET);
	   JSONObject jsonobj = new JSONObject(songjson.toString()).getJSONObject("data");
	   JSONArray jsonArray = jsonobj.getJSONArray("songList");
	   String lrcLink = ((JSONObject) jsonArray.get(0)).getString("lrcLink");
	   Log.d(TAG, jsonobj.getString("xcode"));
	   Log.d(TAG, String.valueOf(jsonArray.length()));
	   Log.d(TAG,lrcLink);
	   
	   
	   
	   
	   String strJson = "{\"students\":[{\"name\":\"Jack\",\"age\":12}, {\"name\":\"Vista\",\"age\":23}, {\"name\":\"Kaka\",\"age\":22}, {\"name\":\"Hony\",\"age\":31}]}";
       try {
           JSONObject jo = new JSONObject(strJson);
           @SuppressWarnings("unused")
		JSONArray jsonArray1 = (JSONArray) jo.get("students");
           for (int i = 0; i < jsonArray.length(); ++i) {
               JSONObject o = (JSONObject) jsonArray.get(i);
               System.out.println("name:" + o.getString("name") + "," + "age:"
                       + o.getInt("age"));
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }
   
   public void writeFile() throws IOException{
	   //InputStream in = HttpTool.getStream("http://ting.baidu.com/data2/lrc/13759942/13759942.lrc", null, null, HttpTool.GET);
	   HttpDownloader down = new HttpDownloader();
	   down.downFile("http://ting.baidu.com/data2/lrc/40874168/40874168.lrc","TMusic","125.lrc");
	   //FileUtil.writeToFile(in, Constant.LRC_PATH + "124.lrc");
   }
   
   public void testLongAdd(){
	   long i = 1000;
	   long j = 100;
	   long k = i+j;
	   Log.e("TESTJUNIT", String.valueOf(k));
   }
   
   public void downLoadFile() throws IOException{
	   HttpDownloader httpDownloader = new HttpDownloader();
	   String uri = "http://zhangmenshiting.baidu.com/data2/music/42632117/33847306122400.mp3?xcode=cee5cce0e59cd1854c919478cfdf962592370e67cf9eb94d";
	  
		InputStream in = HttpTool.getStream(uri, null, null, HttpTool.GET);
	
	   FileUtil.save(in, AndroidUtil.getSDCardRoot()+"TMusic/download/12.mp3");
		//httpDownloader.downFile(uri, "TMusic/download", "为了遇见你" + ".mp3");
   }
   
   public void testProgerss(){
	   int max = 100;
	   for(int i=0;i<100;i++){
		   Log.i("download", String.valueOf(1.0*i/max *100));
	   }
	   
   }
   
   
   public void testBCSDown() throws IOException{
	   String downurl = "http://bcs.duapp.com/comsince/log.txt?response-content-disposition=attachment;filename*=utf8''log.txt&response-cache-control=private";
	   String readurl = "http://bcs.duapp.com/comsince/log.txt";
	   String personReadUrl = "http://bcs.duapp.com/comsince/yc_zh_primary_person.xml";
	   //HttpDownloader httpDownloader = new HttpDownloader();
	   //String result = httpDownloader.download(readurl);
	   //httpDownloader.downFile(downurl, "bcs", "log.txt");
	  // Log.i("download", result);
	   String urlStr = BaiduCloudSaveUtil.generateUrlForGet("comsince", "/YC_ZG_PRIMARY/yc_zg_primary_person_test1.xml");
	   InputStream in = HttpTool.getStream(urlStr, null, null, HttpTool.GET);
	   FileUtil.save(in, AndroidUtil.getSDCardRoot()+"TMusic/download/yc_zh_primary_person_test1.xml");
   }
   
   public void testUpload() throws IOException{
	   String urlStr = "http://bcs.duapp.com/comsince/log.txt";
	   InputStream in = this.mContext.getAssets().open("conf/sounds.xml");
	   HttpDownloader httpDownLoader = new HttpDownloader();
	   httpDownLoader.upLoadFile(in, urlStr);
   }
   
   public void httpUpload() throws ClientProtocolException, IOException{
	   //BCS对象必须以斜线开头
	   String urlStr = BaiduCloudSaveUtil.generateUrl("comsince", "/YC_ZG_PRIMARY/yc_zg_primary_person_test1.xml");
		// 创建client
	   //String urlStr = "http://bcs.duapp.com/comsince/yc_zg_primary_person_test.xml?sign=MBO:9ab8af408f719b4961bbd16f9bac9f16:RGpAqZlIwk6IZy4xFiVXG0o9EIM%3D";
	   //String urlStr = "http://bcs.duapp.com/phonebook";
	   String path = AndroidUtil.getSDCardRoot()+"phoneBook"+File.separator+"yc_zg_primary_person_test.xml";
	   BaiduCloudSaveUtil.putObject(urlStr, path);
   }
   
   public void testSoapRequest(){
	   String soapUrl = "http://10.66.76.86/pdaserverExt/IMPDAMainService.asmx";
	   String soapAction = "http://impda.newtrek.net/IMPDAWebServer/LoginInfo";
	   String nameSpace = "http://impda.newtrek.net/IMPDAWebServer/";
	   MCrypt mcrypt = new MCrypt();
	   String enryptedUsername = null;
	   String enryptedPassword = null;
	try {
		enryptedUsername = new String(Base64.encode( mcrypt.encrypt("MWMS1") ));
		enryptedPassword = new String(Base64.encode( mcrypt.encrypt("abcd1234") ));
	} catch (Exception e) {
		e.printStackTrace();
	}
	//login info 
	   SoapObject loginRequest = new SoapObject(nameSpace, "LoginInfo");
	   SoapObject para = new SoapObject("", "para");
	   para.addProperty("PdaId", "353771057964560");
       para.addProperty("WsPdaId", "353771057964560");
       para.addProperty("WsUserId", enryptedUsername);
       para.addProperty("WsUserPwd", enryptedPassword);	
       loginRequest.addSoapObject(para);
	   SoapObject result = SoapUtil.sendSoapRequest(soapUrl, loginRequest, soapAction);
	   String isSuccess = result.getPropertyAsString("IsSuccess");
	   String sessionId = result.getPropertyAsString("SessionID");
	   Log.d("soap", isSuccess);
	   //get ftpInformation
	   soapAction = "http://impda.newtrek.net/IMPDAWebServer/GetFtpInformation";
	   para = null;
	   para = new SoapObject("", "para");
	   para.addProperty("SessionID",sessionId);
	   para.addProperty("PdaId", "353771057964560");
	   para.addProperty("WsPdaId", "353771057964560");
       para.addProperty("WsUserId", enryptedUsername);
       para.addProperty("WsUserPwd", enryptedPassword);	
	   SoapObject getFtpInformation = new SoapObject(nameSpace, "GetFtpInformation");
	   getFtpInformation.addSoapObject(para);
	   SoapObject resultFtpInformation = SoapUtil.sendSoapRequest(soapUrl, getFtpInformation, soapAction);
	   Log.d("soap", "getFtpInformation");
   }
   
   public void testHTTPWsd() throws IOException{
	   String uri = "http://10.66.76.86/pdaserverExt/IMPDAMainService.asmx/MASRegionServer?";
	   InputStream in = HttpTool.getStream(uri, null, null, HttpTool.GET);
	   FileUtil.save(in, AndroidUtil.getSDCardRoot()+"TMusic/download/wsd.xml");
   }
   
   

}
