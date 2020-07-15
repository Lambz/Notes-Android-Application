package com.lambton.projects.note_wethree_android.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.Utils;
import com.lambton.projects.note_wethree_android.dataHandler.NoteHelperRepository;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class NoteDetailActivity extends AppCompatActivity
{

    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 121;
    private static final int GALLERY_REQUEST_CODE = 2404;
    private static final int CAMERA_PERM_CODE = 101;
    private static final long VIBRATION_PERIOD = 500;
    private static final int MOVE_TO_CATEGORY_REQUEST = 1234;

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Button mDeleteButton;
    private Button mCameraButton;
    private Button mLocationButton;
    private Button mFolderButton;
    private ImageView mImageView;
    private TextView mCreatedOnTextView;
    private TextView mNoteTopTextView;

    private Bitmap mSelectedImage;
    private Animation mShakeAnimation;
    private Vibrator mVibrator;
    private String mRecordedAudio;
    private Button mAudioButton;
    private boolean mIsRecorded = false;
    private boolean mIsRecording = false;
    private boolean mIsPlaying = false;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private LocationManager mLocationManager;
    private Category mCategory;
    private Note mNote;
    private NoteHelperRepository mNoteHelperRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        saveMemberVariables();
        setData();
    }

    private void setData()
    {
        if(mNote == null)
        {
            return;
        }
        mNoteTopTextView.setText("Update Note");
        mTitleEditText.setText(mNote.getNoteTitle());
        mDescriptionEditText.setText(mNote.getNoteDescription());
        if (mNote.getNoteImageAsBitmap() != null)
        {
            mImageView.setVisibility(View.VISIBLE);
            mSelectedImage = mNote.getNoteImageAsBitmap();
            mImageView.setImageBitmap(mSelectedImage);
        }
        if (mNote.getNoteAudio() != null && !mNote.getNoteAudio().isEmpty())
        {
            mRecordedAudio = mNote.getNoteAudio();
            mIsRecorded = true;
            mAudioButton.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));
        }
        DateFormat df = new DateFormat();
        mCreatedOnTextView.setText("Created on "+df.format("EEE, MM-dd-yyyy hh:mm",mNote.getNoteCreatedDate()));
    }

    private void saveMemberVariables()
    {
        mNoteHelperRepository = new NoteHelperRepository(this.getApplication());
        mNote = (Note) getIntent().getSerializableExtra("note");
        mCategory = (Category) getIntent().getSerializableExtra("category");
        mShakeAnimation = AnimationUtils.loadAnimation(NoteDetailActivity.this, R.anim.shake);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mImageView = findViewById(R.id.imageview);
        mNoteTopTextView = findViewById(R.id.note_top_textview);
        mTitleEditText = findViewById(R.id.notes_detail_title);
        mDescriptionEditText = findViewById(R.id.notes_detail_desc);
        mCreatedOnTextView = findViewById(R.id.created_on_textview);
        mFolderButton = findViewById(R.id.foler_btn);
        mDeleteButton = findViewById(R.id.delete_btn);
        mCameraButton = findViewById(R.id.camera_btn);
        mLocationButton = findViewById(R.id.location_btn);
        mAudioButton = findViewById(R.id.audio_btn);
        mAudioButton.setOnLongClickListener(mAudioButtonLongClickListener);
        mAudioButton.setOnClickListener(mAudioButtonClickListener);
        if (mNote == null)
        {
            mDeleteButton.setVisibility(View.GONE);
            mLocationButton.setVisibility(View.GONE);
            mFolderButton.setVisibility(View.GONE);
            mCreatedOnTextView.setVisibility(View.GONE);
        }
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestLocationPermission();
        }
    }

    public void saveClicked(View view)
    {
        String title = mTitleEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        if (title.isEmpty())
        {
            shakeAndVibrate(mTitleEditText);
            return;
        }
        if (mNote == null)
        {
            Location location = null;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            Note note = new Note(title, description, mCategory.getId());
            if (mIsRecorded)
            {
                note.setNoteAudio(mRecordedAudio);
            }
            if (mSelectedImage != null)
            {
                note.setNoteImageAsBitmap(mSelectedImage);
            }
            if (location != null)
            {
                note.setNoteLatitude(location.getLatitude());
                note.setNoteLongitude(location.getLongitude());
            }
            Utils.dump(note);
            mNoteHelperRepository.insertNewNoteInDatabase(note);
            finish();
        } else
        {
            mNote.setNoteTitle(title);
            mNote.setNoteDescription(description);
            if (mIsRecorded)
            {
                mNote.setNoteAudio(mRecordedAudio);
            }
            if (mSelectedImage != null)
            {
                mNote.setNoteImageAsBitmap(mSelectedImage);
            }
            mNoteHelperRepository.updateNoteInDatabase(mNote);
            finish();
        }
    }

    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
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
        finish();
    }

    private void shakeAndVibrate(EditText edittext)
    {
        edittext.startAnimation(mShakeAnimation);
        mVibrator.vibrate(VIBRATION_PERIOD);
    }

    Button.OnLongClickListener mAudioButtonLongClickListener = new Button.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v)
        {
            return false;
        }
    };

    Button.OnClickListener mAudioButtonClickListener = new Button.OnClickListener()
    {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v)
        {
            if (mIsRecorded)
            {
                if (mIsPlaying)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    Toast.makeText(NoteDetailActivity.this, "Stop Playing...", Toast.LENGTH_SHORT).show();
                    mIsPlaying = false;
                    mAudioButton.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));
                } else
                {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setOnCompletionListener(mp -> mAudioButton.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24)));
                    try
                    {
                        mMediaPlayer.setDataSource(mRecordedAudio);
                        mMediaPlayer.prepare();
                        Toast.makeText(NoteDetailActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
                        mMediaPlayer.start();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    mIsPlaying = true;
                    mAudioButton.setBackground(getDrawable(R.drawable.ic_baseline_pause_24));
                }
            } else
            {
                if (mIsRecording)
                {
                    mMediaRecorder.stop();
                    Toast.makeText(NoteDetailActivity.this, "Stop Recording...", Toast.LENGTH_SHORT).show();
                    mIsRecorded = true;
                    mIsRecording = false;
                    mAudioButton.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));
                } else
                {
                    mRecordedAudio = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    try
                    {
                        mMediaRecorder.prepare();
                        Toast.makeText(NoteDetailActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                        mMediaRecorder.start();
                        mAudioButton.setBackground(getDrawable(R.drawable.ic_baseline_mic_24));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    mIsRecording = true;
                }
            }
        }
    };

    private void setupMediaRecorder()
    {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setOutputFile(mRecordedAudio);
    }

    public void deleteClicked(View view)
    {
        mNoteHelperRepository.deleteNoteFromDatabase(mNote);
        finish();
    }

    public void moveToClicked(View view)
    {
        Intent intent = new Intent(this,MoveToActivity.class);
        startActivityForResult(intent,MOVE_TO_CATEGORY_REQUEST);
    }
}