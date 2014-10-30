package com.comsince.secret.component;



import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comsince.secret.R;
import com.comsince.secret.util.AppTools;
import com.comsince.secret.util.BaseContorl;


public class SuperListView extends ListView implements OnScrollListener{
	private final static int RATIO = 3;
	
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	
	private final static int LOADING_MORE = 5;
	private final static int LOADING_MORE_DONE = 6;
	
	private Context context;
	private View header ;
	private View footer ;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private int firstItemIndex;
	private int startY;
	private int headerHeight;
	private int footerState;
	private int state;
	private OnRefreshListener refreshListener;
	private OnScrollPositionListener positionListener;
	private boolean isBack;
	private boolean isRecored;
	private boolean isRefreshable = false;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ImageView progressBar;
	private long lastRefreshTime = System.currentTimeMillis();
	public SuperListView(Context paramContext) {
		super(paramContext);
		this.context = paramContext;
		setOnScrollListener(this);
		init(paramContext);
	}
	
	
	public SuperListView(Context paramContext, AttributeSet paramAttributeSet)
	{
	    super(paramContext, paramAttributeSet);
	    setOnScrollListener(this);
	    this.context = paramContext;
	    init(paramContext);
	}
	

	public void init( final Context context)
	{
		header = LayoutInflater.from(context).inflate(R.layout.list_header, null);
		footer = LayoutInflater.from(context).inflate(R.layout.list_footer, null);
		/*footer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(footer.findViewById(R.id.footProgressBar).getVisibility()==View.VISIBLE)
				{
					return ;
				}
				((ProgressBar) footer.findViewById(R.id.footProgressBar)).setVisibility(View.VISIBLE);
				((TextView) footer.findViewById(R.id.footTextTip)).setText(context.getString(R.string.loading));
				 refreshListener.onShowNextPage();
			}});*/
		arrowImageView = (ImageView) header.findViewById(R.id.head_arrowImageView);
		progressBar = (ImageView) header.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) header.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) header.findViewById(R.id.head_lastUpdatedTextView);
		AppTools.measureView(header);
		headerHeight = header.getMeasuredHeight();
		//footerHeight = footer.getMeasuredHeight();
		addHeaderView(header, null, false);
		
		addFooterView(footer, null, false);
		footer.setVisibility(View.GONE);
		header.setPadding(0, -1 * headerHeight, 0, 0);
		//footer.setPadding(0, 0, 0, -1 * footerHeight);
		animation = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}

	
	
	@Override
	public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int count) {
		firstItemIndex = firstVisiableItem;
		if(firstItemIndex ==  0)
		{
			if(positionListener != null)
			{
				positionListener.hideBackTopButton();
			}
		}else  
		{
			if(positionListener != null)
			{
				positionListener.showBackTopButton();
			}
		}
		 
	}
	
	public void doRefresh()
	{
		state = REFRESHING;
		changeHeaderViewByState();
		onRefresh();
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstItemIndex == 0  && !isRecored) {
				isRecored = true;
				startY = (int) event.getY();
				//Log.v(TAG, "在down时候记录当前位置‘");
			}
			positionListener.backTopButtonFocuse();
			break;

		case MotionEvent.ACTION_UP:

			if (state != REFRESHING && state != LOADING) {
				if (state == DONE) {
					
				}
				if (state == PULL_To_REFRESH) {
					state = DONE;
					changeHeaderViewByState();

					//Log.v(TAG, "由下拉刷新状态，到done状态");
				}
				if (state == RELEASE_To_REFRESH) {
					state = REFRESHING;
					changeHeaderViewByState();
					onRefresh();

				//	Log.v(TAG, "由松开刷新状态，到done状态");
				}
			}

			isRecored = false;
			isBack = false;
			positionListener.backTopButtonBlur();
			break;

		case MotionEvent.ACTION_MOVE:
			int tempY = (int) event.getY();

			if (firstItemIndex == 0  && !isRecored) {
				//Log.v(TAG, "在move时候记录下位置");
				isRecored = true;
				startY = tempY;
			}

			if (state != REFRESHING && isRecored && state != LOADING) {

				// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

				// 可以松手去刷新了
				if (state == RELEASE_To_REFRESH) 
				{

					setSelection(0);

					// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
					if (((tempY - startY) / RATIO < headerHeight)
							&& (tempY - startY) > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();

						//Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
					}
					// 一下子推到顶了
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();

						//Log.v(TAG, "由松开刷新状态转变到done状态");
					}
					// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					else {
						// 不用进行特别的操作，只用更新paddingTop的值就行了
					}
				}
				// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
				if (state == PULL_To_REFRESH) {

					setSelection(0);

					// 下拉到可以进入RELEASE_TO_REFRESH的状态
					if ((tempY - startY) / RATIO >= headerHeight) {
						state = RELEASE_To_REFRESH;
						isBack = true;
						changeHeaderViewByState();

						//Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
					}
					// 上推到顶了
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();

						///Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
					}
				}

				// done状态下
				if (state == DONE) {
					if (tempY - startY > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
					}
				}

				// 更新headView的size
				if (state == PULL_To_REFRESH) {
					header.setPadding(0, -1 * headerHeight
							+ (tempY - startY) / RATIO, 0, 0);

				}

				// 更新headView的paddingTop
				if (state == RELEASE_To_REFRESH) {
					header.setPadding(0, (tempY - startY) / RATIO
							- headerHeight, 0, 0);
				}

			}

			break;
		}
	 return super.onTouchEvent(event);
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
		      //判断是否滚动到底部
			   int lastIndex =  view.getLastVisiblePosition();
			   int count = view.getCount();
            if (lastIndex == count - 1||lastIndex == count - 2) {
		        footer.setVisibility(View.VISIBLE);
		    	if(((lastIndex-1) % 10==0||lastIndex%10==0)&&count>2)
				{
		    		footer.findViewById(R.id.footer_progressBar).setVisibility(View.VISIBLE);
		    		footer.findViewById(R.id.footer_hint).setVisibility(View.GONE);
		    		if(footerState!=LOADING_MORE){
		    			refreshListener.onShowNextPage();
			    		footerState = LOADING_MORE;
		    		}
				}
		    	else
		    	{
		    		footer.findViewById(R.id.footer_progressBar).setVisibility(View.GONE);
		    		footer.findViewById(R.id.footer_hint).setVisibility(View.VISIBLE);
		    	}
		       }
		}
	}
	
	
	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		lastUpdatedTextView.setText(context.getString(R.string.refresh_lasttime)+AppTools.howTimeAgo(context, lastRefreshTime));
		if (isRefreshable) {
			switch (state) {
			case RELEASE_To_REFRESH:
				
				arrowImageView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(animation);
				tipsTextview.setText(context.getString(R.string.refresh_release));
	
				//Log.v(TAG, "当前状态，松开刷新");
				break;
			case PULL_To_REFRESH:
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.VISIBLE);
				// 是由RELEASE_To_REFRESH状态转变来的
				if (isBack) {
					isBack = false;
					arrowImageView.clearAnimation();
					arrowImageView.startAnimation(reverseAnimation);
				}  
				//Log.v(TAG, "当前状态，下拉刷新");
				tipsTextview.setText(context.getString(R.string.refresh_pull));
				break;
	
			case REFRESHING:
	
				header.setPadding(0, 0, 0, 0);
	
				progressBar.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.GONE);
				//tipsTextview.setText(context.getString(R.string.loading));
				tipsTextview.setVisibility(View.GONE);
				lastUpdatedTextView.setVisibility(View.GONE);
				//Log.v(TAG, "当前状态,正在刷新...");
				break;
			case DONE:
				header.setPadding(0, -1 * headerHeight, 0, 0);
	
				progressBar.setVisibility(View.GONE);
				arrowImageView.clearAnimation();
				arrowImageView.setImageResource(R.drawable.arrow);
				tipsTextview.setText(context.getString(R.string.refresh_pull));
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				//Log.v(TAG, "当前状态，done");
				break;
			}
		}
	}

	
	

	
	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
		public void onShowNextPage();
	}
	
	public interface OnScrollPositionListener{
		public void showBackTopButton();
		public void hideBackTopButton();
		public void backTopButtonFocuse();
		public void backTopButtonBlur();
	}
	public void setOnScrollPositionListener(OnScrollPositionListener listener) {
		this.positionListener = listener;
	}

	public void refreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText(context.getString(R.string.refresh_lasttime)+context.getString(R.string.at_now));
		lastRefreshTime = System.currentTimeMillis();
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}


	public void showMoreComplete(boolean  flag) {
		
		footerState = LOADING_MORE_DONE;
		if(!flag)
		{
		    //BaseContorl.doShowToask(context, R.string.tip_network_busy);
            footer.findViewById(R.id.footer_progressBar).setVisibility(View.GONE);
            footer.findViewById(R.id.footer_hint).setVisibility(View.VISIBLE);
		}
	/*	((ProgressBar) footer.findViewById(R.id.footProgressBar)).setVisibility(View.GONE);
		((TextView) footer.findViewById(R.id.footTextTip)).setText(context.getString(R.string.tip_look_more));*/
	}
	 
}
