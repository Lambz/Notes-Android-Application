package com.lambton.projects.note_wethree_android.dataHandler;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithNotes {

    @Embedded
    public Category category;
    @Relation(
            parentColumn = "categoryName",
            entityColumn = "noteCategory"
    )
    public List<Note> notes;
}
