package com.emotiona.study.displayustomview;

import android.os.Bundle;

import com.emotiona.study.R;
import com.emotiona.study.base.view.BaseActivity;

public class ListCustomViewActivity extends BaseActivity {
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_custom_view);
        setBackBar(true);
        setTitle("自定义控件");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
