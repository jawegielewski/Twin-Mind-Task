package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.jawegiel.twinmindjakubwegielewski.model.CalendarEvent
import pl.jawegiel.twinmindjakubwegielewski.repository.RepositoryCalendarEvents
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import java.time.ZonedDateTime

class ViewModelCalendar(application: Application) : AndroidViewModel(application) {

    companion object {
        const val PLUS_DAYS_VALUE = 1L
    }

    private val repositoryCalendarEvents by lazy { RepositoryCalendarEvents(application) }

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean>
        get() = _showLoader

    private val _calendarEvents = MutableLiveData(LinkedHashSet<CalendarEvent>())
    val calendarEvents: LiveData<LinkedHashSet<CalendarEvent>>
        get() = _calendarEvents

    var date: ZonedDateTime

    init {
        date = ZonedDateTime.now()
        updateCalendarEvents()
    }

    fun updateCalendarEvents() {
        _showLoader.value = true

        val dateEnd = date.plusDays(PLUS_DAYS_VALUE)
        val newEvents = repositoryCalendarEvents.getEvents(date, dateEnd)
        if (newEvents.isEmpty()) {
            date = dateEnd
            updateCalendarEvents()
        } else {
            _calendarEvents.value = (_calendarEvents.value!! + newEvents) as LinkedHashSet<CalendarEvent>
            date = dateEnd
        }
    }

    fun clearLoader() {
        _showLoader.value = false
    }

    fun getCalendarEventsMap(calendarEvents: LinkedHashSet<CalendarEvent>): LinkedHashMap<String, List<CalendarEvent>> {
        var distinctDates = UtilsDate.getCalendarEventsDistinctDates(calendarEvents)
        val calendarEventsMap = LinkedHashMap<String, List<CalendarEvent>>()
        for (i in 0..<distinctDates.size) {
            calendarEventsMap.put(UtilsDate.fromJulianToString(distinctDates.elementAt(i).beginDay), calendarEvents.filter { o -> o.beginDay == distinctDates.elementAt(i).beginDay })
        }
        return calendarEventsMap
    }
}