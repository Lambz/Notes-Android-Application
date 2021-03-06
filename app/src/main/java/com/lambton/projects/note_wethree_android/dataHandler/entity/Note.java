package com.lambton.projects.note_wethree_android.dataHandler.entity;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


@Entity(tableName = "note_data")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int noteCategoryId;
    @NonNull
    private String noteTitle;
    private String noteDescription;
    @NonNull
    private Date noteCreatedDate;
    private String noteAudio;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] noteImage;
    private double noteLongitude;
    private double noteLatitude;

//    constructors

    public Note(String noteTitle, String noteDescription, int noteCategoryId) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;

        Calendar calendar = Calendar.getInstance();
        this.noteCreatedDate = calendar.getTime();
        this.noteCategoryId = noteCategoryId;
    }

//    setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNoteCreatedDate(@NonNull Date noteCreatedDate) {
        this.noteCreatedDate = noteCreatedDate;
    }

    public void setNoteCategoryId(int noteCategoryId) {
        this.noteCategoryId = noteCategoryId;
    }

    public void setNoteAudio(String noteAudio) {
        this.noteAudio = noteAudio;
    }

    public void setNoteImage(byte[] noteImage) {
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

    public int getNoteCategoryId() {
        return noteCategoryId;
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

    public byte[] getNoteImage() {
        return noteImage;
    }

    public double getNoteLongitude() {
        return noteLongitude;
    }

    public double getNoteLatitude() {
        return noteLatitude;
    }

//    custom getters and setters for image datatypes

//    custom getters

    public Bitmap getNoteImageAsBitmap() {
        return ConvertDatatypes.convertByteArrayToBitmap(getNoteImage());
    }

    public void setNoteImageAsBitmap(Bitmap bitmap) {
        setNoteImage(ConvertDatatypes.convertBitmapToByteArray(bitmap));
    }
}
