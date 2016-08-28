package com.yoo.ymh.whoru.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.Group;
import com.yoo.ymh.whoru.model.GroupItem;
import com.yoo.ymh.whoru.util.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupAddActivity extends AppCompatActivity {
    private RxBus _rxBus = null;

    @BindView(R.id.groupAddActivity_toolbar)
    Toolbar groupAddActivity_toolbar;

    @BindView(R.id.groupAddActivity_editText_groupName)
    MaterialEditText groupAddActivity_editText_groupName;

    @BindView(R.id.groupAddActivity_button_makeGroup)
    Button groupAddActivity_button_makeGroup;

    @OnClick(R.id.groupAddActivity_button_makeGroup) public void makeGroup(){

        if(groupAddActivity_editText_groupName.getText().length()<1)
        {
            Toast.makeText(getApplicationContext(),"1글자 이상 입력해주세요",Toast.LENGTH_SHORT).show();
        }
        else {
            if (_rxBus.hasObservers()) {
                String groupName = groupAddActivity_editText_groupName.getText().toString();
                Group addItem = new Group();
                addItem.name = groupName;
                AddGroup addGroup = new AddGroup();
                addGroup.setAddItem(addItem);
                _rxBus.send(addGroup);
                finish();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        _rxBus = RxBus.getInstance();
        ButterKnife.bind(this);
        initViews();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return true;
    }
    void initViews(){
        groupAddActivity_toolbar.setTitle("그룹 추가");
        setSupportActionBar(groupAddActivity_toolbar);
        groupAddActivity_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        groupAddActivity_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public class AddGroup{
        Group addItem;

        public Group getAddItem() {
            return addItem;
        }

        public void setAddItem(Group addItem) {
            this.addItem = addItem;
        }
    }
}
