package com.yoo.ymh.whoru.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.util.helper.log.Logger;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.PreferenceUtil;
import com.yoo.ymh.whoru.util.WhoRUApplication;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplahActivity extends AppCompatActivity {
    AccessTokenTracker accessTokenTracker;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah);
        checkAutoLoginFaceBook();
        checkAutoLoginKaKao();
        if (PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regFacebookId() == null && PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regKakaoId() == null) {
            Log.e("SplashActivity", "gotoLogin");
            goToLoginActivity();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (accessTokenTracker != null)
            accessTokenTracker.stopTracking();
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(SplahActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(SplahActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkAutoLoginFaceBook() {
        if (PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regFacebookId() != null && PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regFacebookId().length() > 0) {
            callbackManager = CallbackManager.Factory.create();
            Observable.timer(1, TimeUnit.SECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        accessTokenTracker = new AccessTokenTracker() {
                            @Override
                            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                                String userId = PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regFacebookId();
                                if (currentAccessToken != null && currentAccessToken.getUserId().equals(userId)) {
                                    goToMainActivity();
                                } else {
                                    goToLoginActivity();
                                }
                            }
                        };

                        String facebookId = PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regFacebookId();

                        if (facebookId != null) {
                            if (!facebookId.equals("")) {
                                LoginManager.getInstance().logInWithReadPermissions(this, null);
                            }
                        } else {
                            goToLoginActivity();
                        }
                    });
        }
    }

    public void checkAutoLoginKaKao() {
        if (PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regKakaoId() != null && PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regKakaoId().length() > 0) {
            Observable.timer(1, TimeUnit.SECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        AuthService.requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                goToLoginActivity();
                            }

                            @Override
                            public void onNotSignedUp() {
                                // not happened
                            }

                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Logger.e("failed to get access token info. msg=" + errorResult);
                            }

                            @Override
                            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                                String userId = String.valueOf(accessTokenInfoResponse.getUserId());
                                Log.e("userid",userId + ":" + PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regKakaoId());

                                Logger.d("this access token is for userId=" + userId);
                                if (userId.equals(PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regKakaoId())) {
                                    Log.e("SplashActivity", "loginKakao");
                                    goToMainActivity();
                                } else {
                                    goToLoginActivity();
                                }
                                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
                            }
                        });
                    });
        }
    }
}

