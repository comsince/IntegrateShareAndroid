
package com.ichliebephone.c2dm.netutils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ichliebephone.c2dm.reflect.proxy.OthersProxy;
import com.ichliebephone.c2dm.util.AndroidUtils;
import com.ichliebephone.c2dm.util.EncryptUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MpushRequestUtil {
    private static final String TAG = "MPushRequestUtil";
    public static final String KEY_RESULT_ERROR_CODE = "error_code";
    public static final String KEY_RESULT_ERROR_MSG = "error_msg";
    private static final String WEIBO_PUSH_PARAM_KEY = "OjUiuYe80AUYnbgBNT6";
    private static final String WEIBO_PUSH_URL = "https://p.meizu.com/api/weibopush";
    private static final String WEIBO_PUSH_URL_UNICOM = "https://122.13.148.103/api/weibopush";
    private static final String WEIBO_PUSH_URL_TELECOM = "https://121.14.39.13/api/weibopush";
    
    public static final int OPRATION_TYPE_SUMBIT = 1;
    public static final int ERROR_CODE_WEIBO_PUSH_SUCCESS = 200;

    public static Map<String, Object> weiboPushRequest(Context context, byte optype,
            String weiboId, String token) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        int errorCode = -1;
        String errorMsg = "";

        if (!checkParam(weiboId, "0")
                || !checkParam(token)) {
            errorMsg = "weiboPushRequest check params failed: weiboId-" + weiboId + " token-"
                    + token;
            Log.e(TAG, errorMsg);
            resultMap.put(KEY_RESULT_ERROR_CODE, errorCode);
            resultMap.put(KEY_RESULT_ERROR_MSG, errorMsg);
            return resultMap;
        }

        HttpClient httpClient = getNewHttpClient();
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        postParams.add(new BasicNameValuePair("optype", optype + ""));
        postParams.add(new BasicNameValuePair("weiboid", weiboId));
        postParams.add(new BasicNameValuePair("token", token));

        long timestamp = System.currentTimeMillis() / 1000;
        postParams.add(new BasicNameValuePair("timestamp", timestamp + ""));

        int nonce = (new Random()).nextInt();
        postParams.add(new BasicNameValuePair("nonce", nonce + ""));

        String imei = AndroidUtils.getIMEI(context);
        postParams.add(new BasicNameValuePair("imei", imei));

        String sn = OthersProxy.getProductSeqNo();
        String key = WEIBO_PUSH_PARAM_KEY;
        String signStr = "key=" + key + "&optype=" + optype + "&imei=" + imei + "&weiboid="
                + weiboId + "&token="
                + token + "&nonce=" + nonce + "&timestamp=" + timestamp + "&sn=" + sn;
        String sign = EncryptUtil.digestByMD5(signStr);
        postParams.add(new BasicNameValuePair("sign", sign));

        HttpResponse httpResponse = null;

        try {
            HttpPost httpPost = new HttpPost(WEIBO_PUSH_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
            httpResponse = httpClient.execute(httpPost);
            String result = read(httpResponse);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            try {
                JSONObject jObject = new JSONObject(result);
                errorCode = jObject.getInt("code");
                errorMsg = jObject.getString("msg");
                AndroidUtils.WriteLogToSDCard(context, TAG, "bind_push.txt", "发送请求并获得服务器响应：optype="
                        + optype + ", imei=" + imei + ", status code=" + statusCode
                        + ", errorCode=" + errorCode + ", errorMsg="
                        + (errorMsg == null ? "" : errorMsg));
            } catch (Exception e) {
                e.printStackTrace();
                AndroidUtils.WriteLogToSDCard(context, TAG, "bind_push.txt", "发送请求收到无法解析返回，post： "
                        + signStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
            AndroidUtils.WriteLogToSDCard(context, TAG, "bind_push.txt", "发送请求但返回异常：optype="
                    + optype + ", imei=" + imei + ", exception=" + e.getMessage() + "，使用IP重新链接");
            // 被域名劫持，或者网络链接异常，重新用IP试一次
            try {
                HttpPost httpPost = new HttpPost();
                if (NetworkUtil.isChinaUnicom(context)) {
                    httpPost.setURI(URI.create(WEIBO_PUSH_URL_UNICOM));
                } else {
                    httpPost.setURI(URI.create(WEIBO_PUSH_URL_TELECOM));
                }

                httpPost.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
                httpResponse = httpClient.execute(httpPost);
                String result = read(httpResponse);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                try {
                    JSONObject jObject = new JSONObject(result);
                    errorCode = jObject.getInt("code");
                    errorMsg = jObject.getString("msg");
                    AndroidUtils.WriteLogToSDCard(context, TAG, "bind_push.txt",
                            "发送请求并获得服务器响应：optype="
                                    + optype + ", imei=" + imei + ", status code=" + statusCode
                                    + ", errorCode=" + errorCode + ", errorMsg="
                                    + (errorMsg == null ? "" : errorMsg));
                } catch (Exception e1) {
                    e.printStackTrace();
                    AndroidUtils.WriteLogToSDCard(context, TAG, "bind_push.txt",
                            "发送请求收到无法解析返回，post： " + signStr);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                // Ignore exceptions above, since we are already trying again
            }
        }
        resultMap.put(KEY_RESULT_ERROR_CODE, errorCode);
        resultMap.put(KEY_RESULT_ERROR_MSG, errorMsg);
        return resultMap;
    }

    public static boolean checkParam(String param, String... forbiddenValues) {
        boolean checked = false;
        checked = !TextUtils.isEmpty(param);
        for (String val : forbiddenValues) {
            if (!checked) {
                break;
            } else {
                checked = checked && !param.equals(val);
            }
        }
        return checked;
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] {
                    tm
            }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    /**
     * 读取post请求结果
     * 
     * @param response
     * @return return result from content
     */
    private static String read(HttpResponse response)
            throws IllegalStateException, IOException {
        String result = "";
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
        inputStream = entity.getContent();
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        Header header = response.getFirstHeader("Content-Encoding");
        if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
            inputStream = new GZIPInputStream(inputStream);
        }

        // Read response into a buffered stream
        int readBytes = 0;
        byte[] sBuffer = new byte[512];
        while ((readBytes = inputStream.read(sBuffer)) != -1) {
            content.write(sBuffer, 0, readBytes);
        }
        // Return result from buffered stream
        result = new String(content.toByteArray());
        return result;
    }

}
