package pl.jawegiel.twinmindjakubwegielewski.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
                @ColumnInfo(name = "text") var text: String = "",
                @ColumnInfo(name = "startDate") var startDate: String,
                @ColumnInfo(name = "endDate") var endDate: String) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false
        if (text != other.text) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        return result
    }
}