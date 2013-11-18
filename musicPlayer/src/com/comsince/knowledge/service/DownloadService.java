package com.comsince.knowledge.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;

import com.comsince.knowledge.R;
import com.comsince.knowledge.activity.MainActivity;
import com.comsince.knowledge.constant.Constant;
import com.comsince.knowledge.entity.TingMusicJson;
import com.comsince.knowledge.utils.AndroidUtil;
import com.comsince.knowledge.utils.FileUtil;
import com.comsince.knowledge.utils.HttpDownloader;
import com.comsince.knowledge.utils.HttpTool;


public class DownloadService extends Service {
	/**
	 * 当前下载文件的总长度*
	 * */
	private long fileLength;
	/**
	 * 当前下载的音乐文件名
	 * */
	private String currentMusicName;
	/**
	 * 当前歌手名
	 * */
	private String artistName;
	/**
	 * 任务队列
	 * */
	private ArrayList<TingMusicJson> taskQueue;
	/**
	 * 任务轮询线程
	 * */
	private Thread thread;
	/**
	 * 线程通信对象,更新nitification manager
	 * */
	private Handler handler;
	/**
	 * 下载工具
	 * */
	private HttpDownloader httpDownloader;

	@Override
	public IBinder onBind(Intent arg0) {
		// 返回Binder通信对象，不能忘了
		return new DownLoadBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		httpDownloader = new HttpDownloader();
		// 初始化任务队列
		taskQueue = new ArrayList<TingMusicJson>();
		// 启动轮询线程
		thread = new TaskQueueThread();
		thread.start();
		handler = new UpdateHandler();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// 当service解绑时，设置inUnbind为true
		isUnbind = true;
		return super.onUnbind(intent);
	}

	// binder对象，用于在Activity中向Service通信
	public class DownLoadBinder extends Binder {
		// 添加新任务(此方法在activity中调用，在此service中执行)
		public void addTask(TingMusicJson task) {
			if (!taskQueue.contains(task)) {
				taskQueue.add(task);
				synchronized (thread) {
					thread.notify();
				}
			}
		}
	}

	// Service是否已经unbind的标识值
	private boolean isUnbind = false;

	class TaskQueueThread extends Thread {
		@Override
		public void run() {
			while (true) {
				// 当任务队列中有任务时，循环下载任务
				while (taskQueue.size() > 0) {
					// 下载任务
					try {
						// 取出任务队列中的第一条任务
						TingMusicJson taskMusic = taskQueue.remove(0);
						// //获取当前下载的任务的文件长度
						String uri = taskMusic.getShowLink();
						fileLength = HttpTool.getLength(uri, null, null, HttpTool.GET);
						// 获取当前下载的任务的文件名
						currentMusicName = taskMusic.getSongName();
						artistName = taskMusic.getArtistName();
						// 开始下载时发送消息回主线程
						handler.sendEmptyMessage(Constant.MSG_START);
						// 下载文件
						InputStream in = HttpTool.getStream(uri, null, null, HttpTool.GET);
						// httpDownloader.downFile(uri, "TMusic/download",
						// currentMusicName + "-"
						// +taskMusic.getArtistName()+".mp3");
						String path = AndroidUtil.getSDCardRoot() + "TMusic/download/" + currentMusicName + "-" + taskMusic.getArtistName() + ".mp3";
						FileUtil.save(in, path, handler, fileLength);
						// 下载完成时发送消息回主线程
						Message msg = handler.obtainMessage(Constant.MSG_OK, taskMusic);
						handler.sendMessage(msg);
					} catch (IOException e) {
						e.printStackTrace();
						// 下载失败时发送消息回主线程
						handler.sendEmptyMessage(Constant.MSG_ERROR);
					}

				}
				// 如果任务列表中所有任务都已下载完成，且service已经解绑，
				// 则停止service，退出线程
				if (isUnbind) {
					stopSelf();
					break;
				}

				// 如果任务列表为空，且未解绑，则线程等待
				try {
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class UpdateHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			setNotifiacationManager();
			switch (msg.what) {
			case Constant.MSG_START:
				noti.contentView.setViewVisibility(R.id.processbar_layout, View.VISIBLE);
				noti.contentView.setViewVisibility(R.id.text_layout, View.GONE);
				noti.contentView.setTextViewText(R.id.file_name, currentMusicName + "-" + artistName);
				noti.contentView.setTextViewText(R.id.percent, "0%");
				noti.contentView.setProgressBar(R.id.download_progress, 100, 0, false);
				// 发送通知到通知栏
				manager.notify(0, noti);
				break;
			case Constant.MSG_PROGRESS:
				noti.contentView.setTextViewText(R.id.file_name, currentMusicName + "-" + artistName);
				noti.contentView.setTextViewText(R.id.percent, String.valueOf(msg.arg1)+"%");
				noti.contentView.setProgressBar(R.id.download_progress, 100, msg.arg1, false);
				manager.notify(0, noti);
				break;
			case Constant.MSG_OK:
				noti.contentView.setViewVisibility(R.id.processbar_layout, View.GONE);
				noti.contentView.setViewVisibility(R.id.text_layout, View.VISIBLE);
				noti.contentView.setTextViewText(R.id.status_1, "下载完成，请点击查看");
				manager.notify(0, noti);
				Intent intent = new Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
				Bundle bundle = new Bundle();
				intent.putExtras(bundle);
				sendBroadcast(intent);
				manager.cancel(0);
				break;
			case Constant.MSG_ERROR:
				manager.cancel(0);
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 设置通知管理器
	 * **/
	NotificationManager manager;
	Notification noti;

	public void setNotifiacationManager() {
		// 获得通知管理器对象
		manager = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
		// 创建通知对象
		noti = new Notification(android.R.drawable.stat_sys_download, "通知", System.currentTimeMillis());
		// 设置有通知时闪灯
		noti.defaults = Notification.DEFAULT_LIGHTS;
		// 设置通知不能清除
		noti.flags = Notification.FLAG_NO_CLEAR;
		// 设置通知显示自定义View视图
		noti.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.notification_item_layout);
		// 设置通知的PendingIntent
		noti.contentIntent = PendingIntent.getActivity(DownloadService.this, 0, new Intent(DownloadService.this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
