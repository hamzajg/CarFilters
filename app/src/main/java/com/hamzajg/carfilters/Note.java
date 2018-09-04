package com.hamzajg.carfilters;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class Note {

    @NonNull
    @PrimaryKey
    private String uUID;
    @NonNull
    private String fileName;
    private int x;
    private int y;
    private String noteString;

    public Note(@NonNull String fileName, int x, int y, String noteString) {
        uUID = UUID.randomUUID().toString();
        this.fileName = fileName;
        this.x = x;
        this.y = y;
        this.noteString = noteString;
    }

    @NonNull
    public String getUUID() {
        return uUID;
    }

    public void setUUID(@NonNull String uUID) {
        this.uUID = uUID;
    }

    @NonNull
    public String getFileName() {
        return fileName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNoteString() {
        return noteString;
    }
}
