package com.lambton.projects.note_wethree_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.activities.CategoryListActivity;
import com.lambton.projects.note_wethree_android.activities.NotesListActivity;
import com.lambton.projects.note_wethree_android.dataHandler.CategoryHelperRepository;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>
{

    private static final String TAG = "CategoriesAdapter";
    private Context mContext;
    private List<Category> mCategoryList;
    private HashMap<Integer,Integer> mCategoryCount;

    public CategoriesAdapter(Context context, List<Category> categoryList, HashMap<Integer, Integer> categoryCount)
    {
        this.mContext = context;
        this.mCategoryList = categoryList;
        this.mCategoryCount = categoryCount;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        final Category category = mCategoryList.get(position);
        holder.mTitleTextView.setText(category.getCategoryName());
        holder.mNumNotesTextView.setText(String.valueOf(mCategoryCount.get(category.getId())));
        holder.mConstraintLayout.setOnClickListener(v ->
        {
            Intent intent = new Intent(mContext, NotesListActivity.class);
            intent.putExtra("category",mCategoryList.get(position));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        Log.v(TAG, "getItemCount: " + mCategoryList.size());
        return mCategoryList.size();
    }

    public Category deleteItem(int position)
    {
        Category category = mCategoryList.remove(position);
        notifyItemRemoved(position);
        return category;
    }

    public void addCategory(Category category, int position)
    {
        mCategoryList.add(position,category);
        notifyItemInserted(position);
    }

    public void setNewData(List<Category> categoryList)
    {
        mCategoryList = categoryList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTitleTextView;
        public TextView mNumNotesTextView;
        public ConstraintLayout mConstraintLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.note_title_textview);
            mNumNotesTextView = itemView.findViewById(R.id.num_notes_textview);
            mConstraintLayout = itemView.findViewById(R.id.item_contraint_layout);
        }
    }

}
