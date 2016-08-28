package com.yoo.ymh.whoru.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yoo.ymh.whoru.model.Group;
import com.yoo.ymh.whoru.view.activity.ContactAddActivity;
import com.yoo.ymh.whoru.view.activity.GroupAddActivity;
import com.yoo.ymh.whoru.view.activity.MainActivity;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.model.Contact;
import com.yoo.ymh.whoru.adapter.ContactRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    @BindView(R.id.contactFragment_recyclerview)
    RecyclerView contactFragment_recyclerview;

    @BindView(R.id.contactFragment_deleteLayout)
    RelativeLayout contactFragment_deleteLayout;

    @BindView(R.id.contactFragment_checkBox_all)
    CheckBox contactFragment_checkBox_all;

    @BindView(R.id.contactFragment_button_delete)
    Button contactFragment_button_delete;

    @BindView(R.id.contactFragment_button_addGroup)
    Button contactFragment_button_addGroup;

    private ArrayList<String> groupList;

    @OnClick(R.id.contactFragment_button_addGroup)
    public void onClickAddGroup() {

        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.mainActivity_viewpager);

        if (groupList.isEmpty() || groupList.size() == 0) {
            Toast.makeText(getContext(), "그룹이 없습니다. 그룹을 생성해주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), GroupAddActivity.class);
            getActivity().startActivity(intent);
        } else {
            new MaterialDialog.Builder(getContext())
                    .title("그룹 추가")
                    .items(groupList)
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            AddContactToGroup addContactToGroup = new AddContactToGroup();

                            for (int i = 0; i < which.length; i++) {
                                addContactToGroup.getGroups().add(which[i]);
                            }

                            addContactToGroup.getContacts().addAll(contactRecyclerViewAdapter.getCheckedItemList());

                            if (_rxBus.hasObservers()) {
                                _rxBus.send(addContactToGroup);
                            }
                            checked = false;
                            contactFragment_deleteLayout.setVisibility(View.GONE);
                            contactRecyclerViewAdapter.setChecked(false);

                            viewPager.setCurrentItem(1);

                            return true;
                        }
                    })
                    .positiveText("선택")
                    .show();
        }

    }

    private List<Contact> contactList;
    private RxBus _rxBus;
    private CompositeSubscription _subscriptions;
    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;
    private boolean checked;


    public static ContactFragment newInstance(ArrayList<Contact> contactList) {
        ContactFragment contactFragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("contact", contactList);
        contactFragment.setArguments(args);
        return contactFragment;
    }


    public ContactFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getContext(), "click search", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete:
                Log.e("checked", "gg");
                if (checked == true) {
                    checked = false;
                    contactFragment_deleteLayout.setVisibility(View.GONE);
                } else {
                    checked = true;
                    contactFragment_deleteLayout.setVisibility(View.VISIBLE);
                    contactFragment_checkBox_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (contactFragment_checkBox_all.isChecked()) {
                                contactRecyclerViewAdapter.checkedAllItem(true);
                            } else {
                                contactRecyclerViewAdapter.checkedAllItem(false);
                            }
                        }
                    });
                    contactFragment_button_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MaterialDialog.Builder deleteGroupDialog = new MaterialDialog.Builder(getContext());
                            deleteGroupDialog.title("그룹을 삭제하시겠습니까?")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            contactRecyclerViewAdapter.removeItem();
                                        }
                                    })
                                    .negativeText("취소")
                                    .positiveText("삭제")
                                    .show();
                        }
                    });
                }

                contactRecyclerViewAdapter.setChecked(checked);
                Log.e("delete", "gogo");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, v);
        setContactFragment_recyclerview();
        return v;

    }

    public void initData() {
        contactList = new ArrayList<>();
        contactList = getArguments().getParcelableArrayList("contact");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        _rxBus = RxBus.getInstance();
        _subscriptions = new CompositeSubscription();

        ConnectableObservable<Object> tapEventEmitter = _rxBus.toObserverable().publish();

        //publish는 보통 Observable을 ConnectableObservable로 변환
        //구독 여부와 상관없다. Connect라는 메서드가 불려야만 항목들을 배출하는 Observable.

        _subscriptions//
                .add(tapEventEmitter.subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event instanceof ContactAddActivity.AddContact) {
                            contactRecyclerViewAdapter.addItem(((ContactAddActivity.AddContact) event).getAddContact());

                        } else if (event instanceof GroupFragment.GroupList) {
                            groupList = ((GroupFragment.GroupList) event).getGroupList();
                            Log.e("take", "take");
                        }
                    }
                }));
        _subscriptions.add(tapEventEmitter.connect());
        //이 시점부터 Observable이 배출. 위에 Subscribe가 먼저있다.. 고로 구독하고나서부터 배출을 할수밖에없다.->다 잡을수있다.
    }

    public void setContactFragment_recyclerview() {
        checked = false;
        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        contactFragment_recyclerview.setLayoutManager(manager);
        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(getContext(), contactList, checked);
        contactFragment_recyclerview.setAdapter(contactRecyclerViewAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _subscriptions.clear();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            Log.e("contactFragment","resume");
        }
    }



    public class AddContactToGroup {
        ArrayList<Contact> contacts;
        ArrayList<Integer> groups;

        public AddContactToGroup() {
            contacts = new ArrayList<>();
            groups = new ArrayList<>();
        }

        public ArrayList<Contact> getContacts() {
            return contacts;
        }

        public void setContacts(ArrayList<Contact> contacts) {
            this.contacts = contacts;
        }

        public ArrayList<Integer> getGroups() {
            return groups;
        }

        public void setGroups(ArrayList<Integer> groups) {
            this.groups = groups;
        }
    }

}
