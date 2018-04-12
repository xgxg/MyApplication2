package com.dr.xg.myapplication;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;
import com.orhanobut.hawk.Hawk;

/**
 * @author 黄冬榕
 * @date 2018/1/8
 * @description
 * @remark
 */

public class SysApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        MobSDK.init(this);
    }

    public static Context getContext(){
        return getContext();
    }
}
