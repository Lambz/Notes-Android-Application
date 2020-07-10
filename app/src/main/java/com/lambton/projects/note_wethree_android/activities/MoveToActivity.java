package com.lambton.projects.note_wethree_android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.lambton.projects.note_wethree_android.R;

public class MoveToActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to);
        setMemberVariables();
    }

    private void setMemberVariables()
    {
        // Set ListView Object
        mListView.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Move Notes
    }
}