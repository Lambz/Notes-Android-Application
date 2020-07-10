package com.lambton.projects.note_wethree_android.dataHandler;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import java.util.List;

@Dao
public interface CategoryWithNotesDataInterface {

    @Transaction
    @Query("SELECT * FROM note_data")
    public List<CategoryWithNotes> getNotesForCategory();

}
