package com.lambton.projects.note_wethree_android.dataHandler;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_data")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String categoryName;
//    constructor
//    constructor for serialization
    public Category() {
    }
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
//  getters
    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }
//  setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryName(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }
}
