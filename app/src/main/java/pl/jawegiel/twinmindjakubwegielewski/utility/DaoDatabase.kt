package pl.jawegiel.twinmindjakubwegielewski.utility

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.model.Question

@Dao
interface DaoDatabase {

    @Insert()
    suspend fun insert(question: Question)

    @Insert()
    suspend fun insert(note: Note): Long

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun findNoteById(id: Long?): Note?

    @Query("SELECT * FROM questions ORDER BY id ASC")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT * FROM notes ORDER BY id ASC")
    suspend fun getAllMemories(): List<Note>

    @Query("SELECT * FROM notes WHERE startDate BETWEEN :start AND :end ORDER BY id ASC")
    suspend fun getAllMemoriesWithinDates(start: String, end: String): List<Note>

    @Query("SELECT * FROM questions WHERE startDate BETWEEN :start AND :end ORDER BY id ASC")
    suspend fun getAllQuestionsWithinDates(start: String, end: String): List<Question>

    @Query("update notes SET text = :text WHERE id = :id")
    suspend fun updateNoteText(id: Long, text: String)
}