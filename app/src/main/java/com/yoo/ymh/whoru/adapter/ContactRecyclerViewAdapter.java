package com.yoo.ymh.whoru.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yoo.ymh.whoru.view.activity.DetailContactActivity;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yoo on 2016-08-09.
 */
public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Contact> items;
    private boolean checked;
    private boolean[] isCheckedConfirm;

    //초기화
    public ContactRecyclerViewAdapter(Context mContext, List<Contact> items,boolean checeked) {
        this.mContext = mContext;
        this.items = items;
        this.checked = checeked;
        this.isCheckedConfirm = new boolean[items.size()];
    }

    //add item
    public void addItem(Contact item) {
        items.add(item);
        int size = items.size();
        notifyItemInserted(size-1);

    }

    public void addAllItem(List<Contact> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setItems(List<Contact> items) {
        this.items = items;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
        notifyDataSetChanged();
    }

    public void checkedAllItem(boolean isChecked)
    {
        int size = items.size();

        for(int i =0 ; i<size; i++)
        {
            items.get(i).setSelected(isChecked);
        }
        notifyDataSetChanged();
    }

    public List<Contact> getCheckedItemList(){
        int size = items.size();
        List<Contact> checkedItemList = new ArrayList<>();
        for(int i =0 ; i<size; i++)
        {
            if(items.get(i).isSelected())
            {
                checkedItemList.add(items.get(i));
            }
        }
        return checkedItemList;
    }

    public void removeItem()
    {
        int size = items.size();
        for(int i =size-1 ; i>=0; i--)
        {
            if(items.get(i).isSelected())
            {
                items.remove(i);
                notifyItemRemoved(i);
            }
        }

    }
    //itemView inflater
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setItemData(items.get(position));
        if(checked==true)
        {

            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(items.get(position).isSelected());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (items.get(position).isSelected()) {
                        items.get(position).setSelected(false);
                        holder.checkBox.setChecked(false);

                    }
                    else {
                        items.get(position).setSelected(true);
                        holder.checkBox.setChecked(true);
                    }
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    items.get(position).setSelected(b);
                }
            });
        }
        else if( checked ==false)
        {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.mView.getContext(), DetailContactActivity.class);
                    intent.putExtra("detailContact",holder.item);
                    mContext.startActivity(intent);
                }
            });
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(items==null)
        {
            return 0;
        }
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Contact item;
        @BindView(R.id.contactFragment_recyclerview_circleImageView)
        CircleImageView contactFragment_recyclerview_circleImageView;
        @BindView(R.id.contactFragment_recyclerview_name_textview)
        TextView contactFragment_recyclerview_name_textview;
        @BindView(R.id.contactFragment_recyclerview_memo_textview)
        TextView contactFragment_recyclerview_memo_textview;
        @BindView(R.id.checkBox)
        CheckBox checkBox;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }

        public void setItemData(Contact item) {
            this.item = item;
            if(item.getProfile_img()==0)
            {
                contactFragment_recyclerview_circleImageView.setImageResource(R.drawable.ic_regit_user);
            }
            else{
                contactFragment_recyclerview_circleImageView.setImageResource(item.getProfile_img());
            }
            contactFragment_recyclerview_name_textview.setText(item.getName());
            if(item.getPhone() !=null)
            contactFragment_recyclerview_memo_textview.setText(item.getPhone());
        }
    }
}
