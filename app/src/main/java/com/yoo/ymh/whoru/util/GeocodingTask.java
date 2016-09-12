package com.yoo.ymh.whoru.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.util.Dlog;
import com.gun0912.tedpermission.util.ObjectUtils;

import java.util.List;
import java.util.Locale;


/**
 * Created by Yoo on 2016-09-08.
 */
public class GeocodingTask extends AsyncTask<String, Void, LatLng> {

    Context context;
    GeocodingListener geocodingListener;

    public GeocodingTask(Context context, GeocodingListener geocodingListener) {
        super();
        this.context = context;
        this.geocodingListener = geocodingListener;
    }

    @Override
    protected LatLng doInBackground(String... strings) {
        String addressString = strings[0];
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        Dlog.d("addressString: " + addressString);
        addressString = addressString.replace("전체", "");

        try {
            List<Address> listAddress = geocoder.getFromLocationName(addressString, 1);

            if (ObjectUtils.isEmpty(listAddress)) {
                if (geocodingListener != null) {
                    geocodingListener.onError("\'" + addressString + "\'의 주소값을 가져오지 못했습니다..");
                    return null;
                }
            }

            android.location.Address addr = listAddress.get(0);
            LatLng latLng = new LatLng(addr.getLatitude(), addr.getLongitude());

            return latLng;
        } catch (Exception e) {
            Dlog.e(e.getMessage());
            Dlog.e(e.getLocalizedMessage());
        }

        if (geocodingListener != null) {
            geocodingListener.onError("주소를 선택하는데 문제가 발생했습니다..");
        }

        return null;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        if (latLng != null && geocodingListener != null) {
            geocodingListener.onSuccess(latLng);
        }
//        else geocodingListener.onError("주소를 선택하는데 문제가 발생했습니다..");
    }

    public interface GeocodingListener {
        void onSuccess(LatLng latLng);

        void onError(String errorMessage);
    }
}
