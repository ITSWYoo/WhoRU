package com.yoo.ymh.whoru.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.view.activity.DetailContactActivity;
import com.yoo.ymh.whoru.R;

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
    private List<AppContact> items;
    private boolean checked;

    public List<AppContact> getItems() {
        return items;
    }

    //초기화
    public ContactRecyclerViewAdapter(Context mContext, List<AppContact> items, boolean checeked) {
        this.mContext = mContext;
        this.items = items;
        this.checked = checeked;
    }

    //add item
    public void addItem(AppContact item) {
        items.add(item);
        int size = items.size();
        notifyItemInserted(size - 1);
    }

    public void addAllItem(List<AppContact> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<AppContact> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        notifyDataSetChanged();
    }

    public void checkedAllItem(boolean isChecked) {
        for (AppContact c : items) {
            c.setSelected(isChecked);
        }
        notifyDataSetChanged();
    }

    public List<AppContact> getCheckedItemList() {
        List<AppContact> checkedItemList = new ArrayList<>();
        for (AppContact appContact : items) {
            if (appContact.isSelected()) {
                checkedItemList.add(appContact);
            }
        }
        return checkedItemList;
    }

    public void removeItem() {
        int size = items.size();
        for (int i = size - 1; i >= 0; i--) {
            if (items.get(i).isSelected()) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = items.get(holder.getAdapterPosition());


        if (holder.item.getProfileThumbnail() == null) {
            holder.contactFragment_recyclerview_circleImageView.setImageResource(R.drawable.ic_regit_user);
        } else
            Glide.with(mContext).load(holder.item.getProfileThumbnail()).into(holder.contactFragment_recyclerview_circleImageView);

        if(holder.item.getLinked() !=0) holder.contactFragment_recyclerview_linked.setVisibility(View.VISIBLE);
        else holder.contactFragment_recyclerview_linked.setVisibility(View.GONE);

        holder.contactFragment_recyclerview_name_textview.setText(holder.item.getName());
        holder.contactFragment_recyclerview_memo_textview.setText(holder.item.getPhone());
        if (checked) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(items.get(holder.getAdapterPosition()).isSelected());
            holder.mView.setOnClickListener(view -> {
                if (items.get(holder.getAdapterPosition()).isSelected()) {
                    items.get(holder.getAdapterPosition()).setSelected(false);
                    holder.checkBox.setChecked(false);
                } else {
                    items.get(position).setSelected(true);
                    holder.checkBox.setChecked(true);
                }
            });
            holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> items.get(position).setSelected(b));
        } else if (!checked) {
            holder.mView.setOnClickListener(view -> {
                Intent intent = new Intent(holder.mView.getContext(), DetailContactActivity.class);
                intent.putExtra("contactId", holder.item.getId());
                intent.putParcelableArrayListExtra("myContactList", (ArrayList<? extends Parcelable>) items);
                mContext.startActivity(intent);
            });
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        AppContact item;
        @BindView(R.id.contactFragment_recyclerview_circleImageView)
        CircleImageView contactFragment_recyclerview_circleImageView;
        @BindView(R.id.contactFragment_recyclerview_name_textview)
        TextView contactFragment_recyclerview_name_textview;
        @BindView(R.id.contactFragment_recyclerview_memo_textview)
        TextView contactFragment_recyclerview_memo_textview;
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        @BindView(R.id.contactFragment_recyclerview_linked)
        ImageView contactFragment_recyclerview_linked;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }
    }
}
