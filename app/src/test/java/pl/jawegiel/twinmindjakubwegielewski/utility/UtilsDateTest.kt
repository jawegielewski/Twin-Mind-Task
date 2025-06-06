package pl.jawegiel.twinmindjakubwegielewski.utility

import org.junit.Test

class UtilsDateTest {

    @Test
    fun `getCalendarEventsDistinctDates empty input`() { // Test with an empty LinkedHashSet of CalendarEvent. Expect an empty LinkedHashSet."
    }

    @Test
    fun `getCalendarEventsDistinctDates no duplicate dates`() { // Test with a LinkedHashSet of CalendarEvent where all events have distinct 'beginDay' values. Expect the original set."
    }

    @Test
    fun `getCalendarEventsDistinctDates some duplicate dates`() { // Test with a LinkedHashSet of CalendarEvent where some events share the same 'beginDay'. Expect a set with only one event per distinct 'beginDay'."
    }

    @Test
    fun `getCalendarEventsDistinctDates all duplicate dates`() { // Test with a LinkedHashSet of CalendarEvent where all events have the same 'beginDay'. Expect a set with only one event."
    }

    @Test
    fun `getCalendarEventsDistinctDates different times same date`() { // Test with CalendarEvents that have the same date but different times. Expect only one event for that date as 'fromJulian' likely truncates time."
    }

    @Test
    fun `getNotesDistinctDates empty input`() { // Test with an empty LinkedHashSet of Note. Expect an empty LinkedHashSet."
    }

    @Test
    fun `getNotesDistinctDates no duplicate dates`() { // Test with a LinkedHashSet of Note where all notes have distinct 'startDate' values (after formatting). Expect the original set (potentially with formatted dates)."
    }

    @Test
    fun `getNotesDistinctDates some duplicate dates`() { // Test with a LinkedHashSet of Note where some notes share the same 'startDate' (after formatting). Expect a set with only one note per distinct formatted 'startDate'."
    }

    @Test
    fun `getNotesDistinctDates all duplicate dates`() { // Test with a LinkedHashSet of Note where all notes have the same 'startDate' (after formatting). Expect a set with only one note."
    }

    @Test
    fun `getNotesDistinctDates date format consistency`() { // Test with notes having 'startDate' in different valid formats that should normalize to the same date. The 'getDateOfReadableFormat' should handle this."
    }

    @Test
    fun `getNotesDistinctDates invalid date format`() { // Test how the function handles notes with 'startDate' in an invalid format. Observe the behavior of 'getDateOfReadableFormat' and subsequent distinct operation. (Note: Current implementation seems to ignore the result of `o.startDate == getDateOfReadableFormat(o.startDate)`)."
    }

    @Test
    fun `getQuestionsDistinctDates empty input`() { // Test with an empty LinkedHashSet of Question. Expect an empty LinkedHashSet."
    }

    @Test
    fun `getQuestionsDistinctDates no duplicate dates`() { // Test with a LinkedHashSet of Question where all questions have distinct 'startDate' values (after formatting). Expect the original set (potentially with formatted dates)."
    }

    @Test
    fun `getQuestionsDistinctDates some duplicate dates`() { // Test with a LinkedHashSet of Question where some questions share the same 'startDate' (after formatting). Expect a set with only one question per distinct formatted 'startDate'."
    }

    @Test
    fun `getQuestionsDistinctDates all duplicate dates`() { // Test with a LinkedHashSet of Question where all questions have the same 'startDate' (after formatting). Expect a set with only one question."
    }

    @Test
    fun `getQuestionsDistinctDates date format consistency`() { // Test with questions having 'startDate' in different valid formats that should normalize to the same date. The 'getDateOfReadableFormat' should handle this."
    }

    @Test
    fun `getQuestionsDistinctDates invalid date format`() { // Test how the function handles questions with 'startDate' in an invalid format. Observe the behavior of 'getDateOfReadableFormat' and subsequent distinct operation. (Note: Current implementation seems to ignore the result of `o.startDate == getDateOfReadableFormat(o.startDate)`)."
    }

    @Test
    fun `fromJulian valid Modified Julian Day`() { // Test with a known Modified Julian Day number and verify the corresponding epoch milliseconds. Example: MJD 0 (November 17, 1858)."
    }

    @Test
    fun `fromJulian large Modified Julian Day`() { // Test with a large valid Modified Julian Day number."
    }

    @Test
    fun `fromJulian zero Modified Julian Day`() { // Test with MJD 0."
    }

    @Test
    fun `fromJulian negative Modified Julian Day`() { // Test with a negative Modified Julian Day number, if supported by JulianFields.MODIFIED_JULIAN_DAY."
    }

    @Test
    fun `fromJulianToString valid Julian Day`() { // Test with a known Julian Day number and verify the formatted string output (e.g., 'Mon, Jan 01' for a corresponding JDN)."
    }

    @Test
    fun `fromJulianToString different locales`() { // Test how the function behaves with different default locales, though the code uses Locale.getDefault(). Consider if explicit locale testing is needed."
    }

    @Test
    fun `fromJulianToString Julian Day for epoch start`() { // Test with the JDN corresponding to January 1, 1970."
    }

    @Test
    fun `fromJulianToString very large Julian Day`() { // Test with a very large Julian Day number to check for potential overflow or formatting issues."
    }

    @Test
    fun `convertToTime valid epoch milliseconds`() { // Test with known epoch milliseconds and verify the HH:mm formatted time string."
    }

