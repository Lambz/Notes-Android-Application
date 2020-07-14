package com.lambton.projects.note_wethree_android.dataHandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;

@Dao
public interface CategoryDataInterface {

    @Insert
    void insertCategory(Category category);
    @Delete
    void deleteCategory(Category category);

}
