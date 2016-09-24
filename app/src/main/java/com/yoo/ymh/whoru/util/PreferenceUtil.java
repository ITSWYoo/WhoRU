package com.yoo.ymh.whoru.util;

/**
 * Created by Yoo on 2016-09-08.
 */

import android.content.Context;

public class PreferenceUtil extends BasePreferenceUtil {
    private static PreferenceUtil _instance = null;

    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_REG_FACEBOOK_ID = "facebook_id";
    private static final String PROPERTY_REG_KAKAO_ID = "kakao_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_PUSH_ID = "pushID";
    private static final String PROPERTY_PUSH_FLAG = "pushFlag";
    private static final String PROPERTY_PHONE_NUMBER = "phone_number";

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

    public void putRedFacebookId(String $regFacebookId)
    {
        put(PROPERTY_REG_FACEBOOK_ID, $regFacebookId);
    }

    public void putRedKakaoId(String $regKakaoId)
    {
        put(PROPERTY_REG_KAKAO_ID, $regKakaoId);
    }



    public void putRedPushFlag(Boolean $regPushFlag) {
        put(PROPERTY_PUSH_FLAG, $regPushFlag);
    }

    public void putRedPhoneNumber(String $regPhoneNumber){ put(PROPERTY_PHONE_NUMBER,$regPhoneNumber);}

    public String regId() {
        return get(PROPERTY_REG_ID);
    }

    public String regFacebookId(){
        return get(PROPERTY_REG_FACEBOOK_ID);
    }
    public String regKakaoId(){
        return get(PROPERTY_REG_KAKAO_ID);
    }
    public String regPhoneNumber(){
        return get(PROPERTY_PHONE_NUMBER);
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