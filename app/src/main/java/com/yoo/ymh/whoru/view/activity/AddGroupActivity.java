package com.yoo.ymh.whoru.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.Group;
import com.yoo.ymh.whoru.model.GroupList;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddGroupActivity extends AppCompatActivity {
    private RxBus _rxBus = null;
    private CompositeSubscription compositeSubscription;
    private GroupList myGroupList;
    @BindView(R.id.groupAddActivity_toolbar)
    Toolbar groupAddActivity_toolbar;

    @BindView(R.id.groupAddActivity_editText_groupName)
    MaterialEditText groupAddActivity_editText_groupName;

    @BindView(R.id.groupAddActivity_button_makeGroup)
    Button groupAddActivity_button_makeGroup;

    @OnClick(R.id.groupAddActivity_button_makeGroup)
    public void makeGroup() {
        createGroup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        _rxBus = RxBus.getInstance();
        ButterKnife.bind(this);
        initViews();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            createGroup();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getGroupList("abcd")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(groupList -> myGroupList = groupList)
        );
    }

    void initViews() {
        groupAddActivity_toolbar.setTitle("그룹 추가");
        setSupportActionBar(groupAddActivity_toolbar);
        groupAddActivity_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        groupAddActivity_toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    public class AddGroup {
        Group addItem;

        public Group getAddItem() {
            return addItem;
        }

        public void setAddItem(Group addItem) {
            this.addItem = addItem;
        }
    }

    public void createGroup() {
        if (groupAddActivity_editText_groupName.getText().length() < 1) {
            Toast.makeText(getApplicationContext(), "1글자 이상 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            if (checkGroupList()) {
                String groupName = groupAddActivity_editText_groupName.getText().toString();
                Group addItem = new Group();
                addItem.setName(groupName);
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().addGroup("abcd", addItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            if (_rxBus.hasObservers()) {
                                AddGroup addGroup = new AddGroup();
                                addGroup.setAddItem(addItem);
                                _rxBus.send(addGroup);
                                finish();
                            }
                        }));
            }
        }
    }

    public boolean checkGroupList() {
        if (myGroupList != null && myGroupList.getTotal() > 0) {
            for (int i = 0; i < myGroupList.getTotal(); i++) {
                if (myGroupList.getData().get(i).getName().equals(groupAddActivity_editText_groupName.getText().toString())) {
                    Toast.makeText(AddGroupActivity.this, "이미 존재하는 그룹입니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
