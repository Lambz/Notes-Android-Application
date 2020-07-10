package com.lambton.projects.note_wethree_android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.lambton.projects.note_wethree_android.R;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Integer mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        mPosition = (Integer) getIntent().getSerializableExtra("position");
    }

    public void saveClicked(View view)
    {
        if(mPosition == null)
        {
            String title = mTitleEditText.getText().toString();
            String description = mDescriptionEditText.getText().toString();
            if(title.equals(""))
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
}