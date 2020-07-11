package com.lambton.projects.note_wethree_android.dataHandler;

import android.media.Image;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "note_data", foreignKeys = @ForeignKey( entity = Category.class,
                                                            parentColumns = "categoryName",
                                                            childColumns = "noteCategory" ))
public class Note {

    private String noteCategory;
    private String noteTitle;
    private String noteDescription;
    @PrimaryKey
    private Date noteCreatedDate;
    private String noteAudio;
    private String noteImage;
    private double noteLongitude;
    private double noteLatitude;

//    constructors
    public Note(String noteTitle, String noteDescription, Date noteCreatedDate, String noteCategory) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.noteCreatedDate = noteCreatedDate;
        this.noteCategory = noteCategory;
    }

//    setters

    public void setNoteCategory(String noteCategory) {
        this.noteCategory = noteCategory;
    }

    public void setNoteAudio(String noteAudio) {
        this.noteAudio = noteAudio;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }

    public void setNoteLongitude(double noteLongitude) {
        this.noteLongitude = noteLongitude;
    }

    public void setNoteLatitude(double noteLatitude) {
        this.noteLatitude = noteLatitude;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    //    getters

    public String getNoteCategory() {
        return noteCategory;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public Date getNoteCreatedDate() {
        return noteCreatedDate;
    }

    public String getNoteAudio() {
        return noteAudio;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public double getNoteLongitude() {
        return noteLongitude;
    }

    public double getNoteLatitude() {
        return noteLatitude;
    }
}
