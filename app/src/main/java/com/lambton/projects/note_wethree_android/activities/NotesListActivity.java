package com.lambton.projects.note_wethree_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.projects.note_wethree_android.R;

public class NotesListActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private int mPosition;
    private boolean mIsEditing = false;
    private final int MOVE_TO_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        mPosition = getIntent().getIntExtra("position",0);
    }

    private void setMemberVariables()
    {
        // Set ListView Object
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
        setListViewData();
    }

    private void getNotes()
    {
        // Get Notes
    }

    private void setListViewData()
    {

    }

    public void moveToClicked(View view)
    {
        Intent intent = new Intent(NotesListActivity.this, MoveToActivity.class);
        startActivityForResult(intent, MOVE_TO_REQUEST_CODE);
    }

    public void newNoteClicked(View view)
    {
        Intent intent = new Intent(NotesListActivity.this, NoteDetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MOVE_TO_REQUEST_CODE && requestCode == Activity.RESULT_OK)
        {

        }
    }
}