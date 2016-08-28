package com.yoo.ymh.whoru.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.MenuBuilder;

import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yoo.ymh.whoru.adapter.MainViewPagerAdapter;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.view.fragment.AlarmFragment;
import com.yoo.ymh.whoru.view.fragment.ContactFragment;
import com.yoo.ymh.whoru.view.fragment.GroupFragment;
import com.yoo.ymh.whoru.view.fragment.InfoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private RxBus _rxBus = null;

    @BindView(R.id.mainActivity_toolbar)
    Toolbar mainActivity_toolbar;
    @BindView(R.id.mainActivity_fab)
    FloatingActionButton mainActivity_fab;
    @OnClick(R.id.mainActivity_fab)
    void clickFab(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @BindView(R.id.mainActivity_drawer_layout)
    DrawerLayout mainActivity_drawerLayout;
    @BindView(R.id.mainActivity_navigationView)
    NavigationView mainActivity_navigationView;
    @BindView(R.id.mainActivity_tab)
    TabLayout mainActivity_tabLayout;
    @BindView(R.id.mainActivity_viewpager)
    ViewPager mainActivity_viewPager;

    private MainViewPagerAdapter mainViewPagerAdapter;

    private ArrayList<com.github.tamir7.contacts.Contact> contacts;

    private ArrayList<com.yoo.ymh.whoru.model.Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _rxBus = RxBus.getInstance();

        getPermission();

        ButterKnife.bind(this);

        initView();
    }

    @Override
    public void onBackPressed() {
        if (mainActivity_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainActivity_drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mainActivity_drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), this);
        mainViewPagerAdapter.addFragment(new ContactFragment().newInstance(contactList));
        mainViewPagerAdapter.addFragment(new GroupFragment().newInstance());
        mainViewPagerAdapter.addFragment(new AlarmFragment().newInstance());
        mainViewPagerAdapter.addFragment(new InfoFragment().newInstance());
        viewPager.setAdapter(mainViewPagerAdapter);
        mainActivity_tabLayout.setupWithViewPager(viewPager);
    }

    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            getContactList();
            int tabImge[] = {R.drawable.ic_contacts_black_48dp, R.drawable.ic_group_black_48dp, R.drawable.ic_notifications_black_48dp, R.drawable.ic_face_black_48dp};
            if (mainActivity_viewPager != null) {
                setupViewPager(mainActivity_viewPager);
            }
            for (int i = 0; i < tabImge.length; i++) {
                mainActivity_tabLayout.getTabAt(i).setIcon(tabImge[i]);
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void getPermission() {
        TedPermission tedPermission = new TedPermission(getApplicationContext());
        tedPermission
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .check();
    }

    public void getContactList() {
        Contacts.initialize(MainActivity.this);
        contacts = (ArrayList<Contact>) Contacts.getQuery().find();
        contactList = new ArrayList<>();
        com.yoo.ymh.whoru.model.Contact item;

        for (int i = 0; i < contacts.size(); i++) {
            item = new com.yoo.ymh.whoru.model.Contact();
            if (contacts.get(i).getBestPhoneNumber() != null) {
                item.setName(contacts.get(i).getBestDisplayName());
                item.setPhone(contacts.get(i).getBestPhoneNumber().getNormalizedNumber());
                contactList.add(item);
            }
        }
    }

    private void initView() {
        mainActivity_toolbar.setTitle("Contact");
        setSupportActionBar(mainActivity_toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainActivity_drawerLayout, mainActivity_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

        };

        mainActivity_drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mainActivity_navigationView.setNavigationItemSelectedListener(this);
        mainActivity_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                switch (i) {
                    case 0:
                        mainActivity_toolbar.setTitle("Contact");
                        mainActivity_fab.setImageResource(R.drawable.ic_person_add_white_48dp);
                        mainActivity_fab.setVisibility(View.VISIBLE);
                        mainActivity_fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Toast.makeText(getApplicationContext(), "contact", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, ContactAddActivity.class);
                                startActivity(intent);
                            }
                        });

                        return;
                    case 1:
                        mainActivity_toolbar.setTitle("Group");
                        mainActivity_fab.setImageResource(R.drawable.ic_group_add_white_48dp);
                        mainActivity_fab.setVisibility(View.VISIBLE);
                        mainActivity_fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Group", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, GroupAddActivity.class);
                                startActivity(intent);
                            }
                        });

                        return;
                    case 2:
                        mainActivity_toolbar.setTitle("Alarms");
                        mainActivity_fab.setVisibility(View.INVISIBLE);
                        return;
                    case 3:
                        mainActivity_toolbar.setTitle("My Infomation");
                        mainActivity_fab.setVisibility(View.INVISIBLE);
                        return;
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

    public static class checkedEvent {
    }
}
