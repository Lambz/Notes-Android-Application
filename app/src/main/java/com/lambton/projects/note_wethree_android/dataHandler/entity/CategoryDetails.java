package com.lambton.projects.note_wethree_android.dataHandler.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

//This is helper pojo class for category entity

public class CategoryDetails {
    @Embedded
    public Category category;
    @Relation(parentColumn = "id", entityColumn = "noteCategoryId", entity = Note.class)
    private List<Note> noteList;
//  constructor
    public CategoryDetails() {
    }
//    getter
    public Category getCategory() {
        return category;
    }

    public List<Note> getNoteList() {
        return noteList;
    }
//  setter
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
}
