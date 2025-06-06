package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.model.Question
import pl.jawegiel.twinmindjakubwegielewski.repository.RepositoryDatabase
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import java.time.ZonedDateTime

class ViewModelQuestionsAllMeetings(application: Application) : AndroidViewModel(application) {

    companion object {
        const val MINUS_HOURS_VALUE = 2L
        const val MAX_RECURSIVE_CALLS = 7
    }

    private val repositoryDatabase by lazy { RepositoryDatabase(application) }

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean>
        get() = _showLoader

    private val _questions = MutableLiveData<List<Question>>(ArrayList())
    val questions: LiveData<List<Question>>
        get() = _questions

    private val _questions2 = MutableLiveData(LinkedHashSet<Question>())
    val questions2: LiveData<LinkedHashSet<Question>>
        get() = _questions2


    private val _totalRecursiveCalls = MutableLiveData<Int>(0)
    val totalRecursiveCallsLiveData: LiveData<Int>
        get() = _totalRecursiveCalls

    private var totalRecursiveCalls = 0

    lateinit var date: ZonedDateTime

    init {
        viewModelScope.launch {
            date = ZonedDateTime.now()
            updateQuestions(false)
        }
    }

    suspend fun updateQuestions(isClearingQuestions: Boolean) {
        if (repositoryDatabase.getAllQuestions().isNotEmpty()) {
            date = ZonedDateTime.now()
            if (isClearingQuestions) {
                _questions.value = ArrayList<Question>(ArrayList())
                _questions2.value = LinkedHashSet<Question>()
            }
            updateQuestions()
        }
    }

    suspend fun updateQuestions() {
        _showLoader.value = true

        val dateStart = date.minusHours(MINUS_HOURS_VALUE)
        val newQuestions = repositoryDatabase.getAllQuestionsWithinDatesReversed(dateStart, date)
        date = dateStart
        if (newQuestions.isEmpty()) {
            totalRecursiveCalls++
            _totalRecursiveCalls.value = totalRecursiveCalls
            if (totalRecursiveCalls < MAX_RECURSIVE_CALLS) {
                updateQuestions()
            }
        } else {
            _questions.value = questions.value!! + newQuestions
            _questions2.value = (questions2.value!! + newQuestions) as LinkedHashSet<Question>
        }

        _showLoader.value = false
    }

    fun clearRecursiveValues() {
        totalRecursiveCalls = 0
        _totalRecursiveCalls.value = 0
    }

    fun getQuestionsMap(questions: LinkedHashSet<Question>): LinkedHashMap<String, List<Question>> {
        var distinctDates = UtilsDate.getQuestionsDistinctDates(questions)
        val questionsMap = LinkedHashMap<String, List<Question>>()
        for (i in 0..<distinctDates.size) {
            questionsMap.put(UtilsDate.getDateOfReadableFormat(distinctDates.elementAt(i).startDate), questions.filter { o -> UtilsDate.getDateOfReadableFormat(o.startDate) == UtilsDate.getDateOfReadableFormat(distinctDates.elementAt(i).startDate) })
        }
        return questionsMap
    }
}