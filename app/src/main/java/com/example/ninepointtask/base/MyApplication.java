package com.example.ninepointtask.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by xiaohan on 2018/4/25.
 */

public class MyApplication extends Application{

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        LitePal.initialize(sContext);//初始化
    }

    public static Context getContext() {
        return sContext;
    }
}
