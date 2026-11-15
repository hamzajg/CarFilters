package com.hamzajg.carfilters;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleMovie (Note note);
    @Insert
    void insertMultipleMovies (List<Note> noteList);
    @Query("SELECT * FROM Note WHERE fileName = :fileName")
    List<Note> fetchAllNotesByFileName(String fileName);
    @Query("DELETE FROM Note WHERE fileName = :fileName")
    void deleteNotesByFileName(String fileName);
    @Update
    void updateNote (Note note);
    @Delete
    void deleteNote (Note note);
}