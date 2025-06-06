package pl.jawegiel.twinmindjakubwegielewski.utility

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.model.Question

@Database(entities = [Question::class, Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DaoDatabase
}