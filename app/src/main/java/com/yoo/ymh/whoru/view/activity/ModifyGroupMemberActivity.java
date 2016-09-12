package com.yoo.ymh.whoru.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.adapter.ContactRecyclerViewAdapter;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.Group;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ModifyGroupMemberActivity extends AppCompatActivity {
    @BindView(R.id.groupMemberManagerActivity_toolbar)
    Toolbar groupMemberManagerActivity_toolbar;
    @BindView(R.id.groupMemberManagerActivity_recyclerview)
    RecyclerView groupMemberManagerActivity_recyclerview;
    private CheckBox checkBox;
    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;
    private List<AppContact> appContactList;
    private RxBus _rxbus;
    private CompositeSubscription compositeSubscription;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_manager);
        ButterKnife.bind(this);
        _rxbus = RxBus.getInstance();
        compositeSubscription = new CompositeSubscription();
        initViews();
        initData();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_member_manager_menu, menu);
        checkBox = (CheckBox) menu.findItem(R.id.action_checkAll).getActionView();
        checkBox.setButtonTintList(getResources().getColorStateList(R.color.cardview_light_background));
        checkBox.setOnCheckedChangeListener((compoundButton, b) ->
        {
            if (checkBox.isChecked()) {
                contactRecyclerViewAdapter.checkedAllItem(true);
            } else {
                contactRecyclerViewAdapter.checkedAllItem(false);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_done:
                modifyGroupMemeber();
                finish();
                break;
        }
        return true;
    }

    private void initViews() {
        groupMemberManagerActivity_toolbar.setTitle("그룹원 관리");
        setSupportActionBar(groupMemberManagerActivity_toolbar);
        groupMemberManagerActivity_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        groupMemberManagerActivity_toolbar.setNavigationOnClickListener(view -> onBackPressed());

        final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        groupMemberManagerActivity_recyclerview.setLayoutManager(manager);
        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(getApplicationContext(), appContactList, true);
        groupMemberManagerActivity_recyclerview.setAdapter(contactRecyclerViewAdapter);
    }

    public void initData() {
        group = getIntent().getParcelableExtra("groupInfo");
        appContactList = new ArrayList<>();
        appContactList = group.getmGroupMembers();
        loadContactList();
    }

    public void loadContactList() {
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getAllContactList("abcd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appContactList1 -> {
                    for (AppContact groupMemeber : appContactList) {
                        for (AppContact c : appContactList1.getData()) {
                            if (c.getId() == groupMemeber.getId()) {
                                c.setSelected(true);
                            }
                        }
                    }
                    contactRecyclerViewAdapter.setItems(appContactList1.getData());
                }));
    }
    public void modifyGroupMemeber(){
        ModifyGroupMember modifyGroupMember= new ModifyGroupMember();
        modifyGroupMember.setGroup(group.getId());
        List<Integer> modifyGroupMemberId = new ArrayList<>();
        for(AppContact c : contactRecyclerViewAdapter.getCheckedItemList())
        {
            modifyGroupMemberId.add(c.getId());
        }
        if(modifyGroupMemberId.size()==0) modifyGroupMemberId.add(0);
        modifyGroupMember.setList(modifyGroupMemberId);
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().modifyGroupMemberList("abcd",modifyGroupMember)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    _rxbus.send(new ModifyGroupSuccess());
                    finish();
                },Throwable::printStackTrace));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }
    public class ModifyGroupMember{
        @SerializedName("_group")
        @Expose
        int groupId;
        @SerializedName("_list")
        @Expose
        List<Integer> groupMemberIdList = new ArrayList<>();

        /**
         *
         * @return
         * The groupId
         */
        public Integer getGroup() {
            return groupId;
        }

        /**
         *
         * @param groupId
         * The _group
         */
        public void setGroup(Integer groupId) {
            this.groupId = groupId;
        }

        /**
         *
         * @return
         * The groupMemberIdList
         */
        public List<Integer> getList() {
            return groupMemberIdList;
        }

        /**
         *
         * @param groupMemberIdList
         * The _list
         */
        public void setList(List<Integer> groupMemberIdList) {
            this.groupMemberIdList = groupMemberIdList;
        }

    }
    public class ModifyGroupSuccess{}
}
