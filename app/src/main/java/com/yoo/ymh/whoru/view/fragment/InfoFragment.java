package com.yoo.ymh.whoru.view.fragment;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yoo.ymh.whoru.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements OnMapReadyCallback {
    @BindView(R.id.infoFragment_imageView_basic_setting)
    ImageView infoFragment_imageView_basic_setting;

    @BindView(R.id.infoFragment_circleImageView_profile)
    ImageView infoFragment_circleImageView_profile;

    @BindView(R.id.infoFragment_textView_name)
    TextView infoFragment_textView_name;

    @BindView(R.id.infoFragment_textView_company)
    TextView infoFragment_textView_company;

    @BindView(R.id.infoFragment_textView_department)
    TextView infoFragment_textView_department;

    @BindView(R.id.infoFragment_textView_responsibility)
    TextView infoFragment_textView_responsibility;

    @BindView(R.id.infoFragment_textView_memo)
    TextView infoFragment_textView_memo;

    @BindView(R.id.infoFragment_textView_phoneNumber)
    TextView infoFragment_textView_phoneNumber;

    @BindView(R.id.infoFragment_textView_companyNumber)
    TextView infoFragment_textView_companyNumber;
    @BindView(R.id.infoFragment_layout_companyNumber)
    LinearLayout infoFragment_layout_companyNumber;

    @BindView(R.id.infoFragment_textView_faxNumber)
    TextView infoFragment_textView_faxNumber;
    @BindView(R.id.infoFragment_layout_faxNumber)
    LinearLayout infoFragment_layout_faxNumber;

    @BindView(R.id.infoFragment_textView_email)
    TextView infoFragment_textView_email;
    @BindView(R.id.infoFragment_layout_email)
    LinearLayout infoFragment_layout_email;

    @BindView(R.id.infoFragment_textView_companyAddress)
    TextView infoFragment_textView_companyAddress;
    @BindView(R.id.infoFragment_layout_companyAddress)
    LinearLayout infoFragment_layout_companyAddress;

    @BindView(R.id.infoFragment_mapView)
    MapView mapView;
    private GoogleMap googleMap;
    private List<Address> addressList;
    private Address address;

    @BindView(R.id.infoFragment_layout_sns)
    LinearLayout infoFragment_layout_sns;

    @BindView(R.id.infoFragment_textView_facebook)
    TextView infoFragment_textView_facebook;
    @BindView(R.id.infoFragment_layout_facebook)
    LinearLayout infoFragment_layout_facebook;

    @BindView(R.id.infoFragment_textView_google)
    TextView infoFragment_textView_google;
    @BindView(R.id.infoFragment_layout_google)
    LinearLayout infoFragment_layout_google;

    @BindView(R.id.infoFragment_textView_linkedIn)
    TextView infoFragment_textView_linkedIn;
    @BindView(R.id.infoFragment_layout_linkedIn)
    LinearLayout infoFragment_layout_linkedIn;

    @BindView(R.id.infoFragment_textView_instagram)
    TextView infoFragment_textView_instagram;
    @BindView(R.id.infoFragment_layout_instagram)
    LinearLayout infoFragment_layout_instagram;

    @BindView(R.id.infoFragment_textView_anotherSns)
    TextView infoFragment_textView_anotherSns;
    @BindView(R.id.infoFragment_layout_anotherSns)
    LinearLayout infoFragment_layout_anotherSns;


    public static InfoFragment newInstance() {
        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        infoFragment.setArguments(args);
        return infoFragment;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, v);
        infoFragment_imageView_basic_setting.setOnClickListener(view -> {
//                Intent intent = new Intent(getContext(), ModifyContactActivity.class);
//                startActivity(intent);
        });
        mapView.onCreate(savedInstanceState);
        if(infoFragment_textView_companyAddress.getText().length()>0) {
            mapView.getMapAsync(this);
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Geocoder geocoder = new Geocoder(getContext());
        addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName("강남역", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList == null || addressList.size() == 0) {
            mapView.setVisibility(View.GONE);
        } else {
            address = addressList.get(0);
            Log.e("aaasf", address.toString());
            googleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title("maker"));
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }
}
