package com.example.ninepointtask.util;

import com.example.ninepointtask.base.MyApplication;

/**
 * Created by xiaohan on 2018/4/25.
 */

public class DensityUtil {

    /**
     * 工具类 构造器私有
     */
    private DensityUtil() {
    }

    public static int dipToPx(float dpValue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pxToDp(float pxValue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}

