package com.lambton.projects.note_wethree_android.dataHandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.util.List;

@Dao
public interface NoteDataInterface {

    @Insert
    void insertNote(Note note);
    @Update
    void updateNote(Note note);
    @Delete
    void deleteNote(Note note);


//    mutliple move, fetch and deletion methods to be implemented

//    @Query("SELECT * FROM note_data WHERE noteCategoryId=:id")
//    List<Note> fetchNotes(int categoryNameId);


}
