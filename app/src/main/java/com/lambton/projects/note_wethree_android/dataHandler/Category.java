package com.lambton.projects.note_wethree_android.dataHandler;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Fts4
@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    private int categoryId;

    private String categoryName;
}
