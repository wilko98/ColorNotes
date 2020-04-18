package com.infinit.colornotes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.infinit.colornotes.model.Note

@Dao
interface NotesDao {
    @Insert
    fun insertNote(note: Note)

    @Query("SELECT * FROM Note")
    fun getNotes():List<Note>
}