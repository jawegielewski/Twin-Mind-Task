package pl.jawegiel.twinmindjakubwegielewski.repository

import android.content.Context
import androidx.room.Room
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.model.Question
import pl.jawegiel.twinmindjakubwegielewski.utility.AppDatabase
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import java.time.ZonedDateTime

class RepositoryDatabase(context: Context) {

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "twinmind_database").build()
    private val databaseDao = db.databaseDao()

    suspend fun insert(question: Question) {
        databaseDao.insert(question)
    }

    suspend fun insert(note: Note): Long {
        return databaseDao.insert(note)
    }

    suspend fun findNoteById(id: Long): Note? {
        return databaseDao.findNoteById(id)
    }

    suspend fun updateText(id: Long, text: String) {
        databaseDao.updateNoteText(id, text)
    }

    suspend fun getAllQuestions(): List<Question> {
        return databaseDao.getAllQuestions()
    }

    suspend fun getAllMemories(): List<Note> {
        return databaseDao.getAllMemories()
    }

    suspend fun getAllMemoriesWithinDatesReversed(start: ZonedDateTime, end: ZonedDateTime): LinkedHashSet<Note> {
        val startString = UtilsDate.convertZonedDateTimeString(start)
        val endString = UtilsDate.convertZonedDateTimeString(end)
        val list = databaseDao.getAllMemoriesWithinDates(startString!!, endString!!).reversed()

        return LinkedHashSet<Note>(list)
    }

    suspend fun getAllQuestionsWithinDatesReversed(start: ZonedDateTime, end: ZonedDateTime): LinkedHashSet<Question> {
        val startString = UtilsDate.convertZonedDateTimeString(start)
        val endString = UtilsDate.convertZonedDateTimeString(end)
        val list = databaseDao.getAllQuestionsWithinDates(startString!!, endString!!).reversed()

        return LinkedHashSet<Question>(list)
    }
}