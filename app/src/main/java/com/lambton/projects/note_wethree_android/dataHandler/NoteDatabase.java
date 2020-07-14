package com.lambton.projects.note_wethree_android.dataHandler;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

@Database(entities = {Category.class, Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static final String NOTE_DB = "noteDatabase.db";
    public static volatile NoteDatabase instance;

    static synchronized NoteDatabase getInstance(Context context) {
        if(instance == null) {
            instance = createInstance(context);
        }
        return instance;
    }

    private static NoteDatabase createInstance(final Context context) {
        return Room.databaseBuilder(context, NoteDatabase.class, NOTE_DB)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract NoteDataInterface getNoteDataInterface();
    public abstract CategoryDataInterface getCategoryDataInterface();
}
