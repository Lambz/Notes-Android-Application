package com.lambton.projects.note_wethree_android.dataHandler;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "note_data")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ForeignKey(entity = Category.class,
            parentColumns = "categoryName",
            childColumns = "noteCategory",
            onDelete = CASCADE)
    private String noteCategory;
    @NonNull
    private String noteTitle;
    private String noteDescription;
    @NonNull
    private String noteCreatedDate;
    private String noteAudio;
    private String noteImage;
    private double noteLongitude;
    private double noteLatitude;

//    constructors
//    for serialization
    public Note() {
    }

    public Note(String noteTitle, String noteDescription, String noteCategory) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.noteCreatedDate = simpleDateFormat.format(calendar.getTime());
        this.noteCategory = noteCategory;
    }

//    setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNoteCreatedDate(String noteCreatedDate) {
        this.noteCreatedDate = noteCreatedDate;
    }

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

    public int getId() {
        return id;
    }

    public String getNoteCategory() {
        return noteCategory;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public String getNoteCreatedDate() {
        return noteCreatedDate;
    }

    public Date getNoteCreatedDateInDateFormat() throws ParseException {
        Date date = (Date) new SimpleDateFormat("dd/MM/yyyy").parse(noteCreatedDate);
        return date;
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
