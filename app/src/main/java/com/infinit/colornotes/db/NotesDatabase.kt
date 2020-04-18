package com.infinit.colornotes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinit.colornotes.model.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NotesDatabase : RoomDatabase(){
    abstract fun notesDao():NotesDao
}