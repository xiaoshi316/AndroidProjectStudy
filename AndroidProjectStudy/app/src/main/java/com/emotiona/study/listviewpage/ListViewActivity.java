package com.emotiona.study.listviewpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.emotiona.study.R;
import com.emotiona.study.widget.DecoratorViewPager;
import com.emotiona.study.widget.PagerSlidingTabStrip;

public class ListViewActivity extends AppCompatActivity {

    private PagerSlidingTabStrip tabs;
    private DecoratorViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        initView();
        initData();
    }

    private void initView() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (DecoratorViewPager) findViewById(R.id.pager);
        pager.setNestedpParent((ViewGroup) pager.getParent());
        adapter=new MyPagerAdapter(getSupportFragmentManager());
    }
    private void initData(){
        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
    }




    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "页面一", "页面二", "页面三", "页面四", "页面五", "页面六",
                "页面七", "页面八" };

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
