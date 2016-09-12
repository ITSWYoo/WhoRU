package com.yoo.ymh.whoru.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.view.activity.DetailContactActivity;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yoo on 2016-08-17.
 */
public class GroupMemberItem extends AbstractAdapterItem {
    @BindView(R.id.contactFragment_recyclerview_circleImageView)
    CircleImageView contactFragment_recyclerview_circleImageView;
    @BindView(R.id.contactFragment_recyclerview_name_textview)
    TextView contactFragment_recyclerview_name_textview;
    @BindView(R.id.contactFragment_recyclerview_memo_textview)
    TextView contactFragment_recyclerview_memo_textview;

    private Context mContext;
    private AppContact appContact;

    @Override
    public int getLayoutResId() {
        return R.layout.contact_recyclerview_item;
    }

    @Override
    public void onBindViews(View root) {
        ButterKnife.bind(this, root);
        mContext = root.getContext();
        root.setOnClickListener(view -> {
            Intent intent = new Intent(root.getContext(), DetailContactActivity.class);
            intent.putExtra("contactId", appContact.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public void onSetViews() {
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        if (model instanceof AppContact) {
            appContact = (AppContact) model;
            if (appContact.getProfileThumbnail() == null)
                contactFragment_recyclerview_circleImageView.setImageResource(R.drawable.ic_regit_user);
            else
                Glide.with(mContext).load(appContact.getProfileThumbnail()).into(contactFragment_recyclerview_circleImageView);
            contactFragment_recyclerview_name_textview.setText(appContact.getName());
            contactFragment_recyclerview_memo_textview.setText(appContact.getPhone());
        }
    }
}
