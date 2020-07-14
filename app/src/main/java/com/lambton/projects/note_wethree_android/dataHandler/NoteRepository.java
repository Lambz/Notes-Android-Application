package com.lambton.projects.note_wethree_android.dataHandler;

import com.lambton.projects.note_wethree_android.dataHandler.dao.CategoryDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

public class NoteRepository {

    private CategoryDataInterface categoryDataInterface;

    public NoteRepository(NoteDatabase noteDatabase) {
        categoryDataInterface = noteDatabase.categoryDataInterface();
    }
//    category operations
    public void insertNewCategoryInDatabase(Category category) {
        if(category == null) {
            new NullPointerException();
        }
        else {
            categoryDataInterface.insertCategory(category);
        }
    }

    public void delteCategoryFromDatabase(Category category) {
        categoryDataInterface.deleteCategory(category);
    }


//    note operations
    public void insertNoteForCategoryInDatabase(Note note, int categoryId) {
        if(note == null) {
            new NullPointerException();
        }
        else {
            categoryDataInterface.insertNote(note);
        }
    }




}
