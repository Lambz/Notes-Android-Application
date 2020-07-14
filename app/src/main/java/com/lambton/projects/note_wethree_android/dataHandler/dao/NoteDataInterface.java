package com.lambton.projects.note_wethree_android.dataHandler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.util.List;

@Dao
public interface NoteDataInterface {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);
    @Transaction
    @Query("SELECT * FROM note_data WHERE id=:noteId")
    Note getNote(int noteId);
    @Transaction
    @Query("SELECT * FROM note_data WHERE noteCategoryId=:categoryId")
    List<Note> getAllNotesForCategory(int categoryId);
    @Transaction
    @Query("SELECT * FROM note_data")
    List<Note> getAllNotes();
    @Transaction
    @Query("SELECT * FROM note_data WHERE noteDescription LIKE:searchString AND noteCategoryId=:categoryId")
    List<Note> getSearchResults(String searchString, int categoryId);
    @Transaction
    @Query("SELECT * FROM note_data WHERE noteCategoryId=:categoryId ORDER BY noteCreatedDate DESC")
    List<Note> getSortedNotesByDate(int categoryId);
}
