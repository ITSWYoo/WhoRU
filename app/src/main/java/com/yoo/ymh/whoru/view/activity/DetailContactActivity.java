package com.yoo.ymh.whoru.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yoo.ymh.whoru.R;
import com.yoo.ymh.whoru.model.Contact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailContactActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_FROM_CAMERA = 0;

    @BindView(R.id.detailContactActivity_toolbar)
    Toolbar detailContactActivity_toolbar;

    @BindView(R.id.detailContactActivity_imageView_card)
    ImageView detailContactActivity_imageView_card;

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private Uri mImageCaptureUri;
    private String cropImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        ButterKnife.bind(this);
        setSupportActionBar(detailContactActivity_toolbar);

        Intent intent = getIntent();
        Contact item = intent.getParcelableExtra("detailContact");
        Log.e("get", item.getName() + " " + item.getPhone());

        detailContactActivity_imageView_card.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_setting_menu,menu);
        menu.findItem(R.id.action_delete_card).setTitle(Html.fromHtml("<font color=red>삭제하기</font>"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_add_localContact:
                Log.e("addContact","addContact");
                // 핸드폰 연락처에 추가.
                break;
            case R.id.action_delete_card:
                Log.e("deletecard","deletecard");
                // 명함 삭제.
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
                    Toast.makeText(DetailContactActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                try {
                    handleCropResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(requestCode == PICK_FROM_CAMERA)
            {
                final Uri photoUri = mImageCaptureUri;
                Log.e("사진","찰칵");
//                final Uri capturedUri = data.getData();
                startCropActivity(photoUri);
//                Bitmap bm = (Bitmap) data.getExtras().get("data");
//                detailContactActivity_imageView_card.setImageBitmap(bm);
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
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName += ".jpg";
        cropImagePath = getCacheDir()+"/"+destinationFileName;

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setActiveWidgetColor(getResources().getColor(R.color.colorAccent));
        options.setToolbarColor(getResources().getColor(R.color.colorAccent));
        options.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName))).withMaxResultSize(1000,1000).withOptions(options);
        uCrop.start(DetailContactActivity.this);
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(DetailContactActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(DetailContactActivity.this,"Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleCropResult(@NonNull Intent result) throws IOException {
        final Uri resultUri = UCrop.getOutput(result);

//        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), resultUri.getLastPathSegment());
//
//        File saveFile = new File(downloadsDirectoryPath, filename);
//
//        FileInputStream inStream = new FileInputStream(new File(resultUri.getPath()));
//        FileOutputStream outStream = new FileOutputStream(saveFile);
//        FileChannel inChannel = inStream.getChannel();
//        FileChannel outChannel = outStream.getChannel();
//        inChannel.transferTo(0, inChannel.size(), outChannel);
//        inStream.close();
//        outStream.close();  //사진저장

        Bitmap cromImage = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
        detailContactActivity_imageView_card.setImageBitmap(cromImage);

        if(mImageCaptureUri!=null)
        {
            File f = new File(mImageCaptureUri.getPath());
            if(f.exists()){
                f.delete();
            }
            mImageCaptureUri = null;
        }
    }
    private void doTakePhotoAction()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + "cache" + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }
    @Override
    public void onClick(View view) {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                pickFromGallery();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }
}
