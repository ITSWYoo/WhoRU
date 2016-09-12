package com.yoo.ymh.whoru.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.GroupList;
import com.yoo.ymh.whoru.model.RemovedContactList;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.GeocodingTask;
import com.yoo.ymh.whoru.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DetailContactActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private CompositeSubscription compositeSubscription;
    private ArrayList<String> snsList;
    private ArrayList<AppContact> myContactList;
    private int geocoderRetryCount = 0;
    private int contactId;
    private RxBus _rxBus;
    @BindView(R.id.detailContactActivity_toolbar)
    Toolbar detailContactActivity_toolbar;

    @BindView(R.id.detailContactActivity_circleImageView_profile)
    ImageView detailContactActivity_circleImageView_profile;

    @BindView(R.id.detailContactActivity_imageView_card)
    ImageView detailContactActivity_imageView_card;

    @BindView(R.id.detailContactActivity_textView_name)
    TextView detailContactActivity_textView_name;

    @BindView(R.id.detailContactActivity_textView_company)
    TextView detailContactActivity_textView_company;

    @BindView(R.id.detailContactActivity_textView_department)
    TextView detailContactActivity_textView_department;

    @BindView(R.id.detailContactActivity_textView_responsibility)
    TextView detailContactActivity_textView_responsibility;

    @BindView(R.id.detailContactActivity_textView_memo)
    TextView detailContactActivity_textView_memo;

    @BindView(R.id.detailContactActivity_textView_group)
    TextView detailContactActivity_textView_group;

    @BindView(R.id.detailContactActivity_textView_phoneNumber)
    TextView detailContactActivity_textView_phoneNumber;

    @BindView(R.id.detailContactActivity_textView_companyNumber)
    TextView detailContactActivity_textView_companyNumber;
    @BindView(R.id.detailContactActivity_layout_companyNumber)
    LinearLayout detailContactActivity_layout_companyNumber;

    @BindView(R.id.detailContactActivity_textView_faxNumber)
    TextView detailContactActivity_textView_faxNumber;
    @BindView(R.id.detailContactActivity_layout_faxNumber)
    LinearLayout detailContactActivity_layout_faxNumber;

    @BindView(R.id.detailContactActivity_textView_email)
    TextView detailContactActivity_textView_email;
    @BindView(R.id.detailContactActivity_layout_email)
    LinearLayout detailContactActivity_layout_email;

    @BindView(R.id.detailContactActivity_textView_companyAddress)
    TextView detailContactActivity_textView_companyAddress;
    @BindView(R.id.detailContactActivity_layout_companyAddress)
    LinearLayout detailContactActivity_layout_companyAddress;

    @BindView(R.id.detailContactActivity_layout_sns)
    LinearLayout detailContactActivity_layout_sns;

    @BindView(R.id.detailContactActivity_textView_facebook)
    TextView detailContactActivity_textView_facebook;
    @BindView(R.id.detailContactActivity_layout_facebook)
    LinearLayout detailContactActivity_layout_facebook;

    @BindView(R.id.detailContactActivity_textView_google)
    TextView detailContactActivity_textView_google;
    @BindView(R.id.detailContactActivity_layout_google)
    LinearLayout detailContactActivity_layout_google;

    @BindView(R.id.detailContactActivity_textView_linkedIn)
    TextView detailContactActivity_textView_linkedIn;
    @BindView(R.id.detailContactActivity_layout_linkedIn)
    LinearLayout detailContactActivity_layout_linkedIn;

    @BindView(R.id.detailContactActivity_textView_instagram)
    TextView detailContactActivity_textView_instagram;
    @BindView(R.id.detailContactActivity_layout_instagram)
    LinearLayout detailContactActivity_layout_instagram;

    @BindView(R.id.detailContactActivity_textView_anotherSns)
    TextView detailContactActivity_textView_anotherSns;
    @BindView(R.id.detailContactActivity_layout_anotherSns)
    LinearLayout detailContactActivity_layout_anotherSns;

    @BindView(R.id.detailContactActivity_textView_card_default)
    TextView detailContactActivity_textView_card_default;

    @BindView(R.id.detailContactActivity_mapView)
    MapView mapView;

    @BindView(R.id.detailContactActivity_button_delete)
    Button detailContactActivity_button_delete;
    private RemovedContactList removedContactIdList;
    private Integer[] selectedGroup;
    private GroupList groupList = new GroupList();
    private List<String> groupList_name = new ArrayList<>();
    private ArrayList<Integer> selected_groupIdList = new ArrayList<>();
    private String selectedGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        _rxBus = RxBus.getInstance();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getLocalDetailContact("abcd", contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appContact -> loadView(appContact), Throwable::printStackTrace));
        loadGroupList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeSubscription.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_setting_menu, menu);
        menu.findItem(R.id.action_delete_card).setTitle(Html.fromHtml("<font color=red>삭제하기</font>"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_modify_contact:
                Intent intent = new Intent(DetailContactActivity.this, AddContactActivity.class);
                intent.putExtra("modifyContactId", contactId);
                intent.putExtra("myContactList", myContactList);
                startActivity(intent);
                break;
            case R.id.action_add_contact:
//                Log.e("addAppContact", "addAppContact");
//                Intent intent1 = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT);
//                intent1.putExtra(ContactsContract.Intents.Insert.NAME, detailContactActivity_textView_name.getText().toString());
//                intent1.putExtra(ContactsContract.Intents.Insert.PHONE, detailContactActivity_textView_phoneNumber.getText().toString());
//                intent1.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
//                intent1.setData(Uri.fromParts("tel", detailContactActivity_textView_phoneNumber.getText().toString(), null));
//                intent1.putExtra(ContactsContract.Intents.Insert.PHONE, detailContactActivity_textView_companyNumber.getText().toString());
//                intent1.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
//                intent1.setData(Uri.fromParts("tel", detailContactActivity_textView_companyNumber.getText().toString(), null));
//                intent1.putExtra(ContactsContract.Intents.Insert.EMAIL, detailContactActivity_textView_email.getText().toString());
//                intent1.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
//                intent1.setData(Uri.fromParts("mailto", detailContactActivity_textView_email.getText().toString(), null));
//                intent1.putExtra(ContactsContract.Intents.Insert.NOTES, detailContactActivity_textView_memo.getText().toString());
//                startActivity(intent1);
                break;
            case R.id.action_delete_card:
                Log.e("deletecard", "deletecard");
                // 명함 삭제.
                break;
        }
        return true;
    }

    public void loadView(AppContact appContact) {
        detailContactActivity_toolbar.setTitle(appContact.getName());
        if (appContact.getProfileThumbnail() != null) {
            Glide.with(getApplicationContext()).load(appContact.getProfileThumbnail()).into(detailContactActivity_circleImageView_profile);
        } else {
            detailContactActivity_circleImageView_profile.setImageResource(R.drawable.ic_regit_user);
        }
        if (appContact.getCardImage() != null) {
            detailContactActivity_textView_card_default.setVisibility(View.INVISIBLE);
            Glide.with(getApplicationContext()).load(appContact.getCardImageThumbnail()).into(detailContactActivity_imageView_card);
        }
        detailContactActivity_textView_name.setText(appContact.getName());
        if (appContact.getResponsibility() != null && appContact.getResponsibility().length() > 0) {
            detailContactActivity_textView_responsibility.setText(appContact.getResponsibility());
            detailContactActivity_textView_responsibility.setVisibility(View.VISIBLE);
        }
        if (appContact.getDepartment() != null && appContact.getDepartment().length() > 0) {
            detailContactActivity_textView_department.setText(appContact.getDepartment());
            detailContactActivity_textView_department.setVisibility(View.VISIBLE);
        }
        if (appContact.getCompany() != null && appContact.getCompany().length() > 0) {
            detailContactActivity_textView_company.setText(appContact.getCompany());
            detailContactActivity_textView_company.setVisibility(View.VISIBLE);
        }
        if (appContact.getMemo() != null && appContact.getMemo().length() > 0) {
            detailContactActivity_textView_memo.setText(appContact.getMemo());
        }
        detailContactActivity_textView_memo.setOnClickListener(view1 -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("메모남기기");

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    AppContact modifyMemo = new AppContact();
                    modifyMemo.setId(contactId);
                    modifyMemo.setMemo(value);
                    compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().modifyMemo("abcd", modifyMemo)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                detailContactActivity_textView_memo.setText(value);
                                appContact.setMemo(value);
                            })
                    );
                }
            });


            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

            alert.show();
        });
        detailContactActivity_textView_group.setOnClickListener(view1 -> {
            setGroupDialog();
        });
        if (appContact.getGroup().size() > 0) {
            String groupListStr = "";
            for (int i = 0; i < appContact.getGroup().size(); i++) {
                if (i == appContact.getGroup().size() - 1)
                    groupListStr += appContact.getGroup().get(i).getName();
                else groupListStr += appContact.getGroup().get(i).getName() + ", ";
            }
            detailContactActivity_textView_group.setText(groupListStr);
        } else detailContactActivity_textView_group.setText("그룹");

        detailContactActivity_textView_phoneNumber.setText(appContact.getPhone());
        detailContactActivity_textView_phoneNumber.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + detailContactActivity_textView_phoneNumber.getText()));
            startActivity(intent);
        });

        if (appContact.getEmail().length() > 0) {
            detailContactActivity_textView_email.setText(appContact.getEmail());
            detailContactActivity_layout_email.setVisibility(View.VISIBLE);
            detailContactActivity_textView_email.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + detailContactActivity_textView_email.getText()));
                startActivity(intent);
            });
        }
        if (appContact.getCompanyPhone().length() > 0) {
            detailContactActivity_textView_companyNumber.setText(appContact.getCompanyPhone());
            detailContactActivity_layout_companyNumber.setVisibility(View.VISIBLE);
            detailContactActivity_textView_companyNumber.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + detailContactActivity_textView_companyNumber.getText()));
                startActivity(intent);
            });
        }
        if (appContact.getFaxPhone().length() > 0) {
            detailContactActivity_textView_faxNumber.setText(appContact.getFaxPhone());
            detailContactActivity_layout_faxNumber.setVisibility(View.VISIBLE);
            detailContactActivity_textView_faxNumber.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + detailContactActivity_textView_faxNumber.getText()));
                startActivity(intent);
            });
        }
        if (appContact.getCompanyAddress().length() > 0) {
            detailContactActivity_layout_companyAddress.setVisibility(View.VISIBLE);
            detailContactActivity_textView_companyAddress.setText(appContact.getCompanyAddress());
            mapView.getMapAsync(this);
        }

        snsList = new ArrayList<>();
        if (appContact.getFacebookAddress().length() > 0) {
            snsList.add(appContact.getFacebookAddress());
            detailContactActivity_layout_facebook.setVisibility(View.VISIBLE);
            detailContactActivity_textView_facebook.setText(appContact.getFacebookAddress());
            detailContactActivity_textView_facebook.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (detailContactActivity_textView_facebook.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailContactActivity_textView_facebook.getText().toString()));
                } else url += detailContactActivity_textView_facebook.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
        if (appContact.getGoogleAddress().length() > 0) {
            snsList.add(appContact.getGoogleAddress());
            detailContactActivity_layout_google.setVisibility(View.VISIBLE);
            detailContactActivity_textView_google.setText(appContact.getGoogleAddress());
            detailContactActivity_textView_google.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (detailContactActivity_textView_google.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailContactActivity_textView_google.getText().toString()));
                } else url += detailContactActivity_textView_google.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
        if (appContact.getLinkedinAddress().length() > 0) {
            snsList.add(appContact.getLinkedinAddress());
            detailContactActivity_layout_linkedIn.setVisibility(View.VISIBLE);
            detailContactActivity_textView_linkedIn.setText(appContact.getLinkedinAddress());
            detailContactActivity_textView_linkedIn.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (detailContactActivity_textView_linkedIn.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailContactActivity_textView_linkedIn.getText().toString()));
                } else url += detailContactActivity_textView_linkedIn.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
        if (appContact.getInstagramAddress().length() > 0) {
            snsList.add(appContact.getInstagramAddress());
            detailContactActivity_layout_instagram.setVisibility(View.VISIBLE);
            detailContactActivity_textView_instagram.setText(appContact.getInstagramAddress());
            detailContactActivity_textView_instagram.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (detailContactActivity_textView_instagram.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailContactActivity_textView_instagram.getText().toString()));
                } else url += detailContactActivity_textView_instagram.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
        if (appContact.getExtraAddress().length() > 0) {
            snsList.add(appContact.getExtraAddress());
            detailContactActivity_layout_anotherSns.setVisibility(View.VISIBLE);
            detailContactActivity_textView_anotherSns.setText(appContact.getExtraAddress());
            detailContactActivity_textView_anotherSns.setOnClickListener(view -> {
                Intent intent;
                String url = "http://";
                if (detailContactActivity_textView_anotherSns.getText().toString().contains(url)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailContactActivity_textView_anotherSns.getText().toString()));
                } else url += detailContactActivity_textView_anotherSns.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
        if (snsList.size() > 0) {
            detailContactActivity_layout_sns.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setMapAddress();
    }

    public void initViews() {
        setToolbar();
        contactId = getIntent().getIntExtra("contactId", 0);
        myContactList = getIntent().getParcelableArrayListExtra("myContactList");
        detailContactActivity_button_delete.setOnClickListener(view -> {
            MaterialDialog.Builder deleteGroupDialog = new MaterialDialog.Builder(DetailContactActivity.this);
            deleteGroupDialog.title("연락처를 삭제하시겠습니까?")
                    .onPositive((dialog, which) -> {
                        removedContactIdList = new RemovedContactList();
                        removedContactIdList.getData().add(contactId);
                        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().deleteContact("abcd", removedContactIdList)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(s -> {
                                    finish();
                                    if (_rxBus.hasObservers())
                                        _rxBus.send(new DeleteContactSuccess());
                                }));
                    })
                    .negativeText("취소")
                    .positiveText("삭제")
                    .show();
        });
    }

    public void setToolbar() {
        setSupportActionBar(detailContactActivity_toolbar);
        detailContactActivity_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        detailContactActivity_toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    public void setMapAddress() {
        geocoderRetryCount = 0;
        GeocodingTask.GeocodingListener geocodingListener = new GeocodingTask.GeocodingListener() {
            @Override
            public void onSuccess(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions().position(latLng).title("회사"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mapView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorMessage) {
                if (geocoderRetryCount >= 3) {
                } else {
                    geocoderRetryCount++;
                    new GeocodingTask(DetailContactActivity.this, this).execute(detailContactActivity_textView_companyAddress.getText().toString());
                }
            }
        };
        new GeocodingTask(DetailContactActivity.this, geocodingListener).execute(detailContactActivity_textView_companyAddress.getText().toString());
    }

    public void setGroupDialog() {
        if (!detailContactActivity_textView_group.getText().toString().equals("그룹")) {
            String s[] = detailContactActivity_textView_group.getText().toString().split(",");
            for (int i = 0; i < s.length; i++) {
                s[i] = s[i].trim();
            }
            if (s.length > 0) {
                selectedGroup = new Integer[s.length];
                for (int j = 0; j < s.length; j++) {
                    for (int i = 0; i < groupList_name.size(); i++) {
                        if (s[j].equals(groupList_name.get(i))) {
                            selectedGroup[j] = Integer.valueOf(i);
                        }
                    }
                }
            }
        } else selectedGroup = null;

        new MaterialDialog.Builder(DetailContactActivity.this)
                .title("그룹을 선택해주세요")
                .items(groupList_name)
                .itemsCallbackMultiChoice(selectedGroup, (dialog, which, text) -> {
                    selectedGroupName = "";
                    selected_groupIdList = new ArrayList<>();
                    if (text.length > 0) {
                        for (int j = 0; j < text.length; j++) {
                            for (int i = 0; i < groupList.getTotal(); i++) {
                                if (text[j].toString().equals(groupList.getData().get(i).getName())) {
                                    selected_groupIdList.add(groupList.getData().get(i).getId());
                                    if (j < text.length - 1)
                                        selectedGroupName += groupList.getData().get(i).getName() + " , ";
                                    else
                                        selectedGroupName += groupList.getData().get(i).getName();
                                    break;
                                }
                            }
                            ModifyGroup modifyGroup = new ModifyGroup();
                            modifyGroup.setId(contactId);
                            modifyGroup.setGroup(selected_groupIdList);
                            compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().modifyGroup("abcd", modifyGroup)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(s -> {
                                        if (selectedGroupName.length() > 0)
                                            detailContactActivity_textView_group.setText(selectedGroupName);
                                        else detailContactActivity_textView_group.setText("그룹");
                                        _rxBus.send(new SuccessModifyGroup());
                                    }, Throwable::printStackTrace));
                        }
                    }
                    return true;
                })
                .positiveText("선택")
                .show();
    }

    public class DeleteContactSuccess {
    }

    public void loadGroupList() {
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getGroupList("abcd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_groupList -> {
                    groupList = _groupList;
                    groupList_name.clear();
                    for (int i = 0; i < _groupList.getTotal(); i++) {
                        groupList_name.add(_groupList.getData().get(i).getName());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(DetailContactActivity.this, "서버에 장애가 있습니다.", Toast.LENGTH_SHORT).show();
                }));
    }

    public class ModifyGroup {
        @SerializedName("_id")
        @Expose
        int id;
        @SerializedName("_group")
        @Expose
        List<Integer> modifyGroupList = new ArrayList<>();

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
         * @return The modifyGroupList
         */
        public List<Integer> getGroup() {
            return modifyGroupList;
        }

        /**
         * @param modifyGroupList The _group
         */
        public void setGroup(List<Integer> modifyGroupList) {
            this.modifyGroupList = modifyGroupList;
        }
    }

    public class SuccessModifyGroup {
    }
}

