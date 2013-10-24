package com.comsince.phonebook.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.comsince.phonebook.view.viewpager.JazzyViewPager;

public class FacePageAdeapter extends PagerAdapter {

	private List<View> views;
	private JazzyViewPager viewPager;
	
	public FacePageAdeapter(List<View> lv, JazzyViewPager viewPager) {
		super();
		this.views = lv;
		this.viewPager = viewPager;
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
	   ((ViewPager) container).addView(views.get(position), 0);
		viewPager.setObjectForPosition(views.get(position), position);// 这句很重要,没有这句就没有效果
		return views.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}
	

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}
	
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

}
