package com.lambton.projects.note_wethree_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.activities.MoveToActivity;
import com.lambton.projects.note_wethree_android.activities.NotesListActivity;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;

import java.util.List;

public class MoveToCategoriesAdapter extends BaseAdapter
{
    private Context mContext;
    private List<Category> mCategoryList;

    public MoveToCategoriesAdapter(Context context,List<Category> categoryList)
    {
        this.mCategoryList = categoryList;
        mContext = context;
    }

    @Override
    public int getCount()
    {
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        final View result;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.move_to_list_item,parent,false);
            viewHolder.mTitleTextView = convertView.findViewById(R.id.title_textview);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.mTitleTextView.setText(mCategoryList.get(position).getCategoryName());
        return convertView;
    }

    private static class ViewHolder
    {
        TextView mTitleTextView;
    }
}
