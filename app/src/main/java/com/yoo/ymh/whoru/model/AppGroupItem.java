package com.yoo.ymh.whoru.model;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yoo.ymh.whoru.view.activity.ModifyGroupMemberActivity;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoo on 2016-08-17.
 */
public class AppGroupItem extends AbstractExpandableAdapterItem {
    @BindView(R.id.group_recyclerview_item_group_name_textView)
    TextView group_recyclerview_item_group_name_textView;
    @BindView(R.id.group_recyclerview_item_group_setting_imageView)
    ImageView group_recyclerview_item_group_setting_imageView;
    @BindView(R.id.group_recyclerview_item_group_arrow_imageView)
    ImageView group_recyclerview_item_group_arrow_imageView;

    private AppGroup mAppGroup;
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
        root.setOnClickListener(view -> {
            doExpandOrUnexpand();
        });
        _rxbus = RxBus.getInstance();
        group_recyclerview_item_group_setting_imageView.setOnClickListener(view -> showDialog(root));
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
        mAppGroup = (AppGroup) model;
        String title;
        title = mAppGroup.getName() + " (" + mAppGroup.getChildItemList().size() + ")";
        group_recyclerview_item_group_name_textView.setText(title);
    }

    public void showDialog(final View root) {
        MaterialDialog.Builder settingDialog = new MaterialDialog.Builder(root.getContext());
        settingDialog.title("그룹 관리")
                .items(R.array.group_settings)
                .itemsCallback((dialog, itemView, position, text) -> {
                    String selectedItem = text.toString();
                    switch (selectedItem) {
                        case "그룹원 관리":
                            Intent intent = new Intent(root.getContext(), ModifyGroupMemberActivity.class);
                            intent.putExtra("groupInfo", mAppGroup);
                            root.getContext().startActivity(intent);
                            return;
                        case "그룹명 수정":
                            MaterialDialog.Builder modifyGroupDialog = new MaterialDialog.Builder(root.getContext());
                            modifyGroupDialog.title("그룹명 수정")
                                    .inputType(InputType.TYPE_CLASS_TEXT)
                                    .input(null, mAppGroup.getName(), (dialog1, input) ->
                                    {
                                        if (input.length() == 0) {
                                            Toast.makeText(root.getContext(), "최소 1글자 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ModifyGroupName modifyGroupName = new ModifyGroupName();
                                            modifyGroupName.setName(input.toString());
                                            modifyGroupName.setGroup(mAppGroup.getId());
                                            _rxbus.send(modifyGroupName);
                                            mAppGroup.setName(input.toString());
                                            String title = mAppGroup.getName() + " (" + mAppGroup.getChildItemList().size() + ")";
                                            group_recyclerview_item_group_name_textView.setText(title);
                                        }
                                    })
                                    .show();
                            return;
                        case "그룹 삭제":
                            MaterialDialog.Builder deleteGroupDialog = new MaterialDialog.Builder(root.getContext());
                            deleteGroupDialog.title("그룹을 삭제하시겠습니까?")
                                    .onPositive((dialog1, which1) -> {
                                        if (getExpandableListItem().isExpanded()) {
                                            doExpandOrUnexpand();
                                        }
                                        if (_rxbus.hasObservers()) {
                                            RemoveGroup removeGroup = new RemoveGroup();
                                            removeGroup.setItemIndex(getItemIndex());
                                            removeGroup.setRemoveGroupId(mAppGroup.getId());
                                            _rxbus.send(removeGroup);
                                        }
                                    })
                                    .negativeText("취소")
                                    .positiveText("삭제")
                                    .show();
                    }
                })
                .show();
    }

    public class RemoveGroup {
        int itemIndex;
        int removeGroupId;

        public int getItemIndex() {
            return itemIndex;
        }

        public void setItemIndex(int itemIndex) {
            this.itemIndex = itemIndex;
        }

        public int getRemoveGroupId() {
            return removeGroupId;
        }

        public void setRemoveGroupId(int removeGroupId) {
            this.removeGroupId = removeGroupId;
        }
    }

    public class ModifyGroupName {
        @SerializedName("_group")
        @Expose
        private Integer group;
        @SerializedName("_name")
        @Expose
        private String name;

        /**
         * @return The group
         */
        public Integer getGroup() {
            return group;
        }

        /**
         * @param group The _group
         */
        public void setGroup(Integer group) {
            this.group = group;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The _name
         */
        public void setName(String name) {
            this.name = name;
        }
    }
}
