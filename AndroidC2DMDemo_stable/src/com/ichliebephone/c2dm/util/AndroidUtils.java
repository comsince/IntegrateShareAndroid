
package com.ichliebephone.c2dm.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.ichliebephone.c2dm.netutils.MpushRequestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

public class AndroidUtils {

    public static final String TYPE_NAME_SINA = "com.meizu.sns.sina";
    public static final String DEBUG_TAG = "C2DM";
    public static final boolean DEBUG_DEVELOPER_MODE = false;

    public static void bindMPush(final Context context) {
        final String weiboId = AccountHelper.getLoginAccountUID(context, TYPE_NAME_SINA);
        final String accessToken = AccountHelper.getLoginAccountAccessToken(context,
                TYPE_NAME_SINA);
        Log.i(DEBUG_TAG, "weiboId: "+weiboId +"accessToken: "+accessToken);

        if (TextUtils.isEmpty(weiboId) || TextUtils.isEmpty(accessToken)) {
            return;
        }

     
        new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
                    Map<String, Object> result = MpushRequestUtil.weiboPushRequest(context,
                            (byte) MpushRequestUtil.OPRATION_TYPE_SUMBIT,
                            (weiboId == null ? "" : weiboId),
                            (accessToken == null ? "" : accessToken));
                    
                    Log.i(DEBUG_TAG, "bindpush result :"+result);

                    int errorCode = (Integer) result.get("error_code");
                    if (errorCode == MpushRequestUtil.ERROR_CODE_WEIBO_PUSH_SUCCESS) {
                        return;
                    }
                }

            }

        }.start();
    }
    
    /**
     * get Imei
     * @param context 
     * @return imei
     * */
    public static String getIMEI(Context context) {
        try {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = mTelephonyMgr.getDeviceId();
            return imei;
        } catch (Exception e) {
        }
        return null;
    }
    
   
    public static int debug(String msg) {
        return debug(DEBUG_TAG, msg);
    }

    public static int debug(String tag, String msg) {
        if (DEBUG_DEVELOPER_MODE) {
            return Log.d(tag, msg);
        } else {
            return 0;
        }
    }
    
    private static final boolean WRITE_LOG_SDCARD_SWITCH = true;
    public static void WriteLogToSDCard(Context context, String tag, String logFileName, String msg) {
        if (DEBUG_DEVELOPER_MODE) {
            Log.i(tag, msg);
        }
        if (WRITE_LOG_SDCARD_SWITCH
                && android.os.Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED)) {
            try {

                File dirPath = context.getExternalCacheDir();
                if (dirPath == null) {
                    return;
                }
                
                if (!dirPath.exists() || !dirPath.isDirectory()) {
                    dirPath.mkdirs();
                }

                File savefile = new File(dirPath, logFileName);
                if (!savefile.exists()) {
                    savefile.createNewFile();
                } else {
                    long length = savefile.length();
                    if (length > 2 * 1024 * 1024) {
                        savefile.delete();
                        savefile.createNewFile();
                    }
                }
                Calendar CD = Calendar.getInstance();
                int YY = CD.get(Calendar.YEAR);
                int MM = CD.get(Calendar.MONTH) + 1;
                int DD = CD.get(Calendar.DATE);
                int HH = CD.get(Calendar.HOUR_OF_DAY);
                int NN = CD.get(Calendar.MINUTE);
                int SS = CD.get(Calendar.SECOND);
                String logmsg = "";
                logmsg += "[";
                String szTime = "";
                szTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", YY, MM,
                        DD, HH, NN, SS);
                logmsg += szTime;
                logmsg += "]";
                logmsg += msg;
                logmsg += "\r\n";
                FileOutputStream out = new FileOutputStream(savefile, true);
                out.write(logmsg.getBytes());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
