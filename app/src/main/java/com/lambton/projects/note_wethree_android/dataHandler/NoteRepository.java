package com.lambton.projects.note_wethree_android.dataHandler;

import android.app.Application;

public class NoteRepository {

    private NoteDataInterface noteDataInterface;
    private CategoryDataInterface categoryDataInterface;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDataInterface = database.getNoteDataInterface();
        categoryDataInterface = database.getCategoryDataInterface();
    }

//    category operations

//    note operations



}
