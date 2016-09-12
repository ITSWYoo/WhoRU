package com.yoo.ymh.whoru.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-09-08.
 */
public class GroupList implements Parcelable {
    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("data")
    @Expose
    private List<Group> data = new ArrayList<Group>();

    /**
     * @return The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return The data
     */
    public List<Group> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Group> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeList(this.data);
    }

    public GroupList() {
    }

    protected GroupList(Parcel in) {
        this.total = in.readInt();
        this.data = new ArrayList<>();
        in.readList(this.data, Group.class.getClassLoader());
    }

    public static final Parcelable.Creator<GroupList> CREATOR = new Parcelable.Creator<GroupList>() {
        @Override
        public GroupList createFromParcel(Parcel source) {
            return new GroupList(source);
        }

        @Override
        public GroupList[] newArray(int size) {
            return new GroupList[size];
        }
    };
}
