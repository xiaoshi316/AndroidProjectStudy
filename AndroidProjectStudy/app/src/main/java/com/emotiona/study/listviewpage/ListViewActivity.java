package com.emotiona.study.listviewpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.emotiona.study.R;
import com.emotiona.study.base.view.BaseActivity;
import com.emotiona.study.widget.PagerSlidingTabStrip;


public class ListViewActivity extends BaseActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_view);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        setBackBar(true);
        setTitle("滑动列表页");
    }

    @Override
    public void initData() {
        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
    }

    @Override
    public void initListener() {

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"页面一", "页面二", "页面三", "页面四", "页面五", "页面六",
                "页面七", "页面八"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SwipeMenuListViewFragment.newInstance(position);
        }

    }
}
