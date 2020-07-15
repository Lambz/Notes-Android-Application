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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.adapters.CategoriesAdapter;
import com.lambton.projects.note_wethree_android.adapters.NotesAdapter;
import com.lambton.projects.note_wethree_android.dataHandler.NoteHelperRepository;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NotesListActivity extends AppCompatActivity{

    private static final int MOVE_TO_CATEGORY_REQUEST = 1234;
    private RecyclerView mRecyclerView;
    private TextView mNumNotesTextView;
    private TextView mCategoryNameTextView;

    private Category mCategory;
    private boolean mIsEditing = false;
    private NotesAdapter mNotesAdapter = null;
    private List<Note> mNoteList;
    private NoteHelperRepository mNoteHelperRepository;
    private List<Note> mNotesToMove;

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
        mNumNotesTextView = findViewById(R.id.num_notes_textview);
        mCategoryNameTextView = findViewById(R.id.category_name_textview);
        mNoteList = new ArrayList<>();
        mNoteHelperRepository = new NoteHelperRepository(this.getApplication());
        mCategoryNameTextView.setText(mCategory.getCategoryName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
        setRecyclerViewData();
    }

    private void getNotes()
    {
        mNoteList = mNoteHelperRepository.getNotesForCategory(mCategory.getId());
        mNumNotesTextView.setText("Number of Notes: "+mNoteList.size());
    }

    private void setRecyclerViewData()
    {
        mNotesAdapter = new NotesAdapter(this, mNoteList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mNotesAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallBack);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void moveToClicked(View view)
    {
        Intent intent = new Intent(NotesListActivity.this, MoveToActivity.class);
        startActivityForResult(intent, MOVE_TO_CATEGORY_REQUEST);
    }

    public void newNoteClicked(View view)
    {
        Intent intent = new Intent(NotesListActivity.this, NoteDetailActivity.class);
        intent.putExtra("category",mCategory);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MOVE_TO_CATEGORY_REQUEST )
        {
            Category category = (Category) data.getSerializableExtra("category");
            for(Note note: mNotesToMove)
            {
                note.setNoteCategoryId(category.getId());
                mNoteList.remove(note);
                mNoteHelperRepository.updateNoteInDatabase(note);
            }
            mNotesAdapter.setNewData(mNoteList);
            mNotesAdapter.notifyDataSetChanged();
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
                mNotesToMove = new ArrayList<>();
                mNotesToMove.add(mNoteList.get(position));
                Intent intent = new Intent(NotesListActivity.this, MoveToActivity.class);
                startActivityForResult(intent,MOVE_TO_CATEGORY_REQUEST);
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

    public void backClicked(View view)
    {
        finish();
    }
}