package com.lambton.projects.note_wethree_android.dataHandler;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_data")
public class Category {

    @PrimaryKey
    private String categoryName;
}
