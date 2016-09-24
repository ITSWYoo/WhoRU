package com.yoo.ymh.whoru.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.PreferenceUtil;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.util.WhoRUApplication;
import com.kakao.auth.ISessionCallback;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.loginActivity_button_facebook)
    LoginButton loginActivity_button;
    @BindView(R.id.loginActivity_button_google)
    SignInButton loginActivity_button_google;

    //facebook
    private CallbackManager callbackManager;
    private SessionCallback sessionCallback;
    private CompositeSubscription compositeSubscription;
    private TelephonyManager tMgr;
    private String mPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getPermission();
        compositeSubscription = new CompositeSubscription();
        loginFacebook();
        loginKakao();

    }
    public void getPermission() {
        TedPermission tedPermission = new TedPermission(getApplicationContext());
        tedPermission
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions( Manifest.permission.READ_PHONE_STATE)
                .check();
    }


    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = setPhoneNumber(tMgr.getLine1Number());
            PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).putRedPhoneNumber(mPhoneNumber);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(LoginActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //kakao
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
        compositeSubscription.clear();
    }

    public void loginFacebook() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                String token = accessToken.getToken(); //로그인 성공시 가져오는 토큰

                AppContact loginContact = new AppContact();
                loginContact.setProvider("facebook");
                loginContact.setProviderId(accessToken.getUserId());
                loginContact.setPhone(mPhoneNumber);
                loginContact.setName(Profile.getCurrentProfile().getName());
//                loginContact.setProfileImage(Profile.getCurrentProfile().getProfilePictureUri(100,100));
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().loginOrJoinUser(loginContact)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(appUser -> {
                            if (appUser.getCode() == 1 || appUser.getCode() == 2) {
                                PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).putRedFacebookId(accessToken.getUserId());
                                WhoRUApplication.setSessionId(appUser.getSessionId());
                                Log.e("LoginActivity", "loginFacebook");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), appUser.getSessionId() + " ID가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                            }
                        },Throwable::printStackTrace));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void loginKakao() {
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login);
        }
    }

    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, KaKaoSignUpActivity.class);
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
