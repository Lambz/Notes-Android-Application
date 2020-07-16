package com.lambton.projects.note_wethree_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.adapters.MoveToCategoriesAdapter;
import com.lambton.projects.note_wethree_android.dataHandler.CategoryHelperRepository;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class MoveToActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<Category> mCategoryList;
    private CategoryHelperRepository mCategoryHelperRepository;
    private MoveToCategoriesAdapter mMoveToCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to);
        setMemberVariables();
    }

    private void setMemberVariables()
    {
        // Set ListView Object
        mListView = findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mCategoryHelperRepository = new CategoryHelperRepository(this.getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategories();
        setListViewData();
    }

    private void getCategories()
    {
        mCategoryList = mCategoryHelperRepository.getAllCategoriesSortedByTitle();
    }

    private void setListViewData()
    {
        mMoveToCategoriesAdapter  = new MoveToCategoriesAdapter(this,mCategoryList);
        mListView.setAdapter(mMoveToCategoriesAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("category",mCategoryList.get(position));
        setResult(RESULT_OK,intent);
        finish();
    }

    public void cancelClicked(View view)
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}