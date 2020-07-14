package com.lambton.projects.note_wethree_android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.adapters.CategoriesAdapter;

public class CategoryListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    //Category list variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        setMemberVariables();
    }

    private void setMemberVariables()
    {
        // Set ListView Object
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategories();
        setRecyclerViewData();
    }

    private void getCategories()
    {
        // Get Categories
    }

    private void setRecyclerViewData()
    {
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(categoriesAdapter);
    }

    public void addCategoryClicked(View view)
    {
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(CategoryListActivity.this,NotesListActivity.class);
//        intent.putExtra("position",position);
//        startActivity(intent);
//    }
}