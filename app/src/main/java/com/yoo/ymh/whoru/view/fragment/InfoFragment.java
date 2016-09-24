package com.yoo.ymh.whoru.view.fragment;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.GeocodingTask;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.util.WhoRUApplication;
import com.yoo.ymh.whoru.view.activity.AddContactActivity;
import com.yoo.ymh.whoru.view.activity.DetailContactActivity;

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
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements OnMapReadyCallback {
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

    private CompositeSubscription compositeSubscription;
    private RxBus _rxBus;
    private ArrayList<String> snsList;
    private int geocoderRetryCount = 0;
    private AppContact myContact;

    public static InfoFragment newInstance() {
        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        infoFragment.setArguments(args);
        return infoFragment;
    }

    public InfoFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
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
        mapView.onCreate(savedInstanceState);
        rxBusEvent();
        initViews();
        return v;
    }

    public void initViews() {
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getMyInfo(WhoRUApplication.getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appContact -> loadView(appContact), Throwable::printStackTrace));
    }

    public void loadView(AppContact appContact) {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.mainActivity_navigationView);
        ImageView mainActivity_imageView_header = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.mainActivity_imageView_header);
        TextView mainActivity_textView_card_default = (TextView) getActivity().findViewById(R.id.mainActivity_textView_card_default);
        ImageView mainActivity_imageView_card = (ImageView) getActivity().findViewById(R.id.mainActivity_imageView_card);

        if (appContact.getProfileThumbnail() != null) {
            Glide.with(getContext()).load(appContact.getProfileThumbnail()).into(infoFragment_circleImageView_profile);
            Glide.with(getContext()).load(appContact.getProfileThumbnail()).into(mainActivity_imageView_header);
        } else {
            infoFragment_circleImageView_profile.setImageResource(R.drawable.ic_regit_user);
            mainActivity_imageView_header.setImageResource(R.drawable.ic_regit_user);
        }
        if (appContact.getCardImageThumbnail() != null && appContact.getCardImageThumbnail().length() > 0) {
            mainActivity_textView_card_default.setVisibility(View.INVISIBLE);
            Glide.with(getContext()).load(appContact.getCardImageThumbnail()).into(mainActivity_imageView_card);
        } else {
            mainActivity_textView_card_default.setVisibility(View.VISIBLE);
            mainActivity_imageView_card.setImageResource(0);
            mainActivity_imageView_card.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
        infoFragment_textView_name.setText(appContact.getName());
        if (appContact.getResponsibility() != null && appContact.getResponsibility().length() > 0) {
            infoFragment_textView_responsibility.setText(appContact.getResponsibility());
            infoFragment_textView_responsibility.setVisibility(View.VISIBLE);
        } else infoFragment_textView_responsibility.setVisibility(View.GONE);
        if (appContact.getDepartment() != null && appContact.getDepartment().length() > 0) {
            infoFragment_textView_department.setText(appContact.getDepartment());
            infoFragment_textView_department.setVisibility(View.VISIBLE);
        } else infoFragment_textView_department.setVisibility(View.GONE);
        if (appContact.getCompany() != null && appContact.getCompany().length() > 0) {
            infoFragment_textView_company.setText(appContact.getCompany());
            infoFragment_textView_company.setVisibility(View.VISIBLE);
        } else infoFragment_textView_company.setVisibility(View.GONE);

        infoFragment_textView_phoneNumber.setText(appContact.getPhone());

        if (appContact.getEmail().length() > 0) {
            infoFragment_textView_email.setText(appContact.getEmail());
            infoFragment_layout_email.setVisibility(View.VISIBLE);
        } else infoFragment_layout_email.setVisibility(View.GONE);
        if (appContact.getCompanyPhone().length() > 0) {
            infoFragment_textView_companyNumber.setText(appContact.getCompanyPhone());
            infoFragment_layout_companyNumber.setVisibility(View.VISIBLE);
        } else infoFragment_layout_companyNumber.setVisibility(View.GONE);
        if (appContact.getFaxPhone().length() > 0) {
            infoFragment_textView_faxNumber.setText(appContact.getFaxPhone());
            infoFragment_layout_faxNumber.setVisibility(View.VISIBLE);
        } else infoFragment_layout_faxNumber.setVisibility(View.GONE);
        if (appContact.getCompanyAddress().length() > 0) {
            infoFragment_layout_companyAddress.setVisibility(View.VISIBLE);
            infoFragment_textView_companyAddress.setText(appContact.getCompanyAddress());
            mapView.getMapAsync(this);
        } else infoFragment_layout_companyAddress.setVisibility(View.GONE);

        snsList = new ArrayList<>();
        if (appContact.getFacebookAddress().length() > 0) {
            snsList.add(appContact.getFacebookAddress());
            infoFragment_layout_facebook.setVisibility(View.VISIBLE);
            infoFragment_textView_facebook.setText(appContact.getFacebookAddress());
            infoFragment_textView_facebook.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (infoFragment_textView_facebook.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoFragment_textView_facebook.getText().toString()));
                } else {
                    url += infoFragment_textView_facebook.getText().toString();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
                startActivity(intent);
            });
        } else infoFragment_layout_facebook.setVisibility(View.GONE);
        if (appContact.getGoogleAddress().length() > 0) {
            snsList.add(appContact.getGoogleAddress());
            infoFragment_layout_google.setVisibility(View.VISIBLE);
            infoFragment_textView_google.setText(appContact.getGoogleAddress());
            infoFragment_textView_google.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (infoFragment_textView_google.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoFragment_textView_google.getText().toString()));
                } else {
                    url += infoFragment_textView_google.getText().toString();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
                startActivity(intent);
            });
        } else infoFragment_layout_google.setVisibility(View.GONE);
        if (appContact.getLinkedinAddress().length() > 0) {
            snsList.add(appContact.getLinkedinAddress());
            infoFragment_layout_linkedIn.setVisibility(View.VISIBLE);
            infoFragment_textView_linkedIn.setText(appContact.getLinkedinAddress());
            infoFragment_textView_linkedIn.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (infoFragment_textView_linkedIn.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoFragment_textView_linkedIn.getText().toString()));
                } else {
                    url += infoFragment_textView_linkedIn.getText().toString();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
                startActivity(intent);
            });
        } else infoFragment_layout_linkedIn.setVisibility(View.GONE);
        if (appContact.getInstagramAddress().length() > 0) {
            snsList.add(appContact.getInstagramAddress());
            infoFragment_layout_instagram.setVisibility(View.VISIBLE);
            infoFragment_textView_instagram.setText(appContact.getInstagramAddress());
            infoFragment_textView_instagram.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (infoFragment_textView_instagram.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoFragment_textView_instagram.getText().toString()));
                } else {
                    url += infoFragment_textView_instagram.getText().toString();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
                startActivity(intent);
            });
        } else infoFragment_layout_instagram.setVisibility(View.GONE);
        if (appContact.getExtraAddress().length() > 0) {
            snsList.add(appContact.getExtraAddress());
            infoFragment_layout_anotherSns.setVisibility(View.VISIBLE);
            infoFragment_textView_anotherSns.setText(appContact.getExtraAddress());
            infoFragment_textView_anotherSns.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (infoFragment_textView_anotherSns.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoFragment_textView_anotherSns.getText().toString()));
                } else {
                    url += infoFragment_textView_anotherSns.getText().toString();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
                startActivity(intent);
            });
        } else infoFragment_layout_anotherSns.setVisibility(View.GONE);
        if (snsList.size() > 0) {
            infoFragment_layout_sns.setVisibility(View.VISIBLE);
        } else {
            infoFragment_layout_sns.setVisibility(View.GONE);
        }
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
        compositeSubscription.clear();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setMapAddress();
    }

    public void setMapAddress() {
        geocoderRetryCount = 0;
        GeocodingTask.GeocodingListener geocodingListener = new GeocodingTask.GeocodingListener() {
            @Override
            public void onSuccess(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions().position(latLng).title("회사"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mapView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorMessage) {
                if (geocoderRetryCount >= 3) {
                } else {
                    geocoderRetryCount++;
                    new GeocodingTask(getContext(), this).execute(infoFragment_textView_companyAddress.getText().toString());
                }
            }
        };
        new GeocodingTask(getContext(), geocodingListener).execute(infoFragment_textView_companyAddress.getText().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.info_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_modify_contact) {
            Intent intent = new Intent(getContext(), AddContactActivity.class);
            intent.putExtra("modifyMyInfo", 1);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void rxBusEvent() {
        _rxBus = RxBus.getInstance();
        compositeSubscription = new CompositeSubscription();

        ConnectableObservable<Object> InfotFragmentEmitter = _rxBus.toObserverable().publish();

        //publish는 보통 Observable을 ConnectableObservable로 변환
        //구독 여부와 상관없다. Connect라는 메서드가 불려야만 항목들을 배출하는 Observable.

        compositeSubscription//
                .add(InfotFragmentEmitter.subscribe(event -> {
                    if (event instanceof AddContactActivity.ModifyMyContact) {
                        initViews();
                    }
                }));
        compositeSubscription.add(InfotFragmentEmitter.connect());
        //이 시점부터 Observable이 배출. 위에 Subscribe가 먼저있다.. 고로 구독하고나서부터 배출을 할수밖에없다.->다 잡을수있다.
    }
}
