package pl.jawegiel.twinmindjakubwegielewski.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import pl.jawegiel.twinmindjakubwegielewski.model.CalendarEvent
import java.time.ZonedDateTime
import androidx.core.net.toUri

private const val TAG = "RepositoryCalendarEvents"
private const val PROJECTION_ID_INDEX: Int = 0
private const val PROJECTION_BEGIN_DAY_INDEX: Int = 1
private const val PROJECTION_END_DAY_INDEX: Int = 2
private const val PROJECTION_BEGIN_INDEX: Int = 3
private const val PROJECTION_END_INDEX: Int = 4
private const val PROJECTION_TITLE_INDEX: Int = 5
private val PROJECTION = arrayOf(CalendarContract.Instances.EVENT_ID, CalendarContract.Instances.START_DAY, CalendarContract.Instances.END_DAY, CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.TITLE)

class RepositoryCalendarEvents(val context: Context) {

    fun getEvents(start: ZonedDateTime, end: ZonedDateTime): LinkedHashSet<CalendarEvent> {
        val startMillis = start.toInstant().toEpochMilli()
        val endMillis = end.toInstant().toEpochMilli()


        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        val selection = "(${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTEND} <= ?) OR ${CalendarContract.Events.ALL_DAY} = 1"
        val selectionArgs: Array<String> = arrayOf(startMillis.toString(), endMillis.toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"
        val cursor: Cursor? = context.contentResolver.query(builder.build(), PROJECTION, selection, selectionArgs, sortOrder)

        return getEvents(cursor)
    }

    fun getEvents(cursor: Cursor?): LinkedHashSet<CalendarEvent> {
        val calendarEvents = LinkedHashSet<CalendarEvent>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val eventID: Long = cursor.getLong(PROJECTION_ID_INDEX)
                val beginDay: Long = cursor.getLong(PROJECTION_BEGIN_DAY_INDEX)
                val endDay: Long = cursor.getLong(PROJECTION_END_DAY_INDEX)
                val begin: Long = cursor.getLong(PROJECTION_BEGIN_INDEX)
                val end: Long = cursor.getLong(PROJECTION_END_INDEX)
                val title: String = cursor.getString(PROJECTION_TITLE_INDEX)

                val calendarEvent = CalendarEvent(eventID, beginDay, endDay, begin, end, title)
                calendarEvents.add(calendarEvent)
            }
            cursor.close()

        } else {
            Log.e(TAG, "Cannot get events. Cursor is null")
        }

        return calendarEvents
    }
}


