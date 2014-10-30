package com.comsince.secret.component;



import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comsince.secret.R;

public class MMListView extends ListView implements OnScrollListener{
	
	private Context context;
	private View header ;
	private View footer ;
	private int firstItemIndex;
	private OnMoreListener moreListener;
	private OnScrollPositionListener positionListener;
	private boolean showFooter = true;
	private final static int LOADING_MORE = 5;
	private final static int LOADING_MORE_DONE = 6;
	private int footerState;
	public MMListView(Context paramContext) {
		super(paramContext);
		this.context = paramContext;
		setOnScrollListener(this);
		init(paramContext);
	}
	
	
	public MMListView(Context paramContext, AttributeSet paramAttributeSet)
	{
	    super(paramContext, paramAttributeSet);
	    setOnScrollListener(this);
	    this.context = paramContext;
	    init(paramContext);
	}
	

	public void init( final Context context)
	{
	 
		footer = LayoutInflater.from(context).inflate(R.layout.list_footer, null);
		this.addFooterView(footer,null,false);
		footer.setVisibility(View.GONE);
		 
		 
	}
    public void setHeaderView(View header){
    	this.header = header;
		addHeaderView(this.header, null, false);
		this.header.setPadding(0, 0, 0, 0);
    }
	
    public int getHeaderViewHeight()
    {
    	return header.getHeight();
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
		}else if (visibleItemCount > 1)
		{
			if(positionListener != null)
			{
				positionListener.showBackTopButton();
			}
		}
		 
	}
	

	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		 
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
		      //判断是否滚动到底部
			   int lastIndex =  view.getLastVisiblePosition();
			   int count = view.getCount();
		       if ((lastIndex == count - 1||lastIndex == count - 2 )&&moreListener!=null) {
		        footer.setVisibility(View.VISIBLE);
		    	if(((lastIndex-1) % 10==0||lastIndex%10==0)&&count>2)
				{
		    		footer.findViewById(R.id.footer_progressBar).setVisibility(View.VISIBLE);
		    		footer.findViewById(R.id.footer_hint).setVisibility(View.GONE);
		    		if(footerState!=LOADING_MORE){
		    			moreListener.onShowNextPage();
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
	
 

	
	 
	public interface OnMoreListener {
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

	public void setOnMoreListener(OnMoreListener listener) {
		this.moreListener = listener;
	}


	public void showMoreComplete( ) {
		footerState = LOADING_MORE_DONE;
		/*((ProgressBar) footer.findViewById(R.id.footProgressBar)).setVisibility(View.GONE);
		((TextView) footer.findViewById(R.id.footTextTip)).setText(context.getString(R.string.tip_look_more));*/
	}

	public void thatAll( ) {
		footer.setVisibility(View.VISIBLE);
		footer.findViewById(R.id.footer_progressBar).setVisibility(View.GONE);
		footer.findViewById(R.id.footer_hint).setVisibility(View.VISIBLE);
	}

	public void hideFooter() {
		footer.setVisibility(View.GONE);
	}


	public void setShowFooter(boolean showFooter) {
		this.showFooter = showFooter;
	}
	 
}
