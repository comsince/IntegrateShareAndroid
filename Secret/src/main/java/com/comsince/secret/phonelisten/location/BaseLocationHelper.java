package com.comsince.secret.phonelisten.location;

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
}
