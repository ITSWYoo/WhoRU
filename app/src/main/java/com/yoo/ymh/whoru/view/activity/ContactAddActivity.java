package com.yoo.ymh.whoru.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.PersonNameValidator;
import com.andreabaccega.formedittextvalidator.PhoneValidator;
import com.andreabaccega.widget.FormEditText;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.Contact;
import com.yoo.ymh.whoru.util.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactAddActivity extends AppCompatActivity {

    private RxBus _rxBus = null;

    @BindView(R.id.contactAddActivity_editText_name) FormEditText contactAddActivity_editText_name;
    @BindView(R.id.contactAddActivity_editText_phone) FormEditText contactAddActivity_editText_phone;
    @BindView(R.id.contactAddActivity_toolbar) Toolbar contactAddActivity_toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        _rxBus = RxBus.getInstance();
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_done_menu,menu);
        return true;
    }

    public void initViews(){
        contactAddActivity_toolbar.setTitle("연락처 추가");
        setSupportActionBar(contactAddActivity_toolbar);
        contactAddActivity_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        contactAddActivity_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("hh","hh");
                onBackPressed();
            }
        });

        contactAddActivity_editText_name.addValidator(new PersonNameValidator("Error"));
        contactAddActivity_editText_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    if (contactAddActivity_editText_name.testValidity()) {
                        Toast.makeText(ContactAddActivity.this, "통과", Toast.LENGTH_SHORT).show();
                        contactAddActivity_editText_phone.requestFocus();
                    } else
                        Toast.makeText(ContactAddActivity.this, "탈락", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

        contactAddActivity_editText_phone.addValidator(new PhoneValidator(null));
        contactAddActivity_editText_phone.setImeOptions(EditorInfo.IME_ACTION_SEND);
        contactAddActivity_editText_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    if (contactAddActivity_editText_phone.testValidity()) {
                        Toast.makeText(ContactAddActivity.this, "통과", Toast.LENGTH_SHORT).show();
                        AddContact addContact = new AddContact();
                        addContact.getAddContact().setName(contactAddActivity_editText_name.getText().toString());
                        addContact.getAddContact().setPhone(contactAddActivity_editText_phone.getText().toString());
                        _rxBus.send(addContact);
                        finish();
                    } else
                        Toast.makeText(ContactAddActivity.this, "탈락", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
    public class AddContact{
        Contact addContact;

        public AddContact() {
            addContact = new Contact();
        }

        public Contact getAddContact() {
            return addContact;
        }

        public void setAddContact(Contact addContact) {
            this.addContact = addContact;
        }
    }
}
