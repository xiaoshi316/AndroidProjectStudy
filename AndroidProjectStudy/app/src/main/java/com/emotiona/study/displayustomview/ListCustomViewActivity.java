package com.emotiona.study.displayustomview;

import android.os.Bundle;

import com.emotiona.study.R;
import com.emotiona.study.base.view.BaseActivity;
import com.emotiona.study.widget.WaterWaveView;

public class ListCustomViewActivity extends BaseActivity {
    private WaterWaveView mWaterWaveView;
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_custom_view);
        setBackBar(true);
        setTitle("自定义控件");
        mWaterWaveView = (WaterWaveView) findViewById(R.id.wave_view);
        mWaterWaveView.setmWaterLevel(0.2F);
        mWaterWaveView.startWave();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        mWaterWaveView.stopWave();
        mWaterWaveView=null;
        super.onDestroy();
    }
}
