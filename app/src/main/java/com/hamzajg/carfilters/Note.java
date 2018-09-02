package com.hamzajg.carfilters;

import android.graphics.Point;

public class Note {

    private Point point;
    private String noteString;

    public Note(Point point, String noteString) {
        this.point = point;
        this.noteString = noteString;
    }

    public Point getPoint() {
        return point;
    }

    public String getNoteString() {
        return noteString;
    }
}
