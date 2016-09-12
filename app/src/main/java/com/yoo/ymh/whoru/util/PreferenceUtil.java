package com.yoo.ymh.whoru.util;

/**
 * Created by Yoo on 2016-09-08.
 */

import android.content.Context;

public class PreferenceUtil extends BasePreferenceUtil {
    private static PreferenceUtil _instance = null;

    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_PUSH_ID = "pushID";
    private static final String PROPERTY_PUSH_FLAG = "pushFlag";

    public static synchronized PreferenceUtil instance(Context $context) {
        if (_instance == null)
            _instance = new PreferenceUtil($context);
        return _instance;
    }

    protected PreferenceUtil(Context $context) {
        super($context);
    }

    public void putRedId(String $regId) {
        put(PROPERTY_REG_ID, $regId);
    }


    public void putRedPushId(String $regPushId) {
        put(PROPERTY_PUSH_ID, $regPushId);
    }

    public void putRedPushFlag(Boolean $regPushFlag) {
        put(PROPERTY_PUSH_FLAG, $regPushFlag);
    }


    public String regId() {
        return get(PROPERTY_REG_ID);
    }

    public String regPushID() {
        return get(PROPERTY_PUSH_ID);
    }

    public Boolean regPushFlag() {
        return get(PROPERTY_PUSH_FLAG, true);
    }


    public void putAppVersion(int $appVersion) {
        put(PROPERTY_APP_VERSION, $appVersion);
    }

    public int appVersion() {
        return get(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
    }
}