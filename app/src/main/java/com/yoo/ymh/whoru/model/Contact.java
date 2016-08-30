package com.yoo.ymh.whoru.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Yoo on 2016-08-14.
 */
public class Contact implements Parcelable {
    private int profile_img;
    private String memo;
    private String id;
    private String email;
    private String phone;
    private String address;
    private String name;
    private String company;
    private boolean selected;
    public Contact()
    {}
    public Contact(Parcel in) {
        profile_img = in.readInt();
        memo = in.readString();
        id = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        name = in.readString();
        company = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public boolean isSelected()
    {
        return selected;
    }
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    public int getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(int profile_img) {
        this.profile_img = profile_img;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(profile_img);
        parcel.writeString(memo);
        parcel.writeString(id);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeString(name);
        parcel.writeString(company);
    }


}
