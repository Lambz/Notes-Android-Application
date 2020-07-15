package com.lambton.projects.note_wethree_android.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.Utils;
import com.lambton.projects.note_wethree_android.adapters.CategoriesAdapter;
import com.lambton.projects.note_wethree_android.dataHandler.CategoryHelperRepository;
import com.lambton.projects.note_wethree_android.dataHandler.NoteHelperRepository;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CategoryListActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1001;

    private RecyclerView mRecyclerView;
    private CategoriesAdapter mCategoriesAdapter;
    private CategoryHelperRepository mCategoryHelperRepository;
    private List<Category> mCategoryList = new ArrayList<>();
    private SearchView mSearchView;
    private TextView mSortTextView;
    private int mSort = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        askForPermissions();
        setMemberVariables();
    }

    private void setMemberVariables()
    {
        mCategoryHelperRepository = new CategoryHelperRepository(this.getApplication());
        mRecyclerView = findViewById(R.id.recycler_view);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(mQueryTextListener);
        mSortTextView = findViewById(R.id.sort_textview);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getCategories();
        setRecyclerViewData();
    }

    private void getCategories()
    {
        mCategoryList = mCategoryHelperRepository.getAllCategories();
    }

    private void setRecyclerViewData()
    {
        mCategoriesAdapter = new CategoriesAdapter(this,mCategoryList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCategoriesAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallBack);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void addCategoryClicked(View view)
    {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_edittext, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(view1 -> dialogBuilder.dismiss());
        button1.setOnClickListener(view12 ->
        {
            addCategory(editText.getText().toString());
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void addCategory(String category_name)
    {
        if(category_name.isEmpty())
        {
            Utils.showError("Invalid Category Name","Please enter a valid Category Name",this);
            return;
        }
        Category category = new Category(category_name);
        CategoryHelperRepository helperRepository = new CategoryHelperRepository(this.getApplication());
        helperRepository.insertNewCategoryInDatabase(category);
//        getCategories();
        mCategoryList.add(category);
        mCategoriesAdapter.setNewData(mCategoryList);
        mCategoriesAdapter.notifyDataSetChanged();
    }

    ItemTouchHelper.SimpleCallback mSimpleCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
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
                Category category = mCategoriesAdapter.deleteItem(position);
                Snackbar.make(mRecyclerView,category.getCategoryName(),Snackbar.LENGTH_LONG).setAction("Undo", v ->
                {
                    mCategoriesAdapter.addCategory(category,position);
                    delete.set(false);
                }).show();
                new Handler().postDelayed(() ->
                {
                    if(delete.get())
                    {
                        mCategoryHelperRepository.deleteCategoryFromDatabase(category);
                    }
                },3000);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
        {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(CategoryListActivity.this,R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void askForPermissions()
    {
        if (ContextCompat.checkSelfPermission(CategoryListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CategoryListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CategoryListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CategoryListActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CategoryListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CategoryListActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(CategoryListActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)  && ActivityCompat.shouldShowRequestPermissionRationale(CategoryListActivity.this,
                    Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(CategoryListActivity.this,
                    Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(CategoryListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else
            {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(CategoryListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else
        {
            // Permission has already been granted
        }
    }

    SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener()
    {
        @Override
        public boolean onQueryTextSubmit(String query)
        {
            if(query.isEmpty())
            {
                getCategories();
                mCategoriesAdapter.setNewData(mCategoryList);
            }
            else
            {
                List<Category> categories = new ArrayList<>();
                for(Category category: mCategoryList)
                {
                    if(category.getCategoryName().contains(query))
                    {
                        categories.add(category);
                    }
                }
                mCategoriesAdapter.setNewData(categories);
            }
            mCategoriesAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText)
        {
            try
            {
                List<Category> categories = new ArrayList<>();
                for(Category category: mCategoryList)
                {
                    if(category.getCategoryName().contains(newText))
                    {
                        categories.add(category);
                    }
                }
                mCategoriesAdapter.setNewData(categories);
                mCategoriesAdapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }
    };

    public void sortClicked(View view)
    {
        switch (mSort)
        {
            case 0:
                sortTitleDesc();
                break;
            case 1:
                sortTitleAsc();
        }
    }

    private void sortTitleAsc()
    {
        mCategoryList.sort((o1, o2) -> o1.getCategoryName().compareTo(o2.getCategoryName()));
        mCategoriesAdapter.setNewData(mCategoryList);
        mCategoriesAdapter.notifyDataSetChanged();
        mSortTextView.setText("Title Asc");
        mSort = 0;
    }

    private void sortTitleDesc()
    {
        mCategoryList.sort((o1, o2) -> -o1.getCategoryName().compareTo(o2.getCategoryName()));
        mCategoriesAdapter.setNewData(mCategoryList);
        mCategoriesAdapter.notifyDataSetChanged();
        mSortTextView.setText("Title Desc");
        mSort = 1;
    }
}