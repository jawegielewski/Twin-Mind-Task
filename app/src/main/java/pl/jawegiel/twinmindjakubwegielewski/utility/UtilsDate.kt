package pl.jawegiel.twinmindjakubwegielewski.utility

import android.util.Log
import pl.jawegiel.twinmindjakubwegielewski.model.CalendarEvent
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.model.Question
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.JulianFields
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val TAG = "UtilsDate"

class UtilsDate {

    companion object {

        fun getCalendarEventsDistinctDates(calendarEvents: LinkedHashSet<CalendarEvent>): LinkedHashSet<CalendarEvent> {
            return calendarEvents.distinctBy { o -> fromJulian(o.beginDay) }
                .toCollection(LinkedHashSet())
        }

        fun getNotesDistinctDates(notes: LinkedHashSet<Note>): LinkedHashSet<Note> {
            val notesModified = LinkedHashSet<Note>(notes)
            notesModified.forEach { o -> o.startDate == getDateOfReadableFormat(o.startDate) }
            return notesModified.map { o -> o }.distinctBy { o -> o.startDate }
                .toCollection(LinkedHashSet())
        }

        fun getQuestionsDistinctDates(notes: LinkedHashSet<Question>): LinkedHashSet<Question> {
            val questionsModified = LinkedHashSet<Question>(notes)
            questionsModified.forEach { o -> o.startDate == getDateOfReadableFormat(o.startDate) }
            return questionsModified.map { o -> o }.distinctBy { o -> o.startDate }
                .toCollection(LinkedHashSet())
        }

        fun fromJulian(jdn: Long): Long {
            val localDate = LocalDate.MIN.with(JulianFields.MODIFIED_JULIAN_DAY, jdn)
            return localDate.getLong(ChronoField.EPOCH_DAY) * 86400000
        }

        fun fromJulianToString(jdn: Long): String {
            val localDate = LocalDate.MIN.with(JulianFields.JULIAN_DAY, jdn)
            val dtf = DateTimeFormatter.ofPattern("E, MMM dd", Locale.getDefault())
            val output = dtf.format(localDate)
            return output
        }

        fun convertToTime(long: Long): String {
            val date = Date(long)
            val format: Format = SimpleDateFormat("HH:mm", Locale.getDefault())
            return format.format(date)
        }

        fun getCurrentDateTimeForDirectoryName(): String {
            return SimpleDateFormat("yyyy_MM_dd__HH_mm_ss", Locale.getDefault()).format(Calendar.getInstance()
                .getTime())
        }

        fun getCurrentDateTime(): String {
            return SimpleDateFormat("yyyy-dd-MM HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance()
                .getTime())
        }

        fun convertZonedDateTimeString(zonedDateTime: ZonedDateTime): String? {
            val dateString = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss")
                .format(zonedDateTime)
            return dateString
        }

        fun getDateOfReadableFormat(oldDateString: String): String {
            val sdf = SimpleDateFormat("yyyy-dd-MM HH:mm:ss", Locale.getDefault())
            val date = sdf.parse(oldDateString)
            sdf.applyPattern("E, MMM dd")
            return sdf.format(date!!)
        }

        fun getTimeDifference(startDateString: String, endDateString: String): String {
            val sdf = SimpleDateFormat("yyyy-dd-MM HH:mm:ss", Locale.getDefault())
            val startDate = sdf.parse(startDateString)
            val endDate = sdf.parse(endDateString)

            val zonedStartDate = ZonedDateTime.ofInstant(startDate!!.toInstant(), ZoneId.systemDefault())
            val zonedEndDate = ZonedDateTime.ofInstant(endDate!!.toInstant(), ZoneId.systemDefault())

            val minutes = ChronoUnit.MINUTES.between(zonedStartDate, zonedEndDate)
            val secondsTemp = ChronoUnit.SECONDS.between(zonedStartDate, zonedEndDate)
            val seconds = secondsTemp % 60

            return "${minutes}m ${seconds}s"
        }

        fun getDateAfterAddingMinutesAndSecs(dateTime: String, minutes: Int, seconds: Int): String {
            val sdf = SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
            val outputFormat = SimpleDateFormat("hh:mm a");
            val date: Date;
            val output: String
            var output2 = ""
            try {
                date = sdf.parse(dateTime)
                val cal = Calendar.getInstance()
                cal.setTime(date)
                cal.add(Calendar.MINUTE, minutes)
                cal.add(Calendar.SECOND, seconds)
                output = outputFormat.format(date)
                output2 = sdf.format(cal.getTime());
            } catch (e: ParseException) {
                Log.e(TAG, "An exception occurred during parsing date: ${e.message}")
            }

            return output2
        }

        fun getTimeFromDate(dateTime: String): String {
            val sdf = SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
            val outputFormat = SimpleDateFormat("hh:mm a");
            val date: Date;
            var output = ""
            try {
                date = sdf.parse(dateTime)
                output = outputFormat.format(date)
            } catch (e: ParseException) {
                Log.e(TAG, "An exception occurred during parsing date: ${e.message}")
            }

            return output
        }
    }
}