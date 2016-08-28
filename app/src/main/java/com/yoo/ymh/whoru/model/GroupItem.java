package com.yoo.ymh.whoru.model;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yoo.ymh.whoru.view.activity.GroupMemberManagerActivity;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoo on 2016-08-17.
 */
public class GroupItem extends AbstractExpandableAdapterItem {
    @BindView(R.id.group_recyclerview_item_group_name_textView)
    TextView group_recyclerview_item_group_name_textView;
    @BindView(R.id.group_recyclerview_item_group_setting_imageView)
    ImageView group_recyclerview_item_group_setting_imageView;
    @BindView(R.id.group_recyclerview_item_group_arrow_imageView)
    ImageView group_recyclerview_item_group_arrow_imageView;
    private Group mGroup;
    private RxBus _rxbus;

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            start = 0f;
            target = 90f;
        } else {
            start = 90f;
            target = 0f;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(group_recyclerview_item_group_arrow_imageView, View.ROTATION, start, target);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.group_reyclerview_item_group;
    }

    @Override
    public void onBindViews(final View root) {
        ButterKnife.bind(this, root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
                Toast.makeText(root.getContext(), group_recyclerview_item_group_name_textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(root.getContext(), "longclick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        group_recyclerview_item_group_setting_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(root);
            }
        });

    }

    @Override
    public void onSetViews() {
        group_recyclerview_item_group_arrow_imageView.setImageResource(0);
        group_recyclerview_item_group_arrow_imageView.setImageResource(R.drawable.ic_keyboard_arrow_right_white_48dp);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        onSetViews();
        onExpansionToggled(getExpandableListItem().isExpanded());
        mGroup = (Group) model;
        group_recyclerview_item_group_name_textView.setText(mGroup.name + " (" + mGroup.groupMemberNum + ")");
    }

    public void showDialog(final View root){
        MaterialDialog.Builder settingDialog = new MaterialDialog.Builder(root.getContext());
        settingDialog.title("그룹 관리")
                .items(R.array.group_settings)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        String selectedItem = text.toString();
                        switch (selectedItem) {
                            case "그룹원 관리":
                                Intent intent = new Intent(root.getContext(),GroupMemberManagerActivity.class);
                                intent.putExtra("groupName",mGroup.name);
                                root.getContext().startActivity(intent);
//
//                                Contact addItem = new Contact();
//                                addItem.setName("ggg");
//                                addItem.setPhone("123123");
//
//                                AddItem a = new AddItem();
//                                a.c = addItem;
//                                a.index = getItemIndex();
//                                _rxbus = RxBus.getInstance();
//                                if (getExpandableListItem().isExpanded()) {
//                                    doExpandOrUnexpand();
//                                    _rxbus.send(a);
//                                    doExpandOrUnexpand();
//                                }
//                                else {
//                                    _rxbus.send(a);
//                                }
                                return;
                            case "그룹명 수정":
                                Log.e("그룹명 수정", "수정");
                                MaterialDialog.Builder modifyGroupDialog = new MaterialDialog.Builder(root.getContext());
                                modifyGroupDialog.title("그룹명 수정")
                                        .inputType(InputType.TYPE_CLASS_TEXT)
                                        .input(null, mGroup.name, new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                if(input.length()==0)
                                                {
                                                    Toast.makeText(root.getContext(),"최소 1글자 이상 입력해주세요",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    mGroup.name = input.toString();
                                                    group_recyclerview_item_group_name_textView.setText(mGroup.name + " (" + mGroup.groupMemberNum + ")");
                                                }
                                            }
                                        })
                                        .show();
                                return;
                            case "그룹 삭제":

                                MaterialDialog.Builder deleteGroupDialog = new MaterialDialog.Builder(root.getContext());
                                deleteGroupDialog.title("그룹을 삭제하시겠습니까?")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                if (getExpandableListItem().isExpanded()) {
                                                    doExpandOrUnexpand();
                                                }
                                                _rxbus = RxBus.getInstance();
                                                if (_rxbus.hasObservers()) {
                                                    RemoveGroup removeGroup = new RemoveGroup();
                                                    removeGroup.setItemIndex(getItemIndex());
                                                    _rxbus.send(removeGroup);
                                                }
                                            }
                                        })
                                        .negativeText("취소")
                                        .positiveText("삭제")
                                        .show();
                                return;

                        }
                    }
                })
                .show();
    }
    public class RemoveGroup{
        int itemIndex;

        public int getItemIndex() {
            return itemIndex;
        }

        public void setItemIndex(int itemIndex) {
            this.itemIndex = itemIndex;
        }
    }
}
