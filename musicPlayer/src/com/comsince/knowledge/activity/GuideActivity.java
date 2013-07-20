package com.comsince.knowledge.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.comsince.knowledge.R;

public class GuideActivity extends Activity {
	/**
	 * ViewPager展示引导页内容
	 */
	private ViewPager mPager;
	/**
	 * 引导页的跳转按钮
	 */
	private Button mButton;
	/**
	 * 引导页显示内容的View
	 */
	private View mPage1, mPage2, mPage3;
	/**
	 * 存放显示内容的View
	 */
	private List<View> mViews = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		/**
		 * 获取要显示的引导页内容
		 */
		mPage1 = LayoutInflater.from(this).inflate(R.layout.guide_activity_page1, null);
		mPage2 = LayoutInflater.from(this).inflate(R.layout.guide_activity_page2, null);
		mPage3 = LayoutInflater.from(this).inflate(R.layout.guide_activity_page3, null);
		/**
		 * 添加View
		 */
		mViews.add(mPage1);
		mViews.add(mPage2);
		mViews.add(mPage3);
		
		setView();
		setUpListener();
		mPager.setAdapter(new ViewPagerAdapter());
	}

	private void setView() {
		mButton = (Button) mPage3.findViewById(R.id.guide_activity_btn);
		mPager = (ViewPager) findViewById(R.id.guide_activity_viewpager);
	}

	private void setUpListener() {
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

		});
	}

	
	/**
	 * 
	 * ViewPagerAdapter
	 * 覆写以下的方法
	 * */
	
	private class ViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mViews.get(position));
		}
		
		
		
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mViews.get(position));
			return mViews.get(position);
		}
		
	}
	
}
