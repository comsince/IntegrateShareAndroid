package com.ichliebephone.c2dm.netutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

public class NetworkUtil {
    public enum NetworkOperator {
        CHINA_MOBILE,
        CHINA_UNICOM,
        CHINA_TELECOM,
        UNKNOWN,
    }
    
    
    /**
     * find the network operator from SIM card
     * @return  UNKNOWN if not defined in {@link NetworkOperator} or SIM not inserted
     * @see {@link NetworkOperator}
     */
    public static NetworkOperator getNetworkOperator(Context context) {
        TelephonyManager tm = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        NetworkOperator op = NetworkOperator.UNKNOWN;

        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            String simOperator = tm.getSimOperator(); 
            
            if(simOperator != null){   
                if(simOperator.equals("46000") || 
                   simOperator.equals("46002") || 
                   simOperator.equals("46007")){   // 46007 移动188卡
                    // 中国移动
                    op = NetworkOperator.CHINA_MOBILE;
                }else if(simOperator.equals("46001")){   
                    // 中国联通    
                    op = NetworkOperator.CHINA_UNICOM;
                }else if(simOperator.equals("46003")){   
                    // 中国电信    
                    op = NetworkOperator.CHINA_TELECOM;
                } else {
                    // 暂时不考虑中国以外的运营商
                    op = NetworkOperator.UNKNOWN;
                }
            } else {
                op = NetworkOperator.UNKNOWN;
            }
        }
        return op;
    }
    
    public static boolean isChinaUnicom(Context context) {
        return getNetworkOperator(context) == NetworkOperator.CHINA_UNICOM;
    }

    /**
     * 获取网络连接状态
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // mobile 3G Data Network
        State mobileState = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        State wifiState = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
    
        if (mobileState == State.CONNECTED || wifiState == State.CONNECTED
                || mobileState == State.CONNECTING
                || wifiState == State.CONNECTING) {
            return true;
        } else {
            return false;
        }
    
    }
}
