package com.comsince.phonebook.asynctask;


import com.comsince.phonebook.PhoneBookApplication;
import com.comsince.phonebook.R;
import com.comsince.phonebook.util.L;
import com.comsince.phonebook.util.NetUtil;
import com.comsince.phonebook.util.T;
import com.comsince.phonebook.util.baidupush.BaiduPush;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

public class SendMsgAsyncTask {
	private BaiduPush mBaiduPush;
	private String mMessage;
	private Handler mHandler;
	private MyAsyncTask mTask;
	private String mUserId;
	private OnSendScuessListener mListener;

	public interface OnSendScuessListener {
		void sendScuess();
	}

	public void setOnSendScuessListener(OnSendScuessListener listener) {
		this.mListener = listener;
	}

	Runnable reSend = new Runnable() {

		@Override
		public void run() {
			send();//重发
		}
	};

	public SendMsgAsyncTask(String jsonMsg,String useId) {
		//mBaiduPush = PushApplication.getInstance().getBaiduPush();
		mMessage = jsonMsg;
		mUserId = useId;
		mHandler = new Handler();
	}

	// 发送
	public void send() {
		if (NetUtil.isNetConnected(PhoneBookApplication.context)) {//如果网络可用
			mTask = new MyAsyncTask();
			mTask.execute();
		} else {
			T.showLong(PhoneBookApplication.context, R.string.net_error_tip);
		}
	}

	// 停止
	public void stop() {
		if (mTask != null)
			mTask.cancel(true);
	}

	class MyAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... message) {
			String result = "";
			if(TextUtils.isEmpty(mUserId))
				result = BaiduPush.PushMessage(mMessage);
			else
				result = BaiduPush.PushMessage(mMessage, mUserId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.i("send msg result:"+result);
			if (result.contains(BaiduPush.SEND_MSG_ERROR)) {// 如果消息发送失败，则100ms后重发
				mHandler.postDelayed(reSend, 100);
			} else {
				if (mListener != null)
					mListener.sendScuess();
			}
		}
	}
}
