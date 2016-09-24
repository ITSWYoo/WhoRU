package com.yoo.ymh.whoru.view.activity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.yoo.ymh.whoru.adapter.MainViewPagerAdapter;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.fcm.MyFirebaseInstanceIDService;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.AppContactList;
import com.yoo.ymh.whoru.model.FcmToken;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.PreferenceUtil;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.util.WhoRUApplication;
import com.yoo.ymh.whoru.view.fragment.AlarmFragment;
import com.yoo.ymh.whoru.view.fragment.ContactFragment;
import com.yoo.ymh.whoru.view.fragment.GroupFragment;
import com.yoo.ymh.whoru.view.fragment.InfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.mainActivity_toolbar)
    Toolbar mainActivity_toolbar;
    @BindView(R.id.mainActivity_fab)
    FloatingActionButton mainActivity_fab;
    @BindView(R.id.mainActivity_imageView_card)
    ImageView mainActivity_imageView_card;
    @BindView(R.id.mainActivity_drawer_layout)
    DrawerLayout mainActivity_drawerLayout;
    @BindView(R.id.mainActivity_navigationView)
    NavigationView mainActivity_navigationView;
    @BindView(R.id.mainActivity_tab)
    TabLayout mainActivity_tabLayout;
    @BindView(R.id.mainActivity_viewpager)
    ViewPager mainActivity_viewPager;
    @BindView(R.id.mainActivity_textView_card_default)
    TextView mainActivity_textView_card_default;
    private View headerLayout;
    private CircleImageView mainActivity_imageView_header;
    private TextView mainActivity_textView_name;
    private RxBus _rxBus;
    private CompositeSubscription compositeSubscription;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private List<com.github.tamir7.contacts.Contact> myLocalContactList;
    private List<AppContact> appContactList;
    private List<AppContact> appContactsfromContactFragment;

    private int tabImge[] = {R.drawable.ic_contacts_black_48dp, R.drawable.ic_group_black_48dp, R.drawable.ic_notifications_black_48dp, R.drawable.ic_face_black_48dp};
    private final long FINISH_INTERVAL_TIME = 2000;
    public static final int REQ_CODE_OVERLAY_PERMISSION = 9999;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        FcmToken fcmToken = new FcmToken();
        fcmToken.setToken(token);
        rxBusEvent();
        getPermission();
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().sendFcmToken(WhoRUApplication.getSessionId(), fcmToken)
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                },Throwable::printStackTrace));
        initViews();
    }

    @Override
    public void onBackPressed() {
        if (mainActivity_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainActivity_drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long currentTime = System.currentTimeMillis();
            long intervalTitme = currentTime - backPressedTime;

            if (0 <= intervalTitme && FINISH_INTERVAL_TIME >= intervalTitme) {
                super.onBackPressed();
            } else {
                backPressedTime = currentTime;
                Toast.makeText(getApplicationContext(), "뒤로 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_sign_out) {
            // Handle the camera action
            new MaterialDialog.Builder(this)
                    .title("계정 탈퇴")
                    .content("탈퇴 하시면 모든 정보가 삭제됩니다.\n 그래도 탈퇴 하시겠습니까?")
                    .positiveText("네")
                    .onPositive((dialog, which) -> {
                        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().unregisterMyContact(WhoRUApplication.getSessionId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(s -> {
                                    if (PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regFacebookId() != null) {
                                        PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).putRedFacebookId(null);
                                        LoginManager.getInstance().logOut();
                                    }
                                    if (PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regKakaoId() != null) {
                                        PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).putRedKakaoId(null);
                                        UserManagement.requestLogout(new LogoutResponseCallback() {
                                            @Override
                                            public void onCompleteLogout() {
                                            }
                                        });
                                    }
                                    Intent intent = new Intent(MainActivity.this, SplahActivity.class);
                                    startActivity(intent);
                                    finish();
                                }));
                    })
                    .negativeText("아니오")
                    .show();
        } else if (id == R.id.nav_open_source_library) {
        } else if (id == R.id.nav_log_out) {
            new MaterialDialog.Builder(this)
                    .title("로그아웃")
                    .content("로그아웃 하시겠습니까?")
                    .positiveText("네")
                    .onPositive((dialog, which) -> {
                        if (PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regFacebookId() != null) {
                            PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).putRedFacebookId(null);
                            LoginManager.getInstance().logOut();
                        }
                        if (PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).regKakaoId() != null) {
                            UserManagement.requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    PreferenceUtil.instance(WhoRUApplication.getWhoruContext()).putRedKakaoId(null);
                                }
                            });
                        }
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .negativeText("아니오")
                    .show();
            //페이스북 로그인

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, ManageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_localContact) {
            new MaterialDialog.Builder(this)
                    .title("연락처 가져오기")
                    .content("연락처를 동기화 하시겠습니까?")
                    .positiveText("네")
                    .onPositive((dialog, which) -> {
                        getMyLocalContactList();
                        if (appContactList.size() == 0 || appContactList.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "이미 연락처가 동기화 되어있습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            AppContactList localAppContactList = new AppContactList();
                            localAppContactList.getData().addAll(appContactList);
                            WhoRURetrofit.getWhoRURetorfitInstance().sendLocalcontactList(WhoRUApplication.getSessionId(), localAppContactList)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(integer -> _rxBus.send(new LocalContactToContactFragment()));
                        }
                    }).negativeText("아니오")
                    .show();

        }
        mainActivity_drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), this);
        mainViewPagerAdapter.addFragment(new ContactFragment().newInstance());
        mainViewPagerAdapter.addFragment(new GroupFragment().newInstance());
        mainViewPagerAdapter.addFragment(new AlarmFragment().newInstance());
        mainViewPagerAdapter.addFragment(new InfoFragment().newInstance());
        viewPager.setAdapter(mainViewPagerAdapter);
        mainActivity_tabLayout.setupWithViewPager(viewPager);
    }

    public void getPermission() {
        TedPermission tedPermission = new TedPermission(getApplicationContext());
        tedPermission
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .check();
    }

    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if (mainActivity_viewPager != null) {
                mainActivity_viewPager.setOffscreenPageLimit(3);
                setupViewPager(mainActivity_viewPager);
                for (int i = 0; i < tabImge.length; i++) {
                    mainActivity_tabLayout.getTabAt(i).setIcon(tabImge[i]);
                }
            }
            startOverlayWindowService(getApplicationContext());
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void getMyLocalContactList() {
        Contacts.initialize(MainActivity.this);
        myLocalContactList = (ArrayList<Contact>) Contacts.getQuery().find();
        appContactList = new ArrayList<>();
        AppContact item = new AppContact();

        //휴대폰 연락처 앱 연락처로가져오기
        for (Contact c : myLocalContactList) {
            item = new AppContact();
            if (c.getBestPhoneNumber() != null) {
                item.setName((c.getBestDisplayName()));
                String phoneNumber = c.getBestPhoneNumber().getNormalizedNumber();
                String phone_number = null;
                if (phoneNumber.startsWith("+82")) {
                    if (phoneNumber.length() == 14) phoneNumber = phoneNumber.replace("+82", "");
                    else phoneNumber = phoneNumber.replace("+82", "0");

                }
                if (phoneNumber.startsWith("+082")) {
                    if (phoneNumber.length() == 15) phoneNumber = phoneNumber.replace("+082", "");
                    else phoneNumber = phoneNumber.replace("+082", "0");
                }

                if (phoneNumber.length() == 11) {
                    phone_number = String.format("%s-%s-%s", phoneNumber.substring(0, 3), phoneNumber.substring(3, 7), phoneNumber.substring(7, 11));
                }
                if (phoneNumber.length() == 10) {
                    phone_number = String.format("%s-%s-%s", phoneNumber.substring(0, 3), phoneNumber.substring(3, 6), phoneNumber.substring(6, 10));
                }
                if (phoneNumber.length() == 9) {
                    phone_number = String.format("%s-%s-%s", phoneNumber.substring(0, 2), phoneNumber.substring(2, 5), phoneNumber.substring(5, 9));
                }
                if (phone_number != null && phone_number.length() > 0) item.setPhone(phone_number);
                else item.setPhone(phoneNumber);
                appContactList.add(item);
            }
        }

        //가져온 연락처랑 기존에 등록된 연락처 비교
        if (appContactsfromContactFragment != null && !appContactsfromContactFragment.isEmpty() && appContactsfromContactFragment.size() != 0) {
            for (int i = appContactList.size() - 1; i >= 0; i--) {
                for (int j = 0; j < appContactsfromContactFragment.size(); j++) {
                    if (appContactList.get(i).getPhone().equals(appContactsfromContactFragment.get(j).getPhone())) {
                        appContactList.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void initViews() {
        setToolbar();
        setNavigation();
        setTablayout();
    }

    private void setToolbar() {
        mainActivity_toolbar.setTitle("Contact");
        setSupportActionBar(mainActivity_toolbar);
    }

    private void setNavigation() {

        headerLayout = mainActivity_navigationView.getHeaderView(0);

        mainActivity_imageView_header = (CircleImageView) headerLayout.findViewById(R.id.mainActivity_imageView_header);
        mainActivity_textView_name = (TextView) headerLayout.findViewById(R.id.mainActivity_textView_name);

        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getMyInfo(WhoRUApplication.getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appContact -> {
                    if (appContact.getProfileThumbnail() != null && appContact.getProfileThumbnail().length() > 0) {
                        Glide.with(MainActivity.this).load(appContact.getProfileThumbnail()).into(mainActivity_imageView_header);
                    }
                    mainActivity_textView_name.setText(appContact.getName());

                    if (appContact.getCardImageThumbnail() != null && appContact.getCardImageThumbnail().length() > 0) {
                        Glide.with(MainActivity.this).load(appContact.getCardImageThumbnail()).into(mainActivity_imageView_card);
                        mainActivity_textView_card_default.setVisibility(View.GONE);
                    }
                }));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainActivity_drawerLayout, mainActivity_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        mainActivity_drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mainActivity_navigationView.setNavigationItemSelectedListener(this);
    }

    private void setTablayout() {

        mainActivity_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                switch (i) {
                    case 0:
                        mainActivity_toolbar.setTitle("Contact");
                        mainActivity_fab.setImageResource(R.drawable.ic_person_add_white_48dp);
                        mainActivity_imageView_card.setVisibility(View.GONE);
                        mainActivity_fab.setVisibility(View.VISIBLE);
                        mainActivity_fab.setOnClickListener(view -> {
                            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                            intent.putParcelableArrayListExtra("myContactList", (ArrayList<? extends Parcelable>) appContactsfromContactFragment);
                            startActivity(intent);
                        });

                        mainActivity_textView_card_default.setVisibility(View.GONE);
                        break;
                    case 1:
                        mainActivity_toolbar.setTitle("AppGroup");
                        mainActivity_fab.setImageResource(R.drawable.ic_group_add_white_48dp);
                        mainActivity_imageView_card.setVisibility(View.GONE);
                        mainActivity_fab.setVisibility(View.VISIBLE);
                        mainActivity_fab.setOnClickListener(view -> {
                            Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
                            startActivity(intent);
                        });

                        mainActivity_textView_card_default.setVisibility(View.GONE);
                        break;
                    case 2:
                        mainActivity_toolbar.setTitle("Alarms");
                        mainActivity_imageView_card.setVisibility(View.GONE);
                        mainActivity_fab.setVisibility(View.INVISIBLE);
                        mainActivity_textView_card_default.setVisibility(View.GONE);
                        break;
                    case 3:
                        mainActivity_toolbar.setTitle("My Infomation");
                        mainActivity_imageView_card.setVisibility(View.VISIBLE);
                        mainActivity_fab.setVisibility(View.INVISIBLE);
                        mainActivity_textView_card_default.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void rxBusEvent() {
        _rxBus = RxBus.getInstance();
        compositeSubscription = new CompositeSubscription();
        ConnectableObservable<Object> mainActivityEmitter = _rxBus.toObserverable().publish();

        //publish는 보통 Observable을 ConnectableObservable로 변환
        //구독 여부와 상관없다. Connect라는 메서드가 불려야만 항목들을 배출하는 Observable.

        compositeSubscription//
                .add(mainActivityEmitter.subscribe(o -> {
                    if (o instanceof ContactFragment.AppContactToMain) {
                        appContactsfromContactFragment = new ArrayList<>();
                        appContactsfromContactFragment = ((ContactFragment.AppContactToMain) o).getAppContacts();
                    }
                }));
        compositeSubscription.add(mainActivityEmitter.connect());
        //이 시점부터 Observable이 배출. 위에 Subscribe가 먼저있다.. 고로 구독하고나서부터 배출을 할수밖에없다.->다 잡을수있다.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    public class LocalContactToContactFragment {
    }

    public void startOverlayWindowService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(context)) {
            onObtainingPermissionOverlayWindow();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onObtainingPermissionOverlayWindow() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_OVERLAY_PERMISSION:
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
