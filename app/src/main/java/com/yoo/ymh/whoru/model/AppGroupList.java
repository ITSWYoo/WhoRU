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
public class AppGroupList implements Parcelable {
    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("data")
    @Expose
    private List<AppGroup> data = new ArrayList<AppGroup>();

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
    public List<AppGroup> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<AppGroup> data) {
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

    public AppGroupList() {
    }

    protected AppGroupList(Parcel in) {
        this.total = in.readInt();
        this.data = new ArrayList<>();
        in.readList(this.data, AppGroup.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppGroupList> CREATOR = new Parcelable.Creator<AppGroupList>() {
        @Override
        public AppGroupList createFromParcel(Parcel source) {
            return new AppGroupList(source);
        }

        @Override
        public AppGroupList[] newArray(int size) {
            return new AppGroupList[size];
        }
    };
}
