package com.comsince.test;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.BaiduDevMusic;
import com.comsince.knowledge.entity.BaiduDevMusicList;
import com.comsince.knowledge.entity.NetMusicList;
import com.comsince.knowledge.lrcutil.BaiduLrc;
import com.comsince.knowledge.utils.AndroidUtil;
import com.comsince.knowledge.utils.FileUtil;
import com.comsince.knowledge.utils.HttpDownloader;
import com.comsince.knowledge.utils.HttpTool;
import com.comsince.knowledge.utils.SimpleXmlReaderUtil;
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

}
