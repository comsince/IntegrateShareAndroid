package com.comsince.phonebook.view.pulltorefreshlistview;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.comsince.phonebook.R;


public class RefreshListView extends HandyListView {
	
	private OnRefreshListener mOnRefreshListener;
	private OnCancelListener mOnCancelListener;
	/**
	 * 刷新布局组件
	 * */
	private View mHeader;
	private HandyTextView mHtvTitle;
	private HandyTextView mHtvTime;
	private ImageView mIvArrow;
	private ImageView mIvLoading;
	private ImageView mIvCancel;
	
	/**
	 * 底部布局
	 * **/
	private View mBottom;
	private int mBottomHeight;
	private LinearLayout mBottomLayout;
	
	/**当前移动的点的Y坐标*/
	private int mStartY;
	/**
	 * 刷新头的高度
	 * */
	private int mHeaderHeight;
	
	/**
	 * 判定moveEvent移动高度是否大于RefreshBar高度，从而决定是否开始翻转动画
	 * */
	private boolean mIsBack;
	
	/**
	 * 是否记录点击下的点的Y坐标
	 * **/
	private boolean mIsRecored;
	
	/**
	 * 是否可结束刷新
	 * */
	private boolean mIsCancelable;
	
	/**
	 * 当前是否可下拉刷新
	 * */
	private boolean mIsRefreshable;
	
	/**当前refreshBar所处状态
	 * 状态变化过程：Done->PULL_TO_REFRESH->RELEASE_TO_REFRESH
	 * **/
	private int mState;
	/**释放刷新**/
	private final static int RELEASE_TO_REFRESH = 0;
	/**下拉刷新**/
	private final static int PULL_TO_REFRESH = 1;
	/**正在刷新**/
	private final static int REFRESHING = 2;
	/**刷新完毕**/
	private final static int DONE = 3;
	private final static int LOADING = 4;
	/**底部上拉*/
	private final static int BOTTOM_PULL_TO_REFRESH = 5;
	private final static int BOTTOM_PULL_TO_RELEASE = 6;
	private final static int BOTTOM_DONE = 7;
	/**下拉刷新栏高度比例**/
	private final static int RATIO = 3;
	
