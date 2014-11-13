package com.comsince.secret.phonelisten.location;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Created by liaojinlong on 14-11-13.
 */
public class LocationHelper {
    private static final String TAG = "LocationHelper";
    private BaseLocationHelper mLocationHelper;
    private Handler mUiHandler;
    private Context mContext;

    public LocationHelper(BaseLocationHelper mLocationHelper, Handler mUiHandler) {
        this.mLocationHelper = mLocationHelper;
        this.mUiHandler = mUiHandler;
    }

    public LocationHelper(Handler mUiHandler,Context context){
        this.mUiHandler = mUiHandler;
        this.mContext = context;
    }

    public void requestLocate(int mapType){
        Log.i(TAG, "request locate : " + mapType);
        boolean success = initHelper(mapType);
        if(success){
            mLocationHelper.locateAndSubmitSync();
        }
    }

    public void stopLocate(){
        mLocationHelper.stopLocate();
    }

    /**
     * 根据地图类型初始化选用的地图类型
     * */
    private boolean initHelper(int mapType){
        switch(mapType){
            case BaseLocationHelper.MAP_TYPE_BAIDU:
                if(mLocationHelper == null || mLocationHelper.getMapType() != mapType){
                    mLocationHelper = new BaiduLocationHelper(mContext, mUiHandler);
                }
                return true;
            case BaseLocationHelper.MAP_TYPE_GOOGLE:
                /*if(mLocationHelper == null || mLocationHelper.getMapType() != mapType){
                    mLocationHelper = new GoogleLocationHelper(mContext, mUiHandler);
                }*/
                return true;
            default:
                return false;
        }
    }

}
