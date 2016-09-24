package com.yoo.ymh.whoru.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.adapter.AlarmRecyclerViewAdapter;
import com.yoo.ymh.whoru.model.AppAlarm;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.WhoRUApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.alarmFragment_swipe_refresh_layout)
    SwipeRefreshLayout alarmFragment_swipe_refresh_layout;
    @BindView(R.id.alarmFragment_recyclerview)
    RecyclerView alarmFragment_recyclerview;
    @BindView(R.id.fast_scroller)
    VerticalRecyclerViewFastScroller verticalRecyclerViewFastScroller;

    private CompositeSubscription compositeSubscription;
    private boolean isRefresh = true;
    private List<AppAlarm> appAlarmList;
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;

    public static AlarmFragment newInstance() {
        AlarmFragment alarmFragment = new AlarmFragment();
        Bundle args = new Bundle();
        alarmFragment.setArguments(args);
        return alarmFragment;
    }

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    public void initViews() {
        appAlarmList = new ArrayList<>();
        verticalRecyclerViewFastScroller.setRecyclerView(alarmFragment_recyclerview);
        alarmFragment_recyclerview.addOnScrollListener(verticalRecyclerViewFastScroller.getOnScrollListener());
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(getContext(), appAlarmList);
        alarmFragment_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        alarmFragment_recyclerview.setAdapter(alarmRecyclerViewAdapter);
    }

    public void initData() {
        alarmFragment_swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        alarmFragment_swipe_refresh_layout.setOnRefreshListener(this);
        alarmFragment_swipe_refresh_layout.post(() -> {
            alarmFragment_swipe_refresh_layout.setRefreshing(true);
            loadAlarmList();
        });
    }

    public void loadAlarmList() {
        showProgress();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getAlarmList(WhoRUApplication.getSessionId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(appAlarmList1 -> {
            hideProgress();
            appAlarmList = appAlarmList1.getData();
            alarmRecyclerViewAdapter.clear();
            alarmRecyclerViewAdapter.setItems(appAlarmList);
            alarmFragment_recyclerview.scrollToPosition(0);
        }));
    }

    public void showProgress() {
        if (!alarmFragment_swipe_refresh_layout.isRefreshing()) {
            alarmFragment_swipe_refresh_layout.setRefreshing(true);
        }
    }

    public void hideProgress() {
        if (alarmFragment_swipe_refresh_layout.isRefreshing()) {
            alarmFragment_swipe_refresh_layout.setRefreshing(false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        loadAlarmList();
    }
}