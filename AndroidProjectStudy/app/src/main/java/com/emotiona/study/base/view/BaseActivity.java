package com.emotiona.study.base.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emotiona.study.R;

public abstract class BaseActivity extends AppCompatActivity implements BaseView{
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private FrameLayout mContentLayout;
    public String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setContentView(R.layout.activity_base);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContentLayout = (FrameLayout) findViewById(R.id.content_base);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("");
        setBackBar(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(toolbar);
        initView(savedInstanceState);
        initData();
        initListener();
    }
    public abstract void initData();
    public abstract void initListener();
    public abstract void initView(Bundle savedInstanceState);

    public FrameLayout getContentLayout() {
        return mContentLayout;
    }

    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }
    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        getLayoutInflater().inflate(layoutResID, mContentLayout, true);
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.addView(view, params);
    }

    public void setContentBackground(int color) {
        mContentLayout.setBackgroundResource(color);
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return onOptionsItemSelectedCompat(item);
    }

    protected boolean onOptionsItemSelectedCompat(MenuItem item) {
        return false;
    }

    public ViewGroup getContentRoot() {
        return mContentLayout;
    }

    @Override
    public void setShowToolBar(boolean showToolBar){
        if (!showToolBar)
        getSupportActionBar().hide();
    }
    @Override
    public void setBackBar(boolean isBackBar){
        getSupportActionBar().setDisplayHomeAsUpEnabled(isBackBar);
    }
    @Override
    public void setTitle(CharSequence title) {
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        } else {
            mTitle.setText("");
        }
    }

    @Override
    public void setTitle(int titleId) {
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.tv_title);
        if (titleId > 0) {
            mTitle.setText(titleId);
        } else {
            mTitle.setText("");
        }
    }
    public void setTitleTextColor(int color) {
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.tv_title);
        mTitle.setTextColor(color);
    }

    @Override
    public <T extends View> T findView(int viewId) {
        return (T)mContentLayout.findViewById(viewId);
    }
    @Override
    public void showMessageDialog(int title, int message) {
        showMessageDialog(getText(title), getText(message));
    }
    @Override
    public void showMessageDialog(int title, CharSequence message) {
        showMessageDialog(getText(title), message);
    }
    @Override
    public void showMessageDialog(CharSequence title, int message) {
        showMessageDialog(title, getText(message));
    }

    @Override
    public void showMessageDialog(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void showMessageDialog(CharSequence title, CharSequence message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_ok, okListener);
        builder.show();
    }
    @Override
    public void showToastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
