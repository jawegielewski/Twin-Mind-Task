package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerateContentResponse
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ServerException
import com.google.firebase.ai.type.content
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.repository.RepositoryDatabase
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import java.time.ZonedDateTime

private const val TAG = "ViewModelMemories"

class ViewModelMemories(application: Application) : AViewModelTranscriptionChat(application) {

    companion object {
        const val MINUS_HOURS_VALUE = 2L
        const val MAX_RECURSIVE_CALLS = 7
    }

    private val repositoryDatabase by lazy { RepositoryDatabase(application) }
    private val model: GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.0-flash")

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean>
        get() = _showLoader

    private val _memories = MutableLiveData<List<Note>>(ArrayList())
    val memories: LiveData<List<Note>>
        get() = _memories

    private val _memories2 = MutableLiveData(LinkedHashSet<Note>())
    val memories2: LiveData<LinkedHashSet<Note>>
        get() = _memories2

    private val _totalRecursiveCalls = MutableLiveData<Int>(0)
    val totalRecursiveCallsLiveData: LiveData<Int>
        get() = _totalRecursiveCalls

    private var totalRecursiveCalls = 0

    lateinit var date: ZonedDateTime

    init {
        viewModelScope.launch {
            date = ZonedDateTime.now()
            updateMemories(false)
        }
    }

    suspend fun updateMemories(isClearingMemories: Boolean) {
        if (repositoryDatabase.getAllQuestions().isNotEmpty()) {
            date = ZonedDateTime.now()
            if (isClearingMemories) {
                _memories.value = ArrayList<Note>(ArrayList())
                _memories2.value = LinkedHashSet<Note>()
            }
            updateMemories()
        }
    }

    suspend fun updateMemories() {
        _showLoader.value = true

        val dateStart = date.minusHours(MINUS_HOURS_VALUE)
        val newMemories = repositoryDatabase.getAllMemoriesWithinDatesReversed(dateStart, date)
        date = dateStart
        if (newMemories.isEmpty()) {
            totalRecursiveCalls++
            _totalRecursiveCalls.value = totalRecursiveCalls
            if (totalRecursiveCalls < MAX_RECURSIVE_CALLS) {
                updateMemories()
            }
        } else {
            _memories.value = memories.value!! + newMemories
            _memories2.value = (memories2.value!! + newMemories) as LinkedHashSet<Note>
        }

        _showLoader.value = false
    }

    fun clearRecursiveValues() {
        totalRecursiveCalls = 0
        _totalRecursiveCalls.value = 0
    }

    override suspend fun generateContentChatBased(textFieldText: String,
                                                  onResponse: (GenerateContentResponse?) -> Unit) {

        val knowledgeBase = repositoryDatabase.getAllMemories().map { o -> o.text }
        val prompt = content {
            inlineData(knowledgeBase.toString().toByteArray(), "text/plain")
            text("Given a knowledge base attached to a prompt as a byte array, answer to this request: $textFieldText")
        }

        var response: GenerateContentResponse? = null
        try {
            response = model.generateContent(prompt)
        } catch (ex: ServerException) {
            Log.e(TAG, "An Exception occurred during generating content: ${ex.message}")
        }

        onResponse(response)
    }

    override suspend fun generateContentChatBased(text: String) {
        generateContentChatBased(text, onResponse = { response ->
            mutChatBasedResponse.value = response?.text
                ?: "Result not available :(. Try to ask another question."
        })
    }

    fun getMemoriesMap(memories: LinkedHashSet<Note>): LinkedHashMap<String, List<Note>> {
        var distinctDates = UtilsDate.getNotesDistinctDates(memories)
        val memoriesMap = LinkedHashMap<String, List<Note>>()
        for (i in 0..<distinctDates.size) {
            memoriesMap.put(UtilsDate.getDateOfReadableFormat(distinctDates.elementAt(i).startDate), memories.filter { o -> UtilsDate.getDateOfReadableFormat(o.startDate) == UtilsDate.getDateOfReadableFormat(distinctDates.elementAt(i).startDate) })
        }
        return memoriesMap
    }
}