	/**
	 * 与翻转动画相反
	 * */
	private android.view.animation.RotateAnimation mPullAnimation;
	/**
	 * 用户下拉刷新到一定高度超过refreshBar的最高高度，就开始翻转动画
	 * */
	private android.view.animation.RotateAnimation mReverseAnimation;
	/**
	 * 加载数据动画
	 * */
	private Animation mLoadingAnimation;
	
	
	public RefreshListView(Context context) {
		super(context);
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mHeader = mInflater.inflate(R.layout.include_pull_to_refreshing_header, null);
		mHtvTitle = (HandyTextView) mHeader.findViewById(R.id.refreshing_header_htv_title);
		mHtvTime = (HandyTextView) mHeader.findViewById(R.id.refreshing_header_htv_time);
		mIvArrow = (ImageView) mHeader.findViewById(R.id.refreshing_header_iv_arrow);
		mIvLoading = (ImageView) mHeader.findViewById(R.id.refreshing_header_iv_loading);
		mIvCancel = (ImageView) mHeader.findViewById(R.id.refreshing_header_iv_cancel);
		
		mBottom = mInflater.inflate(R.layout.include_pull_to_refreshing_bottom,null);
		mBottomLayout = (LinearLayout) mBottom.findViewById(R.id.refreshing_bottom_layout_container);

		//初始化cancel事件处理
		mIvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mOnCancelListener != null && mIsCancelable) {
					mOnCancelListener.onCancel();
				}
			}
		});
		
		measureView(mHeader);
		//将刷新头加入到listview的Header上
		addHeaderView(mHeader);
		mHeaderHeight = mHeader.getMeasuredHeight();
		mHeader.setPadding(0, -1 * mHeaderHeight, 0, 0);
		mHeader.invalidate();
		
		measureView(mBottom);
		//加入底部
		addFooterView(mBottom);
		mBottomHeight = mBottom.getMeasuredHeight();
		mBottom.setPadding(0, -1 * mBottomHeight, 0, 0);
		mBottom.invalidate();
		
		//初始化开始时基本控件值
		mHtvTitle.setText("下拉刷新");
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		mHtvTime.setText("最后刷新: " + date);
		
		mPullAnimation = new android.view.animation.RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mPullAnimation.setInterpolator(new LinearInterpolator());
		mPullAnimation.setDuration(250);
		mPullAnimation.setFillAfter(true);

		mReverseAnimation = new android.view.animation.RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseAnimation.setInterpolator(new LinearInterpolator());
		mReverseAnimation.setDuration(200);
		mReverseAnimation.setFillAfter(true);

		mLoadingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading);
		
		/**初始化时设置可刷新*/
		mIsRefreshable = false;
	    /**默认状态隐藏**/
		mState = DONE;
	}

	/**
	 * 设置刷新头显示参数
	 * */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onDown(MotionEvent ev) {
		//System.out.println("onDown X: "+ev.getX());
		//System.out.println("onDown Y: "+ev.getY());
		if (mIsRefreshable) {
			if (mFirstVisibleItem == 0 && !mIsRecored) {
				mStartY = mDownPoint.y;
				mIsRecored = true;
			}
		}
	}

	@Override
	public void onMove(MotionEvent ev) {
		//System.out.println("onMove X: "+ev.getX());
		//System.out.println("onMove Y: "+ev.getY());
		//System.out.println("mIsRecored: "+mIsRecored);
		if (mIsRefreshable) {
			if (!mIsRecored &&mFirstVisibleItem == 0) {
				mStartY = mMovePoint.y;
				mIsRecored = true;
			}
			//System.out.println("mStartY"+ mStartY);
			//System.out.println("mMovePoint.y"+ mMovePoint.y);
			//System.out.println("Current mState: "+mState);
			//判断当前refreshBar状态
			if(mState != REFRESHING && mIsRecored && mState != LOADING && mState != BOTTOM_PULL_TO_REFRESH && mState != BOTTOM_DONE){
				
				if (mState == DONE) {
					if (mMovePoint.y - mStartY > 0) {
						mState = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
				}
				if (mState == PULL_TO_REFRESH) {
					setSelection(0);
					//下拉高度是是刷新栏高度的Ratio倍时开始松开刷新
					if ((mMovePoint.y - mStartY) / RATIO >= mHeaderHeight) {
						mState = RELEASE_TO_REFRESH;
						mIsBack = true;
						changeHeaderViewByState();
						//如果在下拉的时候又上推则关闭刷新栏
					} else if (mMovePoint.y - mStartY <= 0) {
						mState = DONE;
						changeHeaderViewByState();
					}
				}
				if (mState == RELEASE_TO_REFRESH) {
					setSelection(0);
					//松开刷新又回退时，回到pull_to_refresh状态
					if (((mMovePoint.y - mStartY) / RATIO < mHeaderHeight) && (mMovePoint.y - mStartY) > 0) {
						mState = PULL_TO_REFRESH;
						changeHeaderViewByState();
						//会退到关闭刷新栏
					} else if (mMovePoint.y - mStartY <= 0) {
						mState = DONE;
						changeHeaderViewByState();
					}
				}
				//显示刷新Bar,并设置padding高度
				if (mState == PULL_TO_REFRESH) {
					mHeader.setPadding(0, -1 * mHeaderHeight+ (mMovePoint.y - mStartY) / RATIO, 0, 0);
				}
				//松开刷新释放之前不断地加大高度padding
				if (mState == RELEASE_TO_REFRESH) {
					mHeader.setPadding(0, (mMovePoint.y - mStartY) / RATIO- mHeaderHeight, 0, 0);
				}
			}
			//刷新时候不断地加大高度padding
			if (mState == REFRESHING) {
				mHeader.setPadding(0, (mMovePoint.y - mStartY) / RATIO, 0, 0);
			}
			
			//判断当前显示的是不是最后一个item,从而进行向下padding
			//System.out.println("test mIsBottom: "+mIsBottom);
			//System.out.println("test mIsRecored: "+mIsRecored);
			if(mIsBottom && !mIsRecored){
				mStartY = mMovePoint.y;
				mIsRecored = true;
				mState = BOTTOM_PULL_TO_REFRESH;
				//System.out.println("test:mstate "+mState);
			}
			
			if(mState == BOTTOM_PULL_TO_REFRESH){
				setSelection(mLastVisibleItem);
				if ((mStartY - mMovePoint.y) / RATIO >= mBottomHeight) {
					changeHeaderViewByState();
				} else if (mStartY - mMovePoint.y <= 0) {
					mState = BOTTOM_DONE;
					changeHeaderViewByState();
				}
			}
			
			if(mState == BOTTOM_PULL_TO_REFRESH){
				mBottom.setPadding(0, (mStartY - mMovePoint.y) / RATIO- mBottomHeight, 0, 0);
			}
		}
	}

	@Override
	public void onUp(MotionEvent ev) {
		//System.out.println("onUp X: "+ev.getX());
		//System.out.println("onUp Y: "+ev.getY());
		if (mState != REFRESHING && mState != LOADING) {
			if (mState == PULL_TO_REFRESH) {
				mState = DONE;
				changeHeaderViewByState();
			}
			if (mState == RELEASE_TO_REFRESH) {
				mState = REFRESHING;
				changeHeaderViewByState();
				onRefresh();

			}
		}
		if(mState == REFRESHING){
			mHeader.setPadding(0, 0, 0, 0);
		}
		if(mState == BOTTOM_PULL_TO_REFRESH){
			mState = BOTTOM_DONE;
			changeHeaderViewByState();
			mState = DONE;
		}
		if(mState == BOTTOM_DONE){
			mState = DONE;
		}
		mIsRecored = false;
		mIsBack = false;
	}
	
	/**
	 * 根据refreshBar状态改变控件的显示状态
	 * */
	private void changeHeaderViewByState() {
		switch (mState) {
		case RELEASE_TO_REFRESH:
			mIvArrow.setVisibility(View.VISIBLE);
			mIvLoading.setVisibility(View.GONE);
			mHtvTitle.setVisibility(View.VISIBLE);
			mHtvTime.setVisibility(View.VISIBLE);
			mIvCancel.setVisibility(View.GONE);
			mIvArrow.clearAnimation();
			mIvArrow.startAnimation(mPullAnimation);
			mIvLoading.clearAnimation();
			mHtvTitle.setText("松开刷新");
			break;
		case PULL_TO_REFRESH:
			mIvArrow.setVisibility(View.VISIBLE);
			mIvLoading.setVisibility(View.GONE);
			mHtvTitle.setVisibility(View.VISIBLE);
			mHtvTime.setVisibility(View.VISIBLE);
			mIvCancel.setVisibility(View.GONE);
			mIvLoading.clearAnimation();
			mIvArrow.clearAnimation();
			if (mIsBack) {
				mIsBack = false;
				mIvArrow.clearAnimation();
				mIvArrow.startAnimation(mReverseAnimation);
				mHtvTitle.setText("下拉刷新");
			} else {
				mHtvTitle.setText("下拉刷新");
			}
			break;

		case REFRESHING:
			mHeader.setPadding(0, 0, 0, 0);
			mIvLoading.setVisibility(View.VISIBLE);
			mIvArrow.setVisibility(View.GONE);
			mIvLoading.clearAnimation();
			mIvLoading.startAnimation(mLoadingAnimation);
			mIvArrow.clearAnimation();
			mHtvTitle.setText("正在刷新...");
			mHtvTime.setVisibility(View.VISIBLE);
			if (mIsCancelable) {
				mIvCancel.setVisibility(View.VISIBLE);
			} else {
				mIvCancel.setVisibility(View.GONE);
			}

			break;
		case DONE:
			mHeader.setPadding(0, -1 * mHeaderHeight, 0, 0);

			mIvLoading.setVisibility(View.GONE);
			mIvArrow.clearAnimation();
			mIvLoading.clearAnimation();
			mIvArrow.setImageResource(R.drawable.ic_common_droparrow);
			mHtvTitle.setText("下拉刷新");
			mHtvTime.setVisibility(View.VISIBLE);
			mIvCancel.setVisibility(View.GONE);
			break;
		case BOTTOM_PULL_TO_REFRESH:
			mBottomLayout.setVisibility(View.VISIBLE);
			break;
		case BOTTOM_DONE:
			mBottom.setPadding(0, -1 * mBottomHeight, 0, 0);
			mBottomLayout.setVisibility(View.GONE);
			//mState = DONE;
			break;
		}
	}
	
	/**
	 * 刷新结束操作
	 * */
	public void onRefreshComplete() {
		mState = DONE;
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		mHtvTime.setText("最后刷新: " + date);
		changeHeaderViewByState();
	}
	
	/**
	 * 回调接口自调用操作
	 * */
	private void onRefresh() {
		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}
	
	/**
	 * 刷新回调接口
	 * */
	public interface OnRefreshListener {
		public void onRefresh();
	}
	
	public void setOnRefreshListener(OnRefreshListener l) {
		mOnRefreshListener = l;
		mIsRefreshable = true;
	}
	
	public interface OnCancelListener {
		public void onCancel();
	}
	
	public void setOnCancelListener(OnCancelListener l) {
		mOnCancelListener = l;
		mIsCancelable = true;
	}

}
