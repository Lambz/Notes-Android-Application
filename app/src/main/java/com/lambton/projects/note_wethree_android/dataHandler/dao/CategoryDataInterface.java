package com.lambton.projects.note_wethree_android.dataHandler.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.util.List;

@Dao
public interface CategoryDataInterface {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertCategory(Category category);
    @Transaction
    @Delete
    void deleteCategory(Category category);
    @Transaction
    @Query("SELECT * FROM CATEGORY_DATA WHERE id=:categoryId")
    LiveData<Category> getCategory(int categoryId);

    //    pojo query to fetch data
    @Transaction
    @Query("SELECT * FROM category_data")
    LiveData<List<Category>> loadAllCategories();

}
