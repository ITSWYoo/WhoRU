package com.yoo.ymh.whoru.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.Contact;

public class DetailContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        Intent intent = getIntent();
        Contact item = intent.getParcelableExtra("detailContact");
        Log.e("get",item.getName()+" "+item.getPhone());
    }
}
