
package com.ichliebephone.c2dm.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class AccountHelper {

    /**
     * 账号类型名称
     */
    public static final String TYPE_NAME = "type_name";
    public final static String AUTHORITY = "com.meizu.sns";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/account");
    /**
     * access_token
     */
    public static final String ACCESS_TOKEN = "access_token";
    /**
     * 用户id
     */
    public static final String UID = "uid";

    /**
     * 获取当前登录账户的uid
     * 
     * @param context
     * @param accountName
     * @param accountType
     * @return
     */
    public static String getLoginAccountUID(Context context, String accountType) {
        String uid = null;
        String selection = TYPE_NAME + "=?";
        String[] selectionArgs = new String[] {
                accountType
        };
        Cursor c = context.getContentResolver().query(CONTENT_URI, null, selection, selectionArgs,
                null);
        if (c != null && c.moveToFirst()) {
            uid = c.getString(c.getColumnIndex(UID));
        }
        if (c != null) {
            c.close();
        }
        return uid;
    }

    /**
     * 获取当前登录账户的AccessToken
     * 
     * @param context
     * @param accountName
     * @param accountType
     * @return
     */
    public static String getLoginAccountAccessToken(Context context, String accountType) {
        String token = null;
        String selection = TYPE_NAME + "=?";
        String[] selectionArgs = new String[] {
                accountType
        };
        Cursor c = context.getContentResolver().query(CONTENT_URI, null, selection, selectionArgs,
                null);

        if (c != null && c.moveToFirst()) {
            token = c.getString(c.getColumnIndex(ACCESS_TOKEN));
        }
        if (c != null) {
            c.close();
        }
        token = EncryptUtil.decryptByAES(token);
        return token;
    }
}
