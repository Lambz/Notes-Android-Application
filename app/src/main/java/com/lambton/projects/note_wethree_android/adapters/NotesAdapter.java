package com.lambton.projects.note_wethree_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.activities.NoteDetailActivity;
import com.lambton.projects.note_wethree_android.activities.NotesListActivity;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>
{

    private static final String TAG = "NotesAdapter";
    private Context mContext;
    private List<Note> mNoteList;

    public NotesAdapter(Context context, List<Note> noteList)
    {
        this.mContext = context;
        mNoteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.notes_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        final Note note = mNoteList.get(position);
        holder.mTitleTextView.setText(note.getNoteTitle());
        if(!note.getNoteDescription().isEmpty())
        {
            holder.mDescTextView.setText(note.getNoteDescription());
        }
        else
        {
            holder.mDescTextView.setVisibility(View.GONE);
        }
        if(note.getNoteImage() != null)
        {
            if(note.getNoteImage() != null)
            {
                holder.mImageView.setImageBitmap(note.getNoteImageAsBitmap());
            }
        }
        else
        {
            holder.mImageView.setVisibility(View.GONE);
        }
        holder.mConstraintLayout.setOnClickListener(v ->
        {
            Intent intent = new Intent(mContext, NoteDetailActivity.class);
            intent.putExtra("note",mNoteList.get(position));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        Log.v(TAG, "getItemCount: " + mNoteList.size());
        return mNoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTitleTextView;
        public TextView mDescTextView;
        public ConstraintLayout mConstraintLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.notes_list_image);
            mTitleTextView = itemView.findViewById(R.id.notes_title_tv);
            mDescTextView = itemView.findViewById(R.id.notes_desc_tv);
            mConstraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

    public Note deleteItem(int position)
    {
        Note note = mNoteList.remove(position);
        notifyItemRemoved(position);
        return note;
    }

    public void addItem(Note note, int position)
    {
        mNoteList.add(position,note);
        notifyItemInserted(position);
    }

    public void setNewData(List<Note> noteList)
    {
        mNoteList = noteList;
    }
}