    @Test
    fun `convertToTime zero epoch milliseconds`() { // Test with 0L, expecting '00:00' or equivalent based on timezone (though SimpleDateFormat defaults to system timezone)."
    }

    @Test
    fun `convertToTime milliseconds for noon`() { // Test with epoch milliseconds representing 12:00 PM."
    }

    @Test
    fun `convertToTime milliseconds for midnight`() { // Test with epoch milliseconds representing 00:00 AM."
    }

    @Test
    fun `getCurrentDateTimeForDirectoryName format check`() { // Verify the output string matches the 'yyyy_MM_dd__HH_mm_ss' format."
    }

    @Test
    fun `getCurrentDateTimeForDirectoryName consistency`() { // Call the function multiple times in quick succession and check if the generated names are lexicographically sortable by time."
    }

    @Test
    fun `getCurrentDateTime format check`() { // Verify the output string matches the 'yyyy-dd-MM HH:mm:ss' format."
    }

    @Test
    fun `getCurrentDateTime consistency`() { // Call the function multiple times and check for expected changes in seconds/minutes."
    }

    @Test
    fun `convertZonedDateTimeString valid ZonedDateTime`() { // Test with a valid ZonedDateTime object and check if the output string matches 'yyyy-dd-MM HH:mm:ss' format."
    }

    @Test
    fun `convertZonedDateTimeString different timezones`() { // Test with ZonedDateTime objects in different timezones. The output should be in the local time representation of that ZonedDateTime, formatted as specified."
    }

    @Test
    fun `convertZonedDateTimeString UTC ZonedDateTime`() { // Test with a ZonedDateTime in UTC."
    }

    @Test
    fun `getDateOfReadableFormat valid date string`() { // Test with a date string in 'yyyy-dd-MM HH:mm:ss' format. Verify the output is in 'E, MMM dd' format."
    }

    @Test
    fun `getDateOfReadableFormat different valid inputs`() { // Test with various dates (e.g., start of month, end of month, leap year) to ensure correct day of week and month abbreviation."
    }

    @Test
    fun `getDateOfReadableFormat invalid date string`() { // Test with an invalid date string. Expect a ParseException to be caught internally, and the method might return an empty string or throw an exception if not handled (current code uses `date!!` which would throw NPE if parse fails and returns null)."
    }

    @Test
    fun `getDateOfReadableFormat different locales`() { // Test how the function behaves with different default locales for month and day names."
    }

    @Test
    fun `getTimeDifference positive difference`() { // Test with endDateString later than startDateString. Expect a positive 'Xm Ys' output."
    }

    @Test
    fun `getTimeDifference zero difference`() { // Test with startDateString equal to endDateString. Expect '0m 0s'."
    }

    @Test
    fun `getTimeDifference negative difference`() { // Test with startDateString later than endDateString. Expect negative minutes (e.g., '-5m 30s' or similar, depending on ChronoUnit behavior for negative durations)."
    }

    @Test
    fun `getTimeDifference across midnight`() { // Test with startDateString on one day and endDateString on the next."
    }

    @Test
    fun `getTimeDifference large difference`() { // Test with a difference of several hours or days."
    }

    @Test
    fun `getTimeDifference invalid date strings`() { // Test with one or both date strings in an invalid format. Expect a ParseException (current code uses `!!` which would lead to NPE if parse returns null)."
    }

    @Test
    fun `getDateAfterAddingMinutesAndSecs positive minutes and seconds`() { // Test adding positive minutes and seconds to a valid dateTime string. Verify the output format and correctness of the new time."
    }

    @Test
    fun `getDateAfterAddingMinutesAndSecs zero minutes and seconds`() { // Test adding 0 minutes and 0 seconds. Expect the original dateTime (reformatted)."
    }

    @Test
    fun `getDateAfterAddingMinutesAndSecs negative minutes and seconds`() { // Test adding negative minutes and seconds (subtracting time). Verify correctness."
    }

    @Test
    fun `getDateAfterAddingMinutesAndSecs crossing hour or day or month or year boundary`() { // Test scenarios where adding time crosses midnight, end of month, or end of year."
    }

    @Test
    fun `getDateAfterAddingMinutesAndSecs large minutes and seconds`() { // Test adding a large number of minutes (e.g., > 60) and seconds (e.g., > 60)."
    }

    @Test
    fun `getDateAfterAddingMinutesAndSecs invalid dateTime string`() { // Test with an invalid dateTime string. Expect the catch block to be hit and an empty string to be returned."
    }

    @Test
    fun `getDateAfterAddingMinutesAndSecs specific time output format`() { // The code calculates `output` using `hh:mm a` but returns `output2` using `yyyy-dd-MM HH:mm:ss`. This test should verify the returned `output2` format specifically."
    }

    @Test
    fun `getTimeFromDate valid dateTime string AM`() { // Test with a valid dateTime string representing an AM time. Verify the 'hh:mm a' output."
    }

    @Test
    fun `getTimeFromDate valid dateTime string PM`() { // Test with a valid dateTime string representing a PM time. Verify the 'hh:mm a' output."
    }

    @Test
    fun `getTimeFromDate midnight`() { // Test with a dateTime string for '00:00:00'. Expect '12:00 AM'."
    }

    @Test
    fun `getTimeFromDate noon`() { // Test with a dateTime string for '12:00:00'. Expect '12:00 PM'."
    }

    @Test
    fun `getTimeFromDate invalid dateTime string`() { // Test with an invalid dateTime string. Expect the catch block to be hit and an empty string to be returned."
    }


}