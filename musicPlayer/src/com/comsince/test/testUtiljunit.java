package com.comsince.test;

import java.io.IOException;
import java.io.InputStream;

import android.test.AndroidTestCase;
import android.util.Log;

import com.comsince.knowledge.entity.BaiduDevMusicList;
import com.comsince.knowledge.entity.NetMusicList;
import com.comsince.knowledge.lrcutil.BaiduLrc;
import com.comsince.knowledge.utils.AndroidUtil;
import com.comsince.knowledge.utils.FileUtil;
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
	   String decPath = "http://mp3.baidu.com/dev/api/?tn=getinfo&ct=0&ie=utf-8&word="+BaiduLrc.EncodeName("大海")+"&format=xml";
	   InputStream in = HttpTool.getStream(decPath, null, null, HttpTool.GET);
	   BaiduDevMusicList list = simpleXmlReaderUtil.readXmlFromInputStream(in, BaiduDevMusicList.class);
	   Log.d(TAG, "music : "+list.getBaiduDevMusics().get(0).getAlbum());
   }

}
