package data.converter

import androidx.room.TypeConverter
import java.time.LocalDate

object LocalDateConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return LocalDate.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.toString()
    }
}