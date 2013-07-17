package com.comsince.knowledge.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyPagerAdapter extends PagerAdapter {
	List<View> pageListView;
	

	public MyPagerAdapter(List<View> pageListView) {
		this.pageListView = pageListView;
	}

	@Override
	public int getCount() {
		return pageListView.size();
	}
    /**
     * 不要忘记了
     * **/
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}
	@Override
	public Object instantiateItem(View collection, int position) {
		try {
			((ViewPager) collection).addView(pageListView.get(position), 0);
		} catch (Exception e) {
		}
		return pageListView.get(position);
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		if (position > 0) {
			((ViewPager) collection).removeView(pageListView.get(position));
		}
	}

}
