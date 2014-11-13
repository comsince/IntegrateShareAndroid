package com.comsince.secret.phonelisten.location;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.comsince.secret.util.NetWorkUtils;

/**
 * Created by liaojinlong on 14-11-13.
 */
public class BaiduLocationHelper extends BaseLocationHelper {
    private static final String TAG = "BaiduLocationHelper";
    public LocationClient mLocationClient;

    private static final int locationSpan = 30*60*1000;

    protected BaiduLocationHelper(Context context, Handler uiHandler) {
        super(context, uiHandler, MAP_TYPE_BAIDU);
        mContext = context;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.d(TAG, "error code = " + bdLocation.getLocType());
            if(bdLocation.getLocType() == 161 && NetWorkUtils.isNetworkAvailable(mContext)){
                submitLocation(new BaseLocation(
                        bdLocation.getLatitude(), bdLocation.getLongitude(), bdLocation.getAddrStr()));
            }

        }
    }


    @Override
    protected void initServerIfNeed() {
        if(mLocationClient == null){
            mLocationClient = new LocationClient(mContext.getApplicationContext());
            mLocationClient.registerLocationListener(new MyLocationListener());
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);
            option.setCoorType("bd09ll");
            option.setScanSpan(locationSpan);
            option.setIsNeedAddress(true);
            mLocationClient.setLocOption(option);
        }
    }

    @Override
    protected void startLocationServer() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }

    @Override
    protected void stopLocationServer() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }
}
