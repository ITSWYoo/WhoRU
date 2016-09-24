package com.yoo.ymh.whoru.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-08-14.
 */
public class AppGroup implements ExpandableListItem, Parcelable {
    @SerializedName("_id")
    @Expose
    private int id;
    @SerializedName("_name")
    @Expose
    private String name;

    @SerializedName("_list")
    @Expose
    private List<AppContact> mGroupMembers;
    private boolean mExpanded = false;
    private int groupMemberNum;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The _id
     */
    public void setId(Integer id) {
        this.id = id;
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


    /**
     * @return The mGroupMembers
     */
    public List<AppContact> getmGroupMembers() {
        return mGroupMembers;
    }

    /**
     * @param mGroupMembers The _list
     */
    public void setmGroupMembers(List<AppContact> mGroupMembers) {
        this.mGroupMembers = mGroupMembers;
    }

    public boolean ismExpanded() {
        return mExpanded;
    }

    public void setmExpanded(boolean mExpanded) {
        this.mExpanded = mExpanded;
    }

    public int getGroupMemberNum() {
        return groupMemberNum;
    }

    public void setGroupMemberNum(int groupMemberNum) {
        this.groupMemberNum = groupMemberNum;
    }

    public AppGroup() {
        this.mGroupMembers = new ArrayList<>();
    }

    public void addChildItem(AppContact addItem) {
        mGroupMembers.add(addItem);
        groupMemberNum = mGroupMembers.size();
    }

    public void addChildItem(ArrayList<AppContact> addItemList) {
        mGroupMembers.addAll(addItemList);
        groupMemberNum = mGroupMembers.size();
    }

    @Override
    public List<?> getChildItemList() {
        return mGroupMembers;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        mExpanded = isExpanded;
    }

    @Override
    public String toString() {
        return "AppGroup{" +
                "mExpanded=" + mExpanded +
                ", name='" + name + '\'' +
                ", mGroupMembers=" + mGroupMembers +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.mExpanded ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.mGroupMembers);
        dest.writeInt(this.groupMemberNum);
    }

    protected AppGroup(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.mExpanded = in.readByte() != 0;
        this.mGroupMembers = in.createTypedArrayList(AppContact.CREATOR);
        this.groupMemberNum = in.readInt();
    }

    public static final Parcelable.Creator<AppGroup> CREATOR = new Parcelable.Creator<AppGroup>() {
        @Override
        public AppGroup createFromParcel(Parcel source) {
            return new AppGroup(source);
        }

        @Override
        public AppGroup[] newArray(int size) {
            return new AppGroup[size];
        }
    };
}
