package com.yoo.ymh.whoru.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.AppGroup;
import com.yoo.ymh.whoru.model.AppGroupItem;
import com.yoo.ymh.whoru.model.AppGroupMemberItem;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.WhoRUApplication;
import com.yoo.ymh.whoru.view.activity.AddGroupActivity;
import com.yoo.ymh.whoru.view.activity.DetailContactActivity;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.view.activity.ModifyGroupMemberActivity;
import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.groupFragment_recyclerview)
    RecyclerView groupFragment_recyclerview;

    @BindView(R.id.groupFragment_swipe_refresh_layout)
    SwipeRefreshLayout groupFragment_swipe_refresh_layout;

    private final int ITEM_TYPE_GROUP = 1;
    private final int ITEM_TYPE_CONTACT = 2;

    private BaseExpandableAdapter mBaseExpandableAdapter;
    private List<AppGroup> mAppGroupList;
    private RxBus _rxBus;
    private CompositeSubscription compositeSubscription;
    private boolean isRefresh = true;

    public static GroupFragment newInstance() {
        GroupFragment groupFragment = new GroupFragment();
        Bundle args = new Bundle();
        groupFragment.setArguments(args);
        return groupFragment;
    }

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBusEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, v);
        initViews();
        return v;
    }

    private void initViews() {
        //retrofit 으로 그룹 가져오기.
        mAppGroupList = new ArrayList<>();
        groupFragment_swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        groupFragment_swipe_refresh_layout.setOnRefreshListener(this);
        groupFragment_swipe_refresh_layout.post(() -> {
            groupFragment_swipe_refresh_layout.setRefreshing(true);
            loadGroupAndMemberList();
            //loadGroupList();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
        //구독들 모두 해제
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        loadGroupAndMemberList();
        //loadGroupList();
    }

    public void showProgress() {
        if (!groupFragment_swipe_refresh_layout.isRefreshing()) {
            groupFragment_swipe_refresh_layout.setRefreshing(true);
        }
    }

    public void hideProgress() {
        if (groupFragment_swipe_refresh_layout.isRefreshing()) {
            groupFragment_swipe_refresh_layout.setRefreshing(false);
        }
    }

    private void RxBusEvent() {
        _rxBus = RxBus.getInstance();
        compositeSubscription = new CompositeSubscription();
        ConnectableObservable<Object> groupFragmentEventEmitter = _rxBus.toObserverable().publish();

        compositeSubscription//
                .add(groupFragmentEventEmitter.subscribe(event -> {
                    if ((event instanceof AddGroupActivity.AddGroup) || (event instanceof DetailContactActivity.SuccessModifyGroup) || (event instanceof ModifyGroupMemberActivity.ModifyGroupSuccess)) {
                        loadGroupAndMemberList();
                    } else if (event instanceof AppGroupItem.RemoveGroup) {
                        DeleteGroupId deleteGroupId = new DeleteGroupId();
                        deleteGroupId.setDeleteGroupId(((AppGroupItem.RemoveGroup) event).getRemoveGroupId());
                        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().deleteGroup(WhoRUApplication.getSessionId(), deleteGroupId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(s -> {
                                    mBaseExpandableAdapter.collapseAllParents();
                                    mBaseExpandableAdapter.removedItem(((AppGroupItem.RemoveGroup) event).getItemIndex());
                                }));
                    } else if(event instanceof AppGroupItem.ModifyGroupName)
                    {
                        AppGroupItem.ModifyGroupName modifyGroupName = (AppGroupItem.ModifyGroupName) event;
                        Log.e("modify",modifyGroupName.getName()+":"+modifyGroupName.getGroup());
                        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().modifyGroupName(WhoRUApplication.getSessionId(), modifyGroupName)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(s -> {},Throwable::printStackTrace));
                    }
                }));
        compositeSubscription.add(groupFragmentEventEmitter.connect());
    }

    public void loadGroupAndMemberList() {
        showProgress();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getGroupAndMemberList(WhoRUApplication.getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groupList -> {
                    hideProgress();
                    mAppGroupList = groupList.getData();
                    mBaseExpandableAdapter = new BaseExpandableAdapter(mAppGroupList) {
                        @NonNull
                        @Override
                        public AbstractAdapterItem<Object> getItemView(Object type) {
                            int itemType = (int) type;
                            switch (itemType) {
                                case ITEM_TYPE_GROUP:
                                    return new AppGroupItem();
                                case ITEM_TYPE_CONTACT:
                                    return new AppGroupMemberItem();
                            }
                            return null;
                        }

                        @Override
                        public Object getItemViewType(Object t) {
                            if (t instanceof AppGroup) {
                                return ITEM_TYPE_GROUP;
                            } else if (t instanceof AppContact)
                                return ITEM_TYPE_CONTACT;
                            return -1;
                        }
                    };
                    groupFragment_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    groupFragment_recyclerview.setAdapter(mBaseExpandableAdapter);
                }, Throwable::printStackTrace));
    }

    public class DeleteGroupId {
        @SerializedName("_group")
        @Expose
        int deleteGroupId;

        public int getDeleteGroupId() {
            return deleteGroupId;
        }

        public void setDeleteGroupId(int deleteGroupId) {
            this.deleteGroupId = deleteGroupId;
        }
    }
}
