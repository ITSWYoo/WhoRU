package com.yoo.ymh.whoru.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.GroupList;
import com.yoo.ymh.whoru.model.RemovedContactList;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.view.activity.AddContactActivity;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.adapter.ContactRecyclerViewAdapter;
import com.yoo.ymh.whoru.view.activity.DetailContactActivity;
import com.yoo.ymh.whoru.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;
/*
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.contactFragment_swipe_refresh_layout)
    SwipeRefreshLayout contactFragment_swipe_refresh_layout;

    @BindView(R.id.contactFragment_recyclerview)
    RecyclerView contactFragment_recyclerview;

    @BindView(R.id.fast_scroller)
    VerticalRecyclerViewFastScroller verticalRecyclerViewFastScroller;

    @BindView(R.id.contactFragment_manageLayout)
    RelativeLayout contactFragment_manageLayout;

    @BindView(R.id.contactFragment_checkBox_all)
    CheckBox contactFragment_checkBox_all;

    @BindView(R.id.contactFragment_button_delete)
    Button contactFragment_button_delete;

    private boolean isRefresh = true;
    private List<AppContact> appContactList;        //연락처 리스트
    private List<String> groupList_name;       //그룹 name만
    private GroupList groupList;                     //retrofit 으로 가져온 그룹리스트 갯수 id ,name
    private RxBus _rxBus;
    private CompositeSubscription compositeSubscription;
    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;
    private boolean checked;


    private RemovedContactList removedContactIdList;

    public static ContactFragment newInstance() {
        ContactFragment contactFragment = new ContactFragment();
        Bundle args = new Bundle();
        contactFragment.setArguments(args);
        return contactFragment;
    }

    public ContactFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_fragment_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Hint");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getContext(), "click search", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_manage:
                //관리 모드인지 아닌지 관리 layout 체크
                if (checked) {
                    checked = false;
                    contactFragment_manageLayout.setVisibility(View.GONE);
                } else {
                    checked = true;
                    contactFragment_manageLayout.setVisibility(View.VISIBLE);
                    contactFragment_checkBox_all.setOnCheckedChangeListener((compoundButton, b) -> {
                        if (contactFragment_checkBox_all.isChecked()) {
                            contactRecyclerViewAdapter.checkedAllItem(true);
                        } else {
                            contactRecyclerViewAdapter.checkedAllItem(false);
                        }
                    });
                    contactFragment_button_delete.setOnClickListener(view -> {
                        MaterialDialog.Builder deleteGroupDialog = new MaterialDialog.Builder(getContext());
                        deleteGroupDialog.title("연락처를 삭제하시겠습니까?")
                                .onPositive((dialog, which) -> {
                                    removedContactIdList = new RemovedContactList();
                                    if (contactRecyclerViewAdapter.getCheckedItemList().size() > 0) {
                                        for (AppContact removedContact : contactRecyclerViewAdapter.getCheckedItemList()) {
                                            removedContactIdList.getData().add(removedContact.getId());
                                        }
                                        removeContactList(removedContactIdList);
                                    }
                                })
                                .negativeText("취소")
                                .positiveText("삭제")
                                .show();
                    });
                }
                //관리모드인지 아닌지 itemview설정.
                contactRecyclerViewAdapter.setChecked(checked);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxBusEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, v);
        initViews();
        initData();
        return v;
    }

    public void initData() {
        contactFragment_swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        contactFragment_swipe_refresh_layout.setOnRefreshListener(this);
        contactFragment_swipe_refresh_layout.post(() -> {
            contactFragment_swipe_refresh_layout.setRefreshing(true);
            loadContactList();
            loadGroupList();
        });
    }

    public void initViews() {
        appContactList = new ArrayList<>();
        groupList = new GroupList();
        groupList_name = new ArrayList<>();
        checked = false;
        verticalRecyclerViewFastScroller.setRecyclerView(contactFragment_recyclerview);
        contactFragment_recyclerview.addOnScrollListener(verticalRecyclerViewFastScroller.getOnScrollListener());
        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(getContext(), appContactList, checked);
        contactFragment_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        contactFragment_recyclerview.setAdapter(contactRecyclerViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeSubscription.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<AppContact> filteredModelList = filter(appContactList, newText);
        contactRecyclerViewAdapter.setItems(filteredModelList);
        contactRecyclerViewAdapter.notifyDataSetChanged();
        contactFragment_recyclerview.scrollToPosition(0);
        return true;
    }

    private List<AppContact> filter(List<AppContact> datas, String newText) {
        newText = newText.toLowerCase();

        final List<AppContact> filteredModelList = new ArrayList<>();
        for (AppContact data : datas) {
            final String text = data.getName().toLowerCase();
            if (text.contains(newText)) {
                filteredModelList.add(data);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        loadContactList();
        loadGroupList();
    }

    public class AppContactToMain {
        private ArrayList<AppContact> appContacts;

        public AppContactToMain() {
            appContacts = new ArrayList<>();
        }

        public ArrayList<AppContact> getAppContacts() {
            return appContacts;
        }

        public void setAppContacts(ArrayList<AppContact> appContacts) {
            this.appContacts.addAll(appContacts);
        }
    }

    public void rxBusEvent() {
        _rxBus = RxBus.getInstance();
        compositeSubscription = new CompositeSubscription();

        ConnectableObservable<Object> contactFragmentEmitter = _rxBus.toObserverable().publish();

        //publish는 보통 Observable을 ConnectableObservable로 변환
        //구독 여부와 상관없다. Connect라는 메서드가 불려야만 항목들을 배출하는 Observable.

        compositeSubscription//
                .add(contactFragmentEmitter.subscribe(event -> {
                    if (event instanceof AddContactActivity.AddContactSuccess) {
                        loadContactList();
                    } else if (event instanceof MainActivity.LocalContactToContactFragment) {
                        loadContactList();
                    } else if (event instanceof DetailContactActivity.DeleteContactSuccess) {
                        loadContactList();
                    }
                }));
        compositeSubscription.add(contactFragmentEmitter.connect());
        //이 시점부터 Observable이 배출. 위에 Subscribe가 먼저있다.. 고로 구독하고나서부터 배출을 할수밖에없다.->다 잡을수있다.
    }

    public void showProgress() {
        if (!contactFragment_swipe_refresh_layout.isRefreshing()) {
            contactFragment_swipe_refresh_layout.setRefreshing(true);
        }
    }

    public void hideProgress() {
        if (contactFragment_swipe_refresh_layout.isRefreshing()) {
            contactFragment_swipe_refresh_layout.setRefreshing(false);
        }
    }

    public void loadContactList() {
        showProgress();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getAllContactList("abcd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appContactList1 -> {
                    hideProgress();
                    contactRecyclerViewAdapter.clear();
                    contactRecyclerViewAdapter.setItems(appContactList1.getData());
                    contactFragment_recyclerview.scrollToPosition(0);
                    if (_rxBus.hasObservers()) {
                        AppContactToMain appContactToMain = new AppContactToMain();
                        appContactToMain.setAppContacts((ArrayList<AppContact>) contactRecyclerViewAdapter.getItems());
                        _rxBus.send(appContactToMain);
                    }
                }, Throwable::printStackTrace));
    }

    public void loadGroupList() {
        showProgress();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getGroupList("abcd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_groupList -> {
                    hideProgress();
                    groupList = _groupList;
                    groupList_name.clear();
                    for (int i = 0; i < _groupList.getTotal(); i++) {
                        groupList_name.add(_groupList.getData().get(i).getName());
                    }
                }, Throwable::printStackTrace));
    }

    public void removeContactList(RemovedContactList removedContactIdList) {
        showProgress();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().deleteContact("abcd", removedContactIdList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    hideProgress();
                    contactRecyclerViewAdapter.removeItem();
                    if (_rxBus.hasObservers()) {
                        AppContactToMain appContactToMain = new AppContactToMain();
                        appContactToMain.setAppContacts((ArrayList<AppContact>) contactRecyclerViewAdapter.getItems());
                        _rxBus.send(appContactToMain);
                    }
                }, Throwable::printStackTrace));
    }
}

