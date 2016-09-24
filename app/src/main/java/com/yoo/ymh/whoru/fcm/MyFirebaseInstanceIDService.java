package com.yoo.ymh.whoru.fcm;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yoo.ymh.whoru.model.FcmToken;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.WhoRUApplication;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.schedulers.Schedulers;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    public MyFirebaseInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

//        OkHttpClient client = new OkHttpClient();
        FcmToken fcmToken = new FcmToken();
        fcmToken.setToken(token);

        if(WhoRUApplication.getSessionId()!=null && WhoRUApplication.getSessionId().length()>0) {
            WhoRURetrofit.getWhoRURetorfitInstance().sendFcmToken(WhoRUApplication.getSessionId(), fcmToken)
                    .observeOn(Schedulers.io())
                    .subscribe(s -> {
                    }, Throwable::printStackTrace);
        }
//
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}
