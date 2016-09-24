package com.yoo.ymh.whoru.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.AppAlarm;
import com.yoo.ymh.whoru.model.AppContact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yoo on 2016-09-21.
 */

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<AppAlarm> items;

    public List<AppAlarm> getItems() {
        return items;
    }

    //초기화
    public AlarmRecyclerViewAdapter(Context mContext, List<AppAlarm> items) {
        this.mContext = mContext;
        this.items = items;
    }

    //add item
    public void addItem(AppAlarm item) {
        items.add(item);
        int size = items.size();
        notifyItemInserted(size - 1);
    }

    public void addAllItem(List<AppAlarm> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<AppAlarm> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public AlarmRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_recyclerview_item, parent, false);
        return new AlarmRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.item = items.get(holder.getAdapterPosition());

        if (holder.item.getNameN() != null && holder.item.getNameN().length() > 0 && !holder.item.getName().equals(holder.item.getNameN())) {
            holder.alarm_recyclerview_layout_name.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldName.setText(holder.item.getName());
            holder.alarm_recyclerview_textview_newName.setText(holder.item.getNameN());
        } else holder.alarm_recyclerview_layout_name.setVisibility(View.GONE);
        if (holder.item.getResponsibilityN() != null && holder.item.getResponsibilityN().length() > 0 && !holder.item.getResponsibility().equals(holder.item.getResponsibilityN())) {
            holder.alarm_recyclerview_layout_responsibility.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldResponsibility.setText(holder.item.getResponsibility());
            holder.alarm_recyclerview_textview_newResponsibility.setText(holder.item.getResponsibilityN());
        } else holder.alarm_recyclerview_layout_responsibility.setVisibility(View.GONE);
        if (holder.item.getDepartmentN() != null && holder.item.getDepartmentN().length() > 0 && !holder.item.getDepartment().equals(holder.item.getDepartmentN())) {
            holder.alarm_recyclerview_layout_department.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldDepartment.setText(holder.item.getDepartment());
            holder.alarm_recyclerview_textview_newDepartment.setText(holder.item.getDepartmentN());
        } else holder.alarm_recyclerview_layout_department.setVisibility(View.GONE);
        if (holder.item.getCompanyN() != null && holder.item.getCompanyN().length() > 0 && !holder.item.getCompany().equals(holder.item.getCompanyN())) {
            holder.alarm_recyclerview_layout_company.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldCompany.setText(holder.item.getCompany());
            holder.alarm_recyclerview_textview_newCompany.setText(holder.item.getCompanyN());
        } else holder.alarm_recyclerview_layout_company.setVisibility(View.GONE);
        if (holder.item.getPhoneN() != null && holder.item.getPhoneN().length() > 0 && !holder.item.getPhone().equals(holder.item.getPhoneN())) {
            holder.alarm_recyclerview_layout_phone.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldPhone.setText(holder.item.getPhone());
            holder.alarm_recyclerview_textview_newPhone.setText(holder.item.getPhoneN());
        } else holder.alarm_recyclerview_layout_phone.setVisibility(View.GONE);
        if (holder.item.getCompanyPhoneN() != null && holder.item.getCompanyPhoneN().length() > 0 && !holder.item.getCompanyPhone().equals(holder.item.getCompanyPhoneN())) {
            holder.alarm_recyclerview_layout_companyPhone.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldCompanyPhone.setText(holder.item.getCompanyPhone());
            holder.alarm_recyclerview_textview_newCompanyPhone.setText(holder.item.getCompanyPhoneN());
        } else holder.alarm_recyclerview_layout_companyPhone.setVisibility(View.GONE);
        if (holder.item.getFaxPhoneN() != null && holder.item.getFaxPhoneN().length() > 0 && !holder.item.getFaxPhone().equals(holder.item.getFaxPhoneN())) {
            holder.alarm_recyclerview_layout_fax.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldFax.setText(holder.item.getFaxPhone());
            holder.alarm_recyclerview_textview_newFax.setText(holder.item.getFaxPhoneN());
        } else holder.alarm_recyclerview_layout_fax.setVisibility(View.GONE);
        if (holder.item.getEmailN() != null && holder.item.getEmailN().length() > 0 && !holder.item.getEmail().equals(holder.item.getEmailN())) {
            holder.alarm_recyclerview_layout_email.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldEmail.setText(holder.item.getEmail());
            holder.alarm_recyclerview_textview_newEmail.setText(holder.item.getEmailN());
        } else holder.alarm_recyclerview_layout_email.setVisibility(View.GONE);
        if (holder.item.getCompanyAddressN() != null && holder.item.getCompanyAddressN().length() > 0 && !holder.item.getCompanyAddress().equals(holder.item.getCompanyAddressN())) {
            holder.alarm_recyclerview_layout_address.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldAddress.setText(holder.item.getCompanyAddress());
            holder.alarm_recyclerview_textview_newAddress.setText(holder.item.getCompanyAddressN());
        } else holder.alarm_recyclerview_layout_address.setVisibility(View.GONE);
        if (holder.item.getFacebookAddressN() != null && holder.item.getFacebookAddressN().length() > 0 && !holder.item.getFacebookAddress().equals(holder.item.getFacebookAddressN())) {
            holder.alarm_recyclerview_layout_facebook.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldFacebook.setText(holder.item.getFacebookAddress());
            holder.alarm_recyclerview_textview_newFacebook.setText(holder.item.getFacebookAddressN());
        } else holder.alarm_recyclerview_layout_facebook.setVisibility(View.GONE);
        if (holder.item.getGoogleAddressN() != null && holder.item.getGoogleAddressN().length() > 0 && !holder.item.getGoogleAddress().equals(holder.item.getGoogleAddressN())) {
            holder.alarm_recyclerview_layout_google.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldGoogle.setText(holder.item.getGoogleAddress());
            holder.alarm_recyclerview_textview_newGoogle.setText(holder.item.getGoogleAddressN());
        } else holder.alarm_recyclerview_layout_google.setVisibility(View.GONE);
        if (holder.item.getLinkedinAddressN() != null && holder.item.getLinkedinAddressN().length() > 0 && !holder.item.getLinkedinAddress().equals(holder.item.getLinkedinAddressN())) {
            holder.alarm_recyclerview_layout_linkedIn.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldLinkedIn.setText(holder.item.getLinkedinAddress());
            holder.alarm_recyclerview_textview_newLinkedIn.setText(holder.item.getLinkedinAddressN());
        } else holder.alarm_recyclerview_layout_linkedIn.setVisibility(View.GONE);
        if (holder.item.getInstagramAddressN() != null && holder.item.getInstagramAddressN().length() > 0 && !holder.item.getInstagramAddress().equals(holder.item.getInstagramAddressN())) {
            holder.alarm_recyclerview_layout_instagram.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldInstagram.setText(holder.item.getInstagramAddress());
            holder.alarm_recyclerview_textview_newInstagram.setText(holder.item.getInstagramAddressN());
        } else holder.alarm_recyclerview_layout_instagram.setVisibility(View.GONE);
        if (holder.item.getExtraAddressN() != null && holder.item.getExtraAddressN().length() > 0 && !holder.item.getExtraAddress().equals(holder.item.getExtraAddressN())) {
            holder.alarm_recyclerview_layout_extraAddress.setVisibility(View.VISIBLE);
            holder.alarm_recyclerview_textview_oldExtraAddress.setText(holder.item.getExtraAddress());
            holder.alarm_recyclerview_textview_newExtraAddress.setText(holder.item.getExtraAddressN());
        } else holder.alarm_recyclerview_layout_extraAddress.setVisibility(View.GONE);

        holder.alarm_recyclerview_textview_date.setText(holder.item.getDate());
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
        AppAlarm item;
        @BindView(R.id.alarm_recyclerview_circleImageView)
        CircleImageView alarm_recyclerview_circleImageView;
        @BindView(R.id.alarm_recyclerview_textview_name)
        TextView alarm_recyclerview_textview_name;
        @BindView(R.id.alarm_recyclerview_layout_name)
        LinearLayout alarm_recyclerview_layout_name;
        @BindView(R.id.alarm_recyclerview_textview_oldName)
        TextView alarm_recyclerview_textview_oldName;
        @BindView(R.id.alarm_recyclerview_textview_newName)
        TextView alarm_recyclerview_textview_newName;
        @BindView(R.id.alarm_recyclerview_layout_responsibility)
        LinearLayout alarm_recyclerview_layout_responsibility;
        @BindView(R.id.alarm_recyclerview_textview_oldResponsibility)
        TextView alarm_recyclerview_textview_oldResponsibility;
        @BindView(R.id.alarm_recyclerview_textview_newResponsibility)
        TextView alarm_recyclerview_textview_newResponsibility;
        @BindView(R.id.alarm_recyclerview_layout_department)
        LinearLayout alarm_recyclerview_layout_department;
        @BindView(R.id.alarm_recyclerview_textview_oldDepartment)
        TextView alarm_recyclerview_textview_oldDepartment;
        @BindView(R.id.alarm_recyclerview_textview_newDepartment)
        TextView alarm_recyclerview_textview_newDepartment;
        @BindView(R.id.alarm_recyclerview_layout_company)
        LinearLayout alarm_recyclerview_layout_company;
        @BindView(R.id.alarm_recyclerview_textview_oldCompany)
        TextView alarm_recyclerview_textview_oldCompany;
        @BindView(R.id.alarm_recyclerview_textview_newCompany)
        TextView alarm_recyclerview_textview_newCompany;
        @BindView(R.id.alarm_recyclerview_layout_phone)
        LinearLayout alarm_recyclerview_layout_phone;
        @BindView(R.id.alarm_recyclerview_textview_oldPhone)
        TextView alarm_recyclerview_textview_oldPhone;
        @BindView(R.id.alarm_recyclerview_textview_newPhone)
        TextView alarm_recyclerview_textview_newPhone;
        @BindView(R.id.alarm_recyclerview_layout_companyPhone)
        LinearLayout alarm_recyclerview_layout_companyPhone;
        @BindView(R.id.alarm_recyclerview_textview_oldCompanyPhone)
        TextView alarm_recyclerview_textview_oldCompanyPhone;
        @BindView(R.id.alarm_recyclerview_textview_newCompanyPhone)
        TextView alarm_recyclerview_textview_newCompanyPhone;
        @BindView(R.id.alarm_recyclerview_layout_fax)
        LinearLayout alarm_recyclerview_layout_fax;
        @BindView(R.id.alarm_recyclerview_textview_oldFax)
        TextView alarm_recyclerview_textview_oldFax;
        @BindView(R.id.alarm_recyclerview_textview_newFax)
        TextView alarm_recyclerview_textview_newFax;
        @BindView(R.id.alarm_recyclerview_layout_email)
        LinearLayout alarm_recyclerview_layout_email;
        @BindView(R.id.alarm_recyclerview_textview_oldEmail)
        TextView alarm_recyclerview_textview_oldEmail;
        @BindView(R.id.alarm_recyclerview_textview_newEmail)
        TextView alarm_recyclerview_textview_newEmail;
        @BindView(R.id.alarm_recyclerview_layout_address)
        LinearLayout alarm_recyclerview_layout_address;
        @BindView(R.id.alarm_recyclerview_textview_oldAddress)
        TextView alarm_recyclerview_textview_oldAddress;
        @BindView(R.id.alarm_recyclerview_textview_newAddress)
        TextView alarm_recyclerview_textview_newAddress;
        @BindView(R.id.alarm_recyclerview_layout_facebook)
        LinearLayout alarm_recyclerview_layout_facebook;
        @BindView(R.id.alarm_recyclerview_textview_oldFacebook)
        TextView alarm_recyclerview_textview_oldFacebook;
        @BindView(R.id.alarm_recyclerview_textview_newFacebook)
        TextView alarm_recyclerview_textview_newFacebook;
        @BindView(R.id.alarm_recyclerview_layout_google)
        LinearLayout alarm_recyclerview_layout_google;
        @BindView(R.id.alarm_recyclerview_textview_oldGoogle)
        TextView alarm_recyclerview_textview_oldGoogle;
        @BindView(R.id.alarm_recyclerview_textview_newGoogle)
        TextView alarm_recyclerview_textview_newGoogle;
        @BindView(R.id.alarm_recyclerview_layout_linkedIn)
        LinearLayout alarm_recyclerview_layout_linkedIn;
        @BindView(R.id.alarm_recyclerview_textview_oldLinkedIn)
        TextView alarm_recyclerview_textview_oldLinkedIn;
        @BindView(R.id.alarm_recyclerview_textview_newLinkedIn)
        TextView alarm_recyclerview_textview_newLinkedIn;
        @BindView(R.id.alarm_recyclerview_layout_instagram)
        LinearLayout alarm_recyclerview_layout_instagram;
        @BindView(R.id.alarm_recyclerview_textview_oldInstagram)
        TextView alarm_recyclerview_textview_oldInstagram;
        @BindView(R.id.alarm_recyclerview_textview_newInstagram)
        TextView alarm_recyclerview_textview_newInstagram;
        @BindView(R.id.alarm_recyclerview_layout_extraAddress)
        LinearLayout alarm_recyclerview_layout_extraAddress;
        @BindView(R.id.alarm_recyclerview_textview_oldExtraAddress)
        TextView alarm_recyclerview_textview_oldExtraAddress;
        @BindView(R.id.alarm_recyclerview_textview_newExtraAddress)
        TextView alarm_recyclerview_textview_newExtraAddress;
        @BindView(R.id.alarm_recyclerview_textview_date)
        TextView alarm_recyclerview_textview_date;


        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }
    }
}
