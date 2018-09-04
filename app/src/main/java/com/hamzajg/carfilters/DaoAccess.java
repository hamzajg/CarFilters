package com.hamzajg.carfilters;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleMovie (Note note);
    @Insert
    void insertMultipleMovies (List<Note> noteList);
    @Query("SELECT * FROM Note WHERE fileName = :fileName")
    List<Note> fetchAllNotesByFileName(String fileName);
    @Update
    void updateNote (Note note);
    @Delete
    void deleteNote (Note note);
}