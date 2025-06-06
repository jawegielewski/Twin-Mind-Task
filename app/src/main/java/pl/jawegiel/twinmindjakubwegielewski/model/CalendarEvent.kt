package pl.jawegiel.twinmindjakubwegielewski.model

data class CalendarEvent(val id: Long,
                         val beginDay: Long,
                         val endDay: Long,
                         val begin: Long,
                         val end: Long,
                         val title: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CalendarEvent

        if (id != other.id) return false
        if (beginDay != other.beginDay) return false
        if (endDay != other.endDay) return false
        if (begin != other.begin) return false
        if (end != other.end) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + beginDay.hashCode()
        result = 31 * result + endDay.hashCode()
        result = 31 * result + begin.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}

