package com.yoo.ymh.whoru.util;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;

/**
 * Created by Yoo on 2016-09-13.
 */
public class WhoRUApplication extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FacebookSdk.sdkInitialize(this);
    }
    public static Context getMentorContext(){
        return mContext;
    }
}
