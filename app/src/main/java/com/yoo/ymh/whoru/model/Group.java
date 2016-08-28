package com.yoo.ymh.whoru.model;

import android.util.Log;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo on 2016-08-14.
 */
public class Group implements ExpandableListItem {

    public boolean mExpanded = false;
    public String name;
    public int image;
    public List<Contact> mGroupMembers;
    public int groupMemberNum;

    public Group() {
        this.mGroupMembers = new ArrayList<>();
    }

    public void addChildItem(Contact addItem){
        mGroupMembers.add(addItem);
        groupMemberNum = mGroupMembers.size();
    }
    public void addChildItem(ArrayList<Contact> addItemList)
    {
        mGroupMembers.addAll(addItemList);
        groupMemberNum = mGroupMembers.size();
        Log.e("size",groupMemberNum+","+mGroupMembers.size()+","+addItemList.size());
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
        return "Group{" +
                "mExpanded=" + mExpanded +
                ", name='" + name + '\'' +
                ", mGroupMembers=" + mGroupMembers +
                '}';
    }
}
