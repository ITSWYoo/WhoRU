package com.yoo.ymh.whoru.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoo.ymh.whoru.adapter.GroupExpandableAdapter;
import com.yoo.ymh.whoru.view.activity.GroupAddActivity;
import com.yoo.ymh.whoru.view.activity.MainActivity;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.model.Contact;
import com.yoo.ymh.whoru.model.Group;
import com.yoo.ymh.whoru.model.GroupItem;
import com.yoo.ymh.whoru.model.GroupMemberItem;
import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {
    private final int ITEM_TYPE_GROUP = 1;
    private final int ITEM_TYPE_CONTACT = 2;

    private BaseExpandableAdapter mBaseExpandableAdapter;
    private List<Group> mGroupList;
    private RxBus _rxBus;
    private CompositeSubscription _subscriptions;

    @BindView(R.id.groupFragment_recyclerview)
    RecyclerView groupFragment_recyclerview;

    public static GroupFragment newInstance() {
        GroupFragment groupFragment = new GroupFragment();
        Bundle args = new Bundle();
        groupFragment.setArguments(args);

        return groupFragment;
    }

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, v);
        Log.e("GrFrg","onCreateView");
        return v;

    }

    public void initViews() {
        mBaseExpandableAdapter = new BaseExpandableAdapter(mGroupList) {
            @NonNull
            @Override
            public AbstractAdapterItem<Object> getItemView(Object type) {
                int itemType = (int) type;
                switch (itemType) {
                    case ITEM_TYPE_GROUP:
                        return new GroupItem();
                    case ITEM_TYPE_CONTACT:
                        return new GroupMemberItem();
                }
                return null;
            }

            @Override
            public Object getItemViewType(Object t) {
                if (t instanceof Group) {
                    return ITEM_TYPE_GROUP;
                } else if (t instanceof com.yoo.ymh.whoru.model.Contact)
                    return ITEM_TYPE_CONTACT;
                return -1;
            }
        };
        groupFragment_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        groupFragment_recyclerview.setAdapter(mBaseExpandableAdapter);
        Log.e("num", mBaseExpandableAdapter.getItemCount() + "");
        mBaseExpandableAdapter.setExpandCollapseListener(new BaseExpandableAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
            }

            @Override
            public void onListItemCollapsed(int position) {
            }
        });
    }

    private void initData() {
        mGroupList = new ArrayList<Group>();

//        Group baseGroup = new Group();
//        baseGroup.name = "기본";
//        mGroupList.add(baseGroup);
    }

    @NonNull
    private Group createGroup(String groupName) {
        Group firstGroup = new Group();
        firstGroup.name = groupName;
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Contact contact = new Contact();
            contact.setName("Asdf");
            contact.setPhone("1231234");
            contacts.add(contact);
        }
        firstGroup.mGroupMembers = contacts;
        firstGroup.groupMemberNum = contacts.size();
        return firstGroup;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.e("GrFrg","onCreateOptionsMenu");
    }

    //프래그먼트가 보였을때 콜백.
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser )
        {
            Log.e("GrFrg","resume");
            this.setHasOptionsMenu(true); //옆프래그먼트로 넘겼을떄 해제를 위해.
            initData();        //서버연결.
            initViews();

            _rxBus = RxBus.getInstance();
            _subscriptions = new CompositeSubscription();
            if (_rxBus.hasObservers()) {
                GroupList g = new GroupList();
                for (int i = 0; i < mGroupList.size(); i++) {
                    g.getGroupList().add(mGroupList.get(i).name);
                }
                _rxBus.send(g);
            }

            ConnectableObservable<Object> tapEventEmitter = _rxBus.toObserverable().publish();

            _subscriptions//
                    .add(tapEventEmitter.subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object event) {
                            if (event instanceof GroupAddActivity.AddGroup) {
                                mBaseExpandableAdapter.collapseAllParents();
                                if (mGroupList.size() == 0) {
                                    mGroupList.add(((GroupAddActivity.AddGroup) event).getAddItem());
                                    mBaseExpandableAdapter.notifyDataSetChanged();
                                } else {
                                    mBaseExpandableAdapter.addItem(((GroupAddActivity.AddGroup) event).getAddItem());
                                }
                                if (_rxBus.hasObservers()) {
                                    GroupList g = new GroupList();
                                    for (int i = 0; i < mGroupList.size(); i++) {
                                        g.getGroupList().add(mGroupList.get(i).name);
                                    }
                                    _rxBus.send(g);
                                    Log.e("send", "send");
                                }
                            } else if (event instanceof GroupItem.RemoveGroup) {
                                mBaseExpandableAdapter.collapseAllParents();
                                mBaseExpandableAdapter.removedItem(((GroupItem.RemoveGroup) event).getItemIndex());
                                if (_rxBus.hasObservers()) {
                                    GroupList g = new GroupList();
                                    for (int i = 0; i < mGroupList.size(); i++) {
                                        g.getGroupList().add(mGroupList.get(i).name);
                                    }
                                    _rxBus.send(g);
                                    Log.e("send", "send");
                                }
                            } else if (event instanceof ContactFragment.AddContactToGroup) {
                                ArrayList<Contact> contacts = ((ContactFragment.AddContactToGroup) event).getContacts();
                                ArrayList<Integer> indexList = ((ContactFragment.AddContactToGroup) event).getGroups();

                                Log.e("ff", contacts.size() + ":" + indexList.size());
                                for (int i = 0; i < indexList.size(); i++) {
                                    mBaseExpandableAdapter.collapseAllParents();
                                    mGroupList.get(indexList.get(i)).addChildItem(contacts);
                                }
                                mBaseExpandableAdapter.notifyDataSetChanged();
                            }
                        }
                    }));
            _subscriptions.add(tapEventEmitter.connect());
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        Log.e("GrFrg","onDestroyOptionMenu");
        _subscriptions.clear();
    }

    public class GroupList {
        ArrayList<String> groupList;

        public GroupList() {
            groupList = new ArrayList<>();
        }

        public ArrayList<String> getGroupList() {
            return groupList;
        }

        public void setGroupList(ArrayList<String> groupList) {
            this.groupList = groupList;
        }
    }
}
