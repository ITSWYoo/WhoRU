package com.yoo.ymh.whoru.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.PreferenceUtil;
import com.yoo.ymh.whoru.util.WhoRUApplication;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yoo on 2016-09-14.
 */
public class KaKaoSignUpActivity extends Activity{
    private CompositeSubscription compositeSubscription;

    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        requestMe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                String userId = String.valueOf(userProfile.getId());

                TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = setPhoneNumber(tMgr.getLine1Number());
                AppContact loginContact = new AppContact();
                loginContact.setProvider("kakaotalk");
                loginContact.setProviderId(userId);
                loginContact.setName(userProfile.getNickname());
                loginContact.setPhone(mPhoneNumber);
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().loginOrJoinUser(loginContact)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(appUser -> {
                            if (appUser.getCode() == 1 || appUser.getCode() == 2) {
                                PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).putRedKakaoId(userId);
                                WhoRUApplication.setSessionId(appUser.getSessionId());
                                redirectMainActivity(); // 로그인 성공시 MainActivity로
                            } else {
                                Toast.makeText(getApplicationContext(), appUser.getSessionId() + " ID가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                                UserManagement.requestLogout(new LogoutResponseCallback() {
                                    @Override
                                    public void onCompleteLogout() {
                                        redirectLoginActivity();
                                    }
                                });
                            }
                        },Throwable::printStackTrace));


            }
        });
    }

    private void redirectMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public String setPhoneNumber(String phoneNumber) {
        String phone_number = null;
        if (phoneNumber.startsWith("+82")) {
            if (phoneNumber.length() == 14) phoneNumber = phoneNumber.replace("+82", "");
            else phoneNumber = phoneNumber.replace("+82", "0");

        }
        if (phoneNumber.startsWith("+082")) {
            if (phoneNumber.length() == 15) phoneNumber = phoneNumber.replace("+082", "");
            else phoneNumber = phoneNumber.replace("+082", "0");
        }

        if (phoneNumber.length() == 11) {
            phone_number = String.format("%s-%s-%s", phoneNumber.substring(0, 3), phoneNumber.substring(3, 7), phoneNumber.substring(7, 11));
        }
        if (phoneNumber.length() == 10) {
            phone_number = String.format("%s-%s-%s", phoneNumber.substring(0, 3), phoneNumber.substring(3, 6), phoneNumber.substring(6, 10));
        }
        if (phoneNumber.length() == 9) {
            phone_number = String.format("%s-%s-%s", phoneNumber.substring(0, 2), phoneNumber.substring(2, 5), phoneNumber.substring(5, 9));
        }
        return phone_number;
    }
}
