package com.comsince.phonebook.view.pulltorefreshlistview;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public abstract class HandyListView extends ListView implements OnScrollListener {
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	/**
	 * listview中第一个可见的Item的索引
	 */
	protected int mFirstVisibleItem;
	
	/**
	 * listview最后一个可见item
	 * */
	protected int mLastVisibleItem;
	/**
	 * 当前Listview是不是处于顶部
	 */
	protected boolean mIsTop;
	/**
	 * 当前Listview是不是处于底部
	 * */
	protected boolean mIsBottom;
	
	/**
	 * 点击的点
	 * */
	protected Point mDownPoint;
	protected Point mMovePoint;
	/**
	 * 松开点击的点
	 * */
	protected Point mUpPoint;

	public HandyListView(Context context) {
		super(context);
		init(context);
	}

	public HandyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public HandyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		//根据滚动回调函数判断listview的所处位置
		//System.out.println("firstVisibleItem" + firstVisibleItem);
		mFirstVisibleItem = firstVisibleItem;
		mLastVisibleItem = view.getCount() - 1;
		if (view.getFirstVisiblePosition() == 1) {
			mIsTop = true;
		} else if (view.getLastVisiblePosition() == view.getCount() - 1) {
			//mLastVisibleItem = view.getLastVisiblePosition();
			mLastVisibleItem = view.getCount() - 1;
			mIsBottom = true;
		} else {
			mIsTop = false;
			mIsBottom = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int x = 0;
		int y = 0;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			x = (int) ev.getX();
			y = (int) ev.getY();
			mDownPoint = new Point(x, y);
			onDown(ev);
			break;

		case MotionEvent.ACTION_MOVE:
			x = (int) ev.getX();
			y = (int) ev.getY();
			mMovePoint = new Point(x, y);
			onMove(ev);
			break;

		case MotionEvent.ACTION_UP:
			x = (int) ev.getX();
			y = (int) ev.getY();
			mUpPoint = new Point(x, y);
			onUp(ev);
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 初始化变量及listview滚动监听器
	 * */
	private void init(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(context);
		setOnScrollListener(this);
	}
	
	public abstract void onDown(MotionEvent ev);

	public abstract void onMove(MotionEvent ev);

	public abstract void onUp(MotionEvent ev);

}
