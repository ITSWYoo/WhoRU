package com.yoo.ymh.whoru.model;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yoo on 2016-08-17.
 */
public class GroupMemberItem extends AbstractAdapterItem {
    @BindView(R.id.contactFragment_recyclerview_circleImageView) CircleImageView contactFragment_recyclerview_circleImageView;
    @BindView(R.id.contactFragment_recyclerview_name_textview) TextView contactFragment_recyclerview_name_textview;
    @BindView(R.id.contactFragment_recyclerview_memo_textview) TextView contactFragment_recyclerview_memo_textview;

    private RxBus _rxbus;


    @Override
    public int getLayoutResId() {
        return R.layout.contact_recyclerview_item;
    }

    @Override
    public void onBindViews(View root) {

        ButterKnife.bind(this,root);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),contactFragment_recyclerview_name_textview.getText(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        if (model instanceof Contact) {
            Contact contact = (Contact) model;
            if(contact.getProfile_img() ==0)
                contactFragment_recyclerview_circleImageView.setImageResource(R.drawable.ic_regit_user);
            else
            contactFragment_recyclerview_circleImageView.setImageResource(contact.getProfile_img());
            contactFragment_recyclerview_name_textview.setText(contact.getName());
            contactFragment_recyclerview_memo_textview.setText(contact.getPhone());

        }
    }
}
