package com.yoo.ymh.whoru.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yoo on 2016-09-01.
 */
public class WhoRURetrofit {
    public static final String HOST = "http://52.78.32.128:4464/";
    public static WhoRUApi whoRUApi;
    public static Retrofit retrofit;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    static {
    }

    public static WhoRUApi getWhoRURetorfitInstance() {

        if (whoRUApi == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("retrofit", message);
                }
            });
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(HOST)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .client(httpClient.build())
                    .build();
            whoRUApi = retrofit.create(WhoRUApi.class);
        }
        return whoRUApi;
    }
}
