package com.yoo.ymh.whoru.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.util.helper.log.Logger;
import com.yoo.ymh.whoru.adapter.KaKaoSdkAdapter;

/**
 * Created by Yoo on 2016-09-13.
 */
public class WhoRUApplication extends Application{
    private static Context mContext;
    private static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FacebookSdk.sdkInitialize(this);
        KakaoSDK.init(new KaKaoSdkAdapter());
    }
    public static Context getWhoruContext(){
        return mContext;
    }

    public static Activity getCurrentActivity() {
        Logger.d("++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        WhoRUApplication.currentActivity = currentActivity;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mContext = null;
    }
}
