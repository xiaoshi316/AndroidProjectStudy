package com.emotiona.study.utils;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

import java.lang.reflect.Method;

/**
 * @author sxshi on 2016/11/2.
 * @email:emotiona_xiaoshi@126.com
 * @describe:获取屏幕高度，不包括虚拟按键
 */

public class DevicesScreenUtils {
    private Activity mActivity;

    public DevicesScreenUtils(Activity activity) {
        this.mActivity=activity;
    }
    /**
     * 获取屏幕尺寸，但是不包括虚拟功能高度
     *
     * @return
     */
    public int getNoHasVirtualKey() {
        Point outSize=new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(outSize);
        return outSize.y;
    }
    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     *
     * @return
     */
    private int getHasVirtualKey() {
        int dpi = 0;
        Display display =mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /***
     * 获取虚拟按键的高度
     * @return
     */
    private int getVirtualKeysHeight(){
        return getHasVirtualKey()-getNoHasVirtualKey();
    }
}
