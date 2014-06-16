package com.ichliebephone.c2dm.reflect.proxy;


import android.app.ActionBar;

import static org.joor.Reflect.on;

public class OthersProxy {
    public static final String M040 = on(android.os.Build.class).get("M040");
    public static final String SNS_ENABLE = on(android.provider.Settings.System.class).get("SNS_ENALBE").toString();
    public static final String CACHED_CONTACTS_ID = on(android.provider.CallLog.Calls.class).get("CACHED_CONTACTS_ID");
    
    public static String getProductSeqNo() {
        String sn = "";
        try {
            sn = on("com.android.internal.telephony.ITelephony$Stub").call("asInterface", 
                    on("android.os.ServiceManager").call("checkService", "phone").get())
                    .call("queryProductSeqNo").get();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sn;
    }
    
    public static android.app.Notification.Builder setNotificationIcon(
            android.app.Notification.Builder builder, int resId) {
        return on(builder).call("setNotificationIcon", resId).get();
    }
    
    public static void setActionModeHeaderHidden(ActionBar actionBar, boolean hidden) {
        on(actionBar).call("setActionModeHeaderHidden", hidden);
    }
    
    public static void setTabsShowAtBottom(ActionBar actionBar, boolean atBottom) {
        on(actionBar).call("setTabsShowAtBottom", atBottom);
    }
    
    public static void setActionBarViewCollapsable(ActionBar actionBar, boolean collapsable) {
        on(actionBar).call("setActionBarViewCollapsable", collapsable);
    }
    
    public static void useMzTitleLayout(ActionBar actionBar, boolean mzTitle) {
        on(actionBar).call("useMzTitleLayout", mzTitle);
    }
    
    public static void setGroupMode(android.preference.PreferenceScreen ps, boolean mzGroupMode) {
        // Reflect -> android:mzGroupMode="boolean"
        on(ps).call("setGroupMode", mzGroupMode);
    }
    
    
    public static String getAccessPackageName(android.content.Intent intent) {
        return on(intent).call("getAccessPackageName").get();
    }
    
    public static Boolean isResumed(android.app.Activity activity) {
        return on(activity).call("isResumed").get();
    }
    
    public static String[] readStringArray(android.os.Parcel p) {
        return on(p).call("readStringArray").get();
    }
}
