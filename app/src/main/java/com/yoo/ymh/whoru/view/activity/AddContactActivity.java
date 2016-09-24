package com.yoo.ymh.whoru.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yalantis.ucrop.UCrop;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.AppGroupList;
import com.yoo.ymh.whoru.model.RemovedAppContactList;
import com.yoo.ymh.whoru.retrofit.WhoRURetrofit;
import com.yoo.ymh.whoru.util.RxBus;
import com.yoo.ymh.whoru.util.WhoRUApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.addContactActivity_toolbar)
    Toolbar addContactActivity_toolbar;
    @BindView(R.id.addContactActivity_circleImageView_profile)
    CircleImageView addContactActivity_circleImageView_profile;
    @BindView(R.id.addContactActivity_imageView_card)
    ImageView addContactActivity_imageView_card;
    @BindView(R.id.addContactActivity_editText_name)
    EditText addContactActivity_editText_name;
    @BindView(R.id.addContactActivity_editText_company)
    EditText addContactActivity_editText_company;
    @BindView(R.id.addContactActivity_editText_department)
    EditText addContactActivity_editText_department;
    @BindView(R.id.addContactActivity_editText_responsibility)
    EditText addContactActivity_editText_responsibility;
    @BindView(R.id.addContactActivity_editText_memo)
    EditText addContactActivity_editText_memo;
    @BindView(R.id.addContactActivity_layout_memo)
    LinearLayout addContactActivity_layout_memo;
    @BindView(R.id.addContactActivity_textView_group)
    TextView addContactActivity_textView_group;
    @BindView(R.id.addContactActivity_layout_group)
    LinearLayout addContactActivity_layout_group;
    @BindView(R.id.addContactActivity_editText_phoneNumber)
    EditText addContactActivity_editText_phoneNumber;
    @BindView(R.id.addContactActivity_editText_companyNumber)
    EditText addContactActivity_editText_companyNumber;
    @BindView(R.id.addContactActivity_editText_faxNumber)
    EditText addContactActivity_editText_faxNumber;
    @BindView(R.id.addContactActivity_editText_email)
    EditText addContactActivity_editText_email;
    @BindView(R.id.addContactActivity_editText_companyAddress)
    EditText addContactActivity_editText_companyAddress;
    @BindView(R.id.addContactActivity_editText_facebook)
    EditText addContactActivity_editText_facebook;
    @BindView(R.id.addContactActivity_editText_google)
    EditText addContactActivity_editText_google;
    @BindView(R.id.addContactActivity_editText_linkedIn)
    EditText addContactActivity_editText_linkedIn;
    @BindView(R.id.addContactActivity_editText_instagram)
    EditText addContactActivity_editText_instagram;
    @BindView(R.id.addContactActivity_editText_anotherSns)
    EditText addContactActivity_editText_anotherSns;
    @BindView(R.id.addContactActivity_textView_card_default)
    TextView addContactActivity_editText_card_default;
    @BindView(R.id.addContactActivity_button_save)
    Button addContactActivity_button_save;

    private RxBus _rxBus;

    //사진 관련
    private final int PICK_FROM_CAMERA = 0;
    private final int REQUEST_SELECT_PICTURE = 0x01;
    private boolean cardImageClicked, cardBackImageCliked, profileImageClicked;
    private Uri mImageCaptureUri; //찍은거 임시저장

    private static final String CARD_CROPPED_IMAGE_NAME = "CardCropImage";
    private Uri cardResultUri; //임시저장 -> 크롭이미지
    private File cardCropFile;

    private static final String CARD_BACK_CROPPED_IMAGE_NAME = "CardBackCropImage";
    private Uri cardBackResultUri; //임시저장 -> 크롭이미지
    private File cardBackcropFile;

    private static final String PROFILE_CROPPED_IMAGE_NAME = "ProfileCropImage";
    private Uri profileResultUri; //임시저장 -> 크롭이미지
    private File profileCropFile;

    //sns 주소
    private String facebookAddress = "";
    private String googleAddress = "";
    private String linkedInAddress = "";
    private String instagramAddress = "";
    private static final String defaultSns[] = {"http://facebook.com/", "http://plus.google.com/", "http://kr.linkedin.com/in/", "http://instagram.com/"};

    //AppGroupList 와 그룹 이름만.
    private AppGroupList appGroupList = new AppGroupList();
    private List<String> groupList_name = new ArrayList<>();
    private ArrayList<Integer> selected_groupIdList = new ArrayList<>();
    private ArrayList<AppContact> contactListFromMain; //현재 저장된 연락처 -> 중복 저장을 피하기 위해
    private CompositeSubscription compositeSubscription;

    private MultipartBody.Part cardImage, card_backImage, profileImage;

    private boolean isDuplication;
    private int duplicationId;
    private AppContact addContact;
    private Map<String, RequestBody> map;
    private int modifyContactId;
    private AppContact modifyContact;
    private AppContact modifyMyContact;
    private Integer[] selectedGroup;
    private int modifyMyInfo;
    private boolean deleteProfileImage;
    private boolean deleteCardImage;
    private AppContact deleteImageContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        getPermission();
        compositeSubscription = new CompositeSubscription();
        contactListFromMain = new ArrayList<>();
        contactListFromMain = getIntent().getParcelableArrayListExtra("myContactList");
        modifyContactId = getIntent().getIntExtra("modifyContactId", 0);
        modifyMyInfo = getIntent().getIntExtra("modifyMyInfo", 0);
        _rxBus = RxBus.getInstance();
        deleteImageContact = new AppContact();

        //연락처추가
        if (modifyContactId == 0 && modifyMyInfo == 0) {
            initViews();
        }
        //연락처 수정
        else if (modifyContactId != 0) {
            //이미지 삭제 없을떄
            compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getLocalDetailContact(WhoRUApplication.getSessionId(), modifyContactId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(appContact -> {
                        modifyContact = appContact;
                        modifyContact.setId(modifyContactId);
                        initViews();
                    }, Throwable::printStackTrace));
        }
        //내 정보 수정
        else if (modifyMyInfo != 0) {
            compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getMyInfo(WhoRUApplication.getSessionId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(appContact -> {
                        modifyMyContact = appContact;
                        initViews();
                    }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_done_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroupList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_done:
                checkAddContact();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                try {
                    handleCropResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_FROM_CAMERA) {
                final Uri photoUri = mImageCaptureUri;
                startCropActivity(photoUri);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName;

        if (cardImageClicked) {
            destinationFileName = CARD_CROPPED_IMAGE_NAME + ".jpg";
        } else destinationFileName = PROFILE_CROPPED_IMAGE_NAME + ".jpg";


        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setActiveWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName))).withMaxResultSize(1000, 1000).withOptions(options);
        uCrop.start(this);
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(@NonNull Intent result) throws IOException {
        Bitmap cropImage = MediaStore.Images.Media.getBitmap(getContentResolver(), UCrop.getOutput(result)); //크롭 마친 최종 이미지 -> 서버로 전달 후 삭제.
        if (profileImageClicked) {
            profileResultUri = UCrop.getOutput(result);
            addContactActivity_circleImageView_profile.setImageResource(0);
            addContactActivity_circleImageView_profile.setImageBitmap(cropImage);
        }
        if (cardImageClicked) {
            cardResultUri = UCrop.getOutput(result);
            addContactActivity_imageView_card.setImageResource(0);
            addContactActivity_imageView_card.setImageBitmap(cropImage);
            addContactActivity_editText_card_default.setVisibility(View.GONE);
        }

        if (mImageCaptureUri != null) {  //이건 사진 찍엇을댸
            File f = new File(mImageCaptureUri.getPath());
            if (f.exists()) {
                f.delete();
            }
            mImageCaptureUri = null;
        }
    }

    private void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + "cache" + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void initViews() {
        setToolbar();

        if (modifyMyContact != null) {
            addContactActivity_layout_memo.setVisibility(View.GONE);
            addContactActivity_layout_group.setVisibility(View.GONE);
        } else {
            addContactActivity_textView_group.setOnClickListener(view -> {
                if (groupList_name.isEmpty() || groupList_name.size() == 0) {
                    Toast.makeText(AddContactActivity.this, "그룹이 없습니다. 그룹을 생성해주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddContactActivity.this, AddGroupActivity.class);
                    startActivity(intent);
                } else {
                    setGroupDialog();
                }
            });
        }

        addContactActivity_editText_phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        addContactActivity_editText_companyNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        addContactActivity_editText_faxNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if (modifyMyContact == null && modifyContact == null) {
            addContactActivity_toolbar.setTitle("연락처 추가");
        } else if (modifyMyContact != null) {
            addContactActivity_editText_name.setText(modifyMyContact.getName());
            addContactActivity_editText_responsibility.setText(modifyMyContact.getResponsibility());
            addContactActivity_editText_department.setText(modifyMyContact.getDepartment());
            addContactActivity_editText_company.setText(modifyMyContact.getCompany());
            if (modifyMyContact.getCardImageThumbnail() != null && modifyMyContact.getCardImageThumbnail().length() > 0) {
                Glide.with(AddContactActivity.this).load(modifyMyContact.getCardImageThumbnail()).into(addContactActivity_imageView_card);
                addContactActivity_editText_card_default.setVisibility(View.INVISIBLE);
            }
            if (modifyMyContact.getProfileThumbnail() != null && modifyMyContact.getProfileThumbnail().length() > 0) {
                Glide.with(AddContactActivity.this).load(modifyMyContact.getProfileThumbnail()).into(addContactActivity_circleImageView_profile);
            }
            addContactActivity_editText_memo.setText(modifyMyContact.getMemo());
            addContactActivity_editText_phoneNumber.setText(modifyMyContact.getPhone());
            addContactActivity_editText_companyNumber.setText(modifyMyContact.getCompanyPhone());
            addContactActivity_editText_faxNumber.setText(modifyMyContact.getFaxPhone());
            addContactActivity_editText_email.setText(modifyMyContact.getEmail());
            addContactActivity_editText_companyAddress.setText(modifyMyContact.getCompanyAddress());
            if (modifyMyContact.getFacebookAddress().length() > 0)
                addContactActivity_editText_facebook.setText(modifyMyContact.getFacebookAddress());
            if (modifyMyContact.getGoogleAddress().length() > 0)
                addContactActivity_editText_google.setText(modifyMyContact.getGoogleAddress());
            if (modifyMyContact.getLinkedinAddress().length() > 0)
                addContactActivity_editText_linkedIn.setText(modifyMyContact.getLinkedinAddress());
            if (modifyMyContact.getInstagramAddress().length() > 0)
                addContactActivity_editText_instagram.setText(modifyMyContact.getInstagramAddress());
            addContactActivity_editText_anotherSns.setText(modifyMyContact.getExtraAddress());
            addContactActivity_toolbar.setTitle("연락처 수정");
        } else if (modifyContact != null) {
            addContactActivity_editText_name.setText(modifyContact.getName());
            addContactActivity_editText_responsibility.setText(modifyContact.getResponsibility());
            addContactActivity_editText_department.setText(modifyContact.getDepartment());
            addContactActivity_editText_company.setText(modifyContact.getCompany());
            addContactActivity_editText_memo.setText(modifyContact.getMemo());
            if (modifyContact.getCardImageThumbnail() != null && modifyContact.getCardImageThumbnail().length() > 0) {
                Glide.with(AddContactActivity.this).load(modifyContact.getCardImageThumbnail()).into(addContactActivity_imageView_card);
                addContactActivity_editText_card_default.setVisibility(View.INVISIBLE);
            }
            if (modifyContact.getProfileThumbnail() != null && modifyContact.getProfileThumbnail().length() > 0) {
                Glide.with(AddContactActivity.this).load(modifyContact.getProfileThumbnail()).into(addContactActivity_circleImageView_profile);
            }
            if (modifyContact.getAppGroup().size() > 0) {
                String groupText = "";
                for (int i = 0; i < modifyContact.getAppGroup().size(); i++) {
                    if (i == modifyContact.getAppGroup().size() - 1) {
                        groupText += modifyContact.getAppGroup().get(i).getName();
                    } else groupText += modifyContact.getAppGroup().get(i).getName() + " , ";
                }
                addContactActivity_textView_group.setText(groupText);
            }
            addContactActivity_editText_phoneNumber.setText(modifyContact.getPhone());
            addContactActivity_editText_companyNumber.setText(modifyContact.getCompanyPhone());
            addContactActivity_editText_faxNumber.setText(modifyContact.getFaxPhone());
            addContactActivity_editText_email.setText(modifyContact.getEmail());
            addContactActivity_editText_companyAddress.setText(modifyContact.getCompanyAddress());
            if (modifyContact.getFacebookAddress().length() > 0)
                addContactActivity_editText_facebook.setText(modifyContact.getFacebookAddress());
            if (modifyContact.getGoogleAddress().length() > 0)
                addContactActivity_editText_google.setText(modifyContact.getGoogleAddress());
            if (modifyContact.getLinkedinAddress().length() > 0)
                addContactActivity_editText_linkedIn.setText(modifyContact.getLinkedinAddress());
            if (modifyContact.getInstagramAddress().length() > 0)
                addContactActivity_editText_instagram.setText(modifyContact.getInstagramAddress());
            addContactActivity_editText_anotherSns.setText(modifyContact.getExtraAddress());
            addContactActivity_toolbar.setTitle("연락처 수정");
        }

        editTextSetSelection(addContactActivity_editText_name);
        editTextSetSelection(addContactActivity_editText_responsibility);
        editTextSetSelection(addContactActivity_editText_department);
        editTextSetSelection(addContactActivity_editText_company);
        editTextSetSelection(addContactActivity_editText_memo);
        editTextSetSelection(addContactActivity_editText_phoneNumber);
        editTextSetSelection(addContactActivity_editText_companyNumber);
        editTextSetSelection(addContactActivity_editText_faxNumber);
        editTextSetSelection(addContactActivity_editText_email);
        editTextSetSelection(addContactActivity_editText_companyAddress);
        editTextSetSelection(addContactActivity_editText_facebook);
        editTextSetSelection(addContactActivity_editText_google);
        editTextSetSelection(addContactActivity_editText_linkedIn);
        editTextSetSelection(addContactActivity_editText_instagram);
        editTextSetSelection(addContactActivity_editText_anotherSns);
        addContactActivity_button_save.setOnClickListener(view -> checkAddContact());
        addContactActivity_circleImageView_profile.setOnClickListener(this);
        addContactActivity_imageView_card.setOnClickListener(this);
    }


    public void setToolbar() {
        addContactActivity_toolbar.setTitle("연락처 추가");
        setSupportActionBar(addContactActivity_toolbar);
        addContactActivity_toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        addContactActivity_toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addContactActivity_circleImageView_profile) {
            profileImageClicked = true;
            cardImageClicked = false;
        }
        if (view.getId() == R.id.addContactActivity_imageView_card) {
            cardImageClicked = true;
            profileImageClicked = false;
        }
        setCameraDialog();
    }


    public class AddContactSuccess {
    }

    public void editTextSetSelection(EditText editText) {
        editText.setSelection(editText.length());
    }


    public void checkAddContact() {
        if (addContactActivity_editText_phoneNumber.getText().toString().length() == 0 || addContactActivity_editText_name.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "필수정보(이름, 전화번호)를 입력해주세요", Toast.LENGTH_SHORT).show();
            if (addContactActivity_editText_phoneNumber.getText().toString().length() == 0)
                addContactActivity_editText_phoneNumber.requestFocus();
            if (addContactActivity_editText_name.getText().toString().length() == 0)
                addContactActivity_editText_name.requestFocus();
        } else {
            if (!addContactActivity_editText_facebook.getText().toString().equals(defaultSns[0]))
                facebookAddress = addContactActivity_editText_facebook.getText().toString();
            if (!addContactActivity_editText_google.getText().toString().equals(defaultSns[1]))
                googleAddress = addContactActivity_editText_google.getText().toString();
            if (!addContactActivity_editText_linkedIn.getText().toString().equals(defaultSns[2]))
                linkedInAddress = addContactActivity_editText_linkedIn.getText().toString();
            if (!addContactActivity_editText_instagram.getText().toString().equals(defaultSns[3]))
                instagramAddress = addContactActivity_editText_instagram.getText().toString();

            addContact = new AppContact();
            initAddContact(addContact);

            if (modifyContact == null && modifyMyContact == null) {
                for (AppContact contact : contactListFromMain) {
                    if (contact.getPhone().equals(addContactActivity_editText_phoneNumber.getText().toString())) {
                        Toast.makeText(AddContactActivity.this, "이미 저장한 연락처가 있습니다.", Toast.LENGTH_SHORT).show();
                        duplicationId = contact.getId();
                        isDuplication = true;
                        break;
                    }
                }
                if (isDuplication) {
                    duplicationContactDialog(duplicationId);
                } else {
                    postContact(addContact);
                }
            } else if (modifyContact != null) {
                addContact.setId(modifyContactId);
                postContact(addContact);
            } else if (modifyMyContact != null) {
                postContact(addContact);
            }
        }
    }

    public void initAddContact(AppContact addContact) {
        addContact.setName(addContactActivity_editText_name.getText().toString());
        addContact.setPhone(addContactActivity_editText_phoneNumber.getText().toString());
        addContact.setResponsibility(addContactActivity_editText_responsibility.getText().toString());
        addContact.setDepartment(addContactActivity_editText_department.getText().toString());
        addContact.setCompany(addContactActivity_editText_company.getText().toString());
        addContact.setMemo(addContactActivity_editText_memo.getText().toString());
        addContact.setCompanyPhone(addContactActivity_editText_companyNumber.getText().toString());
        addContact.setFaxPhone(addContactActivity_editText_faxNumber.getText().toString());
        addContact.setEmail(addContactActivity_editText_email.getText().toString());
        addContact.setCompanyAddress(addContactActivity_editText_companyAddress.getText().toString());
        addContact.setFacebookAddress(facebookAddress);
        addContact.setGoogleAddress(googleAddress);
        addContact.setLinkedinAddress(linkedInAddress);
        addContact.setInstagramAddress(instagramAddress);
        addContact.setExtraAddress(addContactActivity_editText_anotherSns.getText().toString());
    }

    public void loadGroupList() {
        compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().getGroupList(WhoRUApplication.getSessionId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_groupList -> {
                    appGroupList = _groupList;
                    groupList_name.clear();
                    for (int i = 0; i < _groupList.getTotal(); i++) {
                        groupList_name.add(_groupList.getData().get(i).getName());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(AddContactActivity.this, "서버에 장애가 있습니다.", Toast.LENGTH_SHORT).show();
                }));
    }

    public void postContact(AppContact addContact) {
        if (modifyContact == null && modifyMyContact == null) {
            if (addContact.getId() == 0) {
                setRequestBody(addContact);
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().addOrModifyContact(WhoRUApplication.getSessionId(), selected_groupIdList, map, cardImage, card_backImage, profileImage)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            if (s.equals("1")) {
                                if (_rxBus.hasObservers()) _rxBus.send(new AddContactSuccess());
                            } else
                                Toast.makeText(AddContactActivity.this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }, Throwable::printStackTrace, () -> {
                            removeCachePhoto();
                            finish();
                        }));
            }
            //연락처 추가시 겹치는 번호 있을시 덮어쓰기
            else {
                RemovedAppContactList removedAppContactList = new RemovedAppContactList();
                removedAppContactList.getData().add(addContact.getId());
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().deleteContact(WhoRUApplication.getSessionId(), removedAppContactList)
                        .flatMap(s -> {
                            addContact.setId(0);
                            setRequestBody(addContact);
                            return WhoRURetrofit.getWhoRURetorfitInstance().addOrModifyContact(WhoRUApplication.getSessionId(), selected_groupIdList, map, cardImage, card_backImage, profileImage);
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s1 -> {
                            if (s1.equals("1")) {
                                if (_rxBus.hasObservers()) _rxBus.send(new AddContactSuccess());
                            } else
                                Toast.makeText(AddContactActivity.this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        },Throwable::printStackTrace,() -> {
                            removeCachePhoto();
                            finish();
                        }));
            }
        }
        //연락처 수정
        else if (modifyContact != null) {
            addContact.setId(modifyContact.getId());
            setRequestBody(addContact);
            if (!deleteProfileImage && !deleteCardImage) {
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().addOrModifyContact(WhoRUApplication.getSessionId(), selected_groupIdList, map, cardImage, card_backImage, profileImage)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            if (s.equals("1")) {
                                if (_rxBus.hasObservers()) _rxBus.send(new AddContactSuccess());
                            } else
                                Toast.makeText(AddContactActivity.this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }, Throwable::printStackTrace,() -> {
                            removeCachePhoto();
                            finish();
                        }));
            } else {
                deleteImageContact.setId(modifyContact.getId());
                compositeSubscription
                        .add(WhoRURetrofit.getWhoRURetorfitInstance().addOrModifyContact(WhoRUApplication.getSessionId(), selected_groupIdList, map, cardImage, card_backImage, profileImage)
                                .flatMap(s -> WhoRURetrofit.getWhoRURetorfitInstance().deleteContactImage(WhoRUApplication.getSessionId(), deleteImageContact))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(r -> {
                                    if (_rxBus.hasObservers()) _rxBus.send(new AddContactSuccess());
                                }, Throwable::printStackTrace,() -> {
                                    removeCachePhoto();
                                    finish();
                                }));
            }
        } else if (modifyMyContact != null) {
            setRequestBody(addContact);
            if (!deleteProfileImage && !deleteCardImage) {
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().modifyMyInfo(WhoRUApplication.getSessionId(), map, cardImage, card_backImage, profileImage)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            if (s.equals("1")) {
                                if (_rxBus.hasObservers()) _rxBus.send(new ModifyMyContact());
                            } else
                                Toast.makeText(AddContactActivity.this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }, Throwable::printStackTrace,() -> {
                            removeCachePhoto();
                            finish();
                        }));
            } else {
                compositeSubscription.add(WhoRURetrofit.getWhoRURetorfitInstance().modifyMyInfo(WhoRUApplication.getSessionId(), map, cardImage, card_backImage, profileImage)
                        .flatMap(s -> WhoRURetrofit.getWhoRURetorfitInstance().deleteContactMyImage(WhoRUApplication.getSessionId(), deleteImageContact))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s1 -> {
                            if (s1.equals("1")) {
                                if (_rxBus.hasObservers()) _rxBus.send(new ModifyMyContact());
                            } else
                                Toast.makeText(AddContactActivity.this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        },Throwable::printStackTrace,() -> {
                            removeCachePhoto();
                            finish();
                        }));
            }
        }
    }

    public void setGroupDialog() {
        if (!addContactActivity_textView_group.getText().toString().equals("그룹")) {
            String s[] = addContactActivity_textView_group.getText().toString().split(",");
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

        new MaterialDialog.Builder(AddContactActivity.this)
                .title("그룹을 선택해주세요")
                .items(groupList_name)
                .itemsCallbackMultiChoice(selectedGroup, (dialog, which, text) -> {
                    String selectedGroupName = "";
                    selected_groupIdList = new ArrayList<>();
                    if (text.length > 0) {
                        for (int j = 0; j < text.length; j++) {
                            for (int i = 0; i < appGroupList.getTotal(); i++) {
                                if (text[j].toString().equals(appGroupList.getData().get(i).getName())) {
                                    selected_groupIdList.add(appGroupList.getData().get(i).getId());
                                    if (j < text.length - 1)
                                        selectedGroupName += appGroupList.getData().get(i).getName() + " , ";
                                    else
                                        selectedGroupName += appGroupList.getData().get(i).getName();
                                    break;
                                }
                            }
                        }
                    }


                    if (selectedGroupName.length() > 0)
                        addContactActivity_textView_group.setText(selectedGroupName);
                    else addContactActivity_textView_group.setText("그룹");
                    return true;
                })
                .positiveText("선택")
                .show();
    }

    public void setCameraDialog() {
        new MaterialDialog.Builder(this)
                .title("업로드할 이미지 선택")
                .items("사진촬영", "앨범선택", "삭제")
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            doTakePhotoAction();
                            break;
                        case 1:
                            pickFromGallery();
                            break;
                        case 2:
                            if (profileImageClicked) {
                                addContactActivity_circleImageView_profile.setImageResource(R.drawable.ic_regit_user);
                                profileResultUri = null;
                                profileImage = null;
                                deleteProfileImage = true;
                                deleteImageContact.setProfileImage("1");
                            }
                            if (cardImageClicked) {
                                addContactActivity_imageView_card.setImageResource(0);
                                addContactActivity_imageView_card.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                                addContactActivity_editText_card_default.setVisibility(View.VISIBLE);
                                cardResultUri = null;
                                cardImage = null;
                                card_backImage = null;
                                deleteCardImage = true;
                                deleteImageContact.setCardImage("1");
                                deleteImageContact.setCardImage_back("1");
                            }
                            break;
                    }
                }).show();
    }

    public void duplicationContactDialog(int contactId) {
        new MaterialDialog.Builder(this)
                .title("중복 연락처")
                .items("기존 연락처보기", "덮어쓰기")
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            Intent intent = new Intent(AddContactActivity.this, DetailContactActivity.class);
                            intent.putExtra("contactId", contactId);
                            startActivity(intent);
                            finish();
                            break;
                        case 1:
                            addContact.setId(contactId);
                            postContact(addContact);
                            break;
                    }
                }).show();
    }

    public void setRequestBody(AppContact addContact) {
        map = new HashMap<>();
        map.put("_id", RequestBody.create(null, String.valueOf(addContact.getId())));
        map.put("_name", RequestBody.create(null, addContact.getName()));
        map.put("_responsibility", RequestBody.create(null, addContact.getResponsibility()));
        map.put("_department", RequestBody.create(null, addContact.getDepartment()));
        map.put("_company", RequestBody.create(null, addContact.getCompany()));
        map.put("_memo", RequestBody.create(null, addContact.getMemo()));
        map.put("_phone", RequestBody.create(null, addContact.getPhone()));
        map.put("_companyPhone", RequestBody.create(null, addContact.getCompanyPhone()));
        map.put("_faxPhone", RequestBody.create(null, addContact.getFaxPhone()));
        map.put("_email", RequestBody.create(null, addContact.getEmail()));
        map.put("_companyAddress", RequestBody.create(null, addContact.getCompanyAddress()));
        map.put("_facebookAddress", RequestBody.create(null, addContact.getFacebookAddress()));
        map.put("_googleAddress", RequestBody.create(null, addContact.getGoogleAddress()));
        map.put("_linkedinAddress", RequestBody.create(null, addContact.getLinkedinAddress()));
        map.put("_instagramAddress", RequestBody.create(null, addContact.getInstagramAddress()));
        map.put("_extraAddress", RequestBody.create(null, addContact.getExtraAddress()));
        if (selected_groupIdList.size() == 0) {
            selected_groupIdList.add(null);
        }
        if (profileResultUri != null) {
            profileCropFile = new File(profileResultUri.getPath());
            profileImage = MultipartBody.Part.createFormData("_profileImage", profileCropFile.getName(), RequestBody.create(MediaType.parse("image/jpg"), profileCropFile));
            deleteProfileImage = false;
            if (deleteImageContact != null)
                deleteImageContact.setProfileImage("");
        } else
            profileImage = null;

        if (cardResultUri != null) {
            cardCropFile = new File(cardResultUri.getPath());
            cardImage = MultipartBody.Part.createFormData("_cardImage", cardCropFile.getName(), RequestBody.create(MediaType.parse("image/jpg"), cardCropFile));
            card_backImage = MultipartBody.Part.createFormData("_cardImage1", cardCropFile.getName(), RequestBody.create(MediaType.parse("image/jpg"), cardCropFile));
            deleteCardImage = false;
            if (deleteImageContact != null) {
                deleteImageContact.setCardImage_back("");
                deleteImageContact.setCardImage("");
            }
        } else {
            cardImage = null;
            card_backImage = null;
        }
    }

    public void removeCachePhoto() {
        if (profileResultUri != null) {  //이건 사진 찍엇을댸
            File f = new File(profileResultUri.getPath());
            if (f.exists()) {
                f.delete();
            }
            profileResultUri = null;
        }
        if (cardResultUri != null) {  //이건 사진 찍엇을댸
            File f = new File(cardResultUri.getPath());
            if (f.exists()) {
                f.delete();
            }
            cardResultUri = null;
        }
        if (cardBackResultUri != null) {  //이건 사진 찍엇을댸
            File f = new File(cardBackResultUri.getPath());
            if (f.exists()) {
                f.delete();
            }
            cardBackResultUri = null;
        }
    }

    public void getPermission() {
        TedPermission tedPermission = new TedPermission(getApplicationContext());
        tedPermission
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(AddContactActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public class ModifyMyContact {
    }
}