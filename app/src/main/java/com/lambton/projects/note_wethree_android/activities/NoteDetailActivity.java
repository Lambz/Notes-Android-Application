package com.lambton.projects.note_wethree_android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.lambton.projects.note_wethree_android.R;

import java.io.File;

public class NoteDetailActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 121;
    private static final int GALLERY_REQUEST_CODE = 2404;
    private static final int CAMERA_PERM_CODE = 101;
    private static final long VIBRATION_PERIOD = 500;

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Button mDeleteButton;
    private Button mCameraButton;
    private ImageView mImageView;
    private Integer mPosition;
    private Bitmap mSelectedImage;
    private Animation mShakeAnimation;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        mPosition = (Integer) getIntent().getSerializableExtra("position");
        saveMemberVariables();
    }

    private void saveMemberVariables()
    {
        mShakeAnimation = AnimationUtils.loadAnimation(NoteDetailActivity.this, R.anim.shake);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mTitleEditText = findViewById(R.id.notes_detail_title);
        mDescriptionEditText = findViewById(R.id.notes_detail_desc);
        mDeleteButton = findViewById(R.id.delete_btn);
        mCameraButton = findViewById(R.id.camera_btn);
        mImageView = findViewById(R.id.imageview);
    }

    public void saveClicked(View view)
    {
        if(mPosition == null)
        {
            String title = mTitleEditText.getText().toString();
            String description = mDescriptionEditText.getText().toString();
            if(title.isEmpty())
            {

                return;
            }
            // Save New Note
        }
        else
        {
            // Update Existing Note
        }
    }

    public void cameraClicked(View view)
    {
        String[] arr = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Choose Image From: ");
        dialog.setItems(arr, (dialog1, position) ->
        {
            if (position == 0)
            {
                askCameraPermissions();
            } else
            {
                ImagePicker.Companion.with(this)
                        .galleryOnly()
                        .compress(1024)
                        .start();
            }
        });
        dialog.setPositiveButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alert = dialog.create();
        alert.show();
    }

    private void askCameraPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
        } else
        {
            openCamera();
            //dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == CAMERA_PERM_CODE)
        {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                openCamera();
                //dispatchTakePictureIntent();
            } else
            {
                Toast.makeText(this, "Camera and File Access Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera()
    {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                mImageView.setVisibility(View.VISIBLE);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                mSelectedImage = bitmap;
                mImageView.setImageBitmap(bitmap);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK && data != null && data.getData() != null)
            {
                mImageView.setVisibility(View.VISIBLE);
                File file = ImagePicker.Companion.getFile(data);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                mSelectedImage = bitmap;
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

    public void backClicked(View view)
    {
    }

    private void shakeAndVibrate(EditText edittext)
    {
        edittext.startAnimation(mShakeAnimation);
        mVibrator.vibrate(VIBRATION_PERIOD);
    }
}