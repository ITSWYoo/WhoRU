package com.yoo.ymh.whoru.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.adapter.ContactRecyclerViewAdapter;
import com.yoo.ymh.whoru.model.Contact;
import com.yoo.ymh.whoru.model.Group;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupMemberManagerActivity extends AppCompatActivity {
    @BindView(R.id.groupMemberManagerActivity_toolbar)
    Toolbar groupMemberManagerActivity_toolbar;

    @BindView(R.id.groupMemberManagerActivity_recyclerview)
    RecyclerView groupMemberManagerActivity_recyclerview;
    private CheckBox checkBox;
    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;
    private List<Contact> contactList;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_manager);
        ButterKnife.bind(this);
        initData();
        initViews();

        Intent i = new Intent();
        i.putExtra("contact",new Contact());
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_member_manager_menu, menu);

        checkBox = (CheckBox) menu.findItem(R.id.action_checkAll).getActionView();
        checkBox.setButtonTintList(getResources().getColorStateList(R.color.cardview_light_background));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox.isChecked())
                {
                    Log.e("Asdf","체크");
                }
                else
                {
                    Log.e("ㅁㄴㅇㄹ","안체크");
                }
            }
        });
        return true;
    }

    private void initViews() {
        groupMemberManagerActivity_toolbar.setTitle("그룹원 관리");
        setSupportActionBar(groupMemberManagerActivity_toolbar);
        groupMemberManagerActivity_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        groupMemberManagerActivity_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        groupMemberManagerActivity_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_done) {
                    Log.e("완료", "완료");
                    finish();
                }
                return true;
            }
        });

        final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        groupMemberManagerActivity_recyclerview.setLayoutManager(manager);
        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(getApplicationContext(), contactList, true);
        groupMemberManagerActivity_recyclerview.setAdapter(contactRecyclerViewAdapter);
    }
    public void initData() {
        Intent intent = new Intent();
        String groupName = intent.getStringExtra("groupName");
        contactList = new ArrayList<>();
    }


}
