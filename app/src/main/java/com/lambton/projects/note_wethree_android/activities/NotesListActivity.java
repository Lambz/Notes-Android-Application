package com.lambton.projects.note_wethree_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.adapters.CategoriesAdapter;
import com.lambton.projects.note_wethree_android.adapters.NotesAdapter;
import com.lambton.projects.note_wethree_android.dataHandler.Category;
import com.lambton.projects.note_wethree_android.dataHandler.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NotesListActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private Category mCategory;
    private boolean mIsEditing = false;
    private final int MOVE_TO_REQUEST_CODE = 1;
    private NotesAdapter mNotesAdapter = null;
    private List<Note> mNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        setMemberVariables();
    }

    private void setMemberVariables()
    {
        // Set ListView Object
        mRecyclerView = findViewById(R.id.recycler_view);
        mCategory = (Category) getIntent().getSerializableExtra("category");
        mNoteList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
        setRecyclerViewData();
    }

    private void getNotes()
    {
        // Get Notes
    }

    private void setRecyclerViewData()
    {
        mNotesAdapter = new NotesAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mNotesAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallBack);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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

    ItemTouchHelper.SimpleCallback mSimpleCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
        {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            int position = viewHolder.getAdapterPosition();
            if(direction == ItemTouchHelper.LEFT)
            {
                AtomicBoolean delete = new AtomicBoolean(true);
                Note note = mNotesAdapter.deleteItem(position);
                Snackbar.make(mRecyclerView,note.getNoteTitle(),Snackbar.LENGTH_LONG).setAction("Undo", v ->
                {
                    mNotesAdapter.addItem(note,position);
                    delete.set(false);
                }).show();
                new Handler().postDelayed(() ->
                {
                    if(delete.get())
                    {
                        //Delete Category
                    }
                },3000);
            }
            else if (direction == ItemTouchHelper.RIGHT)
            {
                // Get Note at position
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
        {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(NotesListActivity.this,R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(NotesListActivity.this,R.color.green))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_move_to_inbox_24)
                    .addSwipeRightLabel("Move")
                    .setSwipeRightLabelColor(Color.WHITE)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}