package com.emotiona.study.base.view;

import android.content.DialogInterface;
import android.view.View;


/***
 * @author sxshi
 * @describe view 的基本操作。
 */
public interface BaseView{
    void setShowToolBar(boolean showToolBar);
    void setBackBar(boolean isBackBar);
    <T extends View> T findView(int viewId);
    void showToastMsg(String msg);
    void showMessageDialog(int title, int message);
    void showMessageDialog(int title, CharSequence message);
    void showMessageDialog(CharSequence title, int message);
    void showMessageDialog(CharSequence title, CharSequence message);
    void showMessageDialog(CharSequence title, CharSequence message, DialogInterface.OnClickListener okListener);
}