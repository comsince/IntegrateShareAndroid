package comsince.test;

import java.io.IOException;
import java.io.InputStream;

import android.test.AndroidTestCase;
import android.util.Log;

import com.comsince.knowledge.utils.AndroidUtil;
import com.comsince.knowledge.utils.FileUtil;
import com.comsince.knowledge.utils.SimpleXmlReaderUtil;
import comsince.test.xmlMode.Impda;

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

}
