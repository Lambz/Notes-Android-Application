package com.lambton.projects.note_wethree_android.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.projects.note_wethree_android.R;

public class CategoryListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    //Category list variables
    ListView listView;
    ImageView folderImage;
    TextView categoryTitle, notesNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        setMemberVariables();
    }

    private void setMemberVariables()
    {
        // Set ListView Object
        folderImage = findViewById(R.id.folder_imageview);
        categoryTitle = findViewById(R.id.title_textview);
        notesNumber = findViewById(R.id.num_notes_textview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategories();
        setListViewData();
    }

    private void getCategories()
    {
        // Get Categories
    }

    private void setListViewData()
    {

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(CategoryListActivity.this,NotesListActivity.class);
//        intent.putExtra("position",position);
//        startActivity(intent);
//    }
}