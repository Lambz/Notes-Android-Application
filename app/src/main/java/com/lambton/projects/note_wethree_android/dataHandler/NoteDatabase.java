package com.lambton.projects.note_wethree_android.dataHandler;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.lambton.projects.note_wethree_android.dataHandler.dao.CategoryDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.dao.NoteDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;
import com.lambton.projects.note_wethree_android.dataHandler.entity.ConvertDatatypes;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

@Database(entities = {Category.class, Note.class}, version = 1, exportSchema = false)
@TypeConverters({ConvertDatatypes.class})
public abstract class NoteDatabase extends RoomDatabase {

    private static final String NOTE_DB = "noteDatabase.db";
    public static volatile NoteDatabase instance;
    public abstract CategoryDataInterface categoryDataInterface();
    public abstract NoteDataInterface noteDataInterface();

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

}
