package domain.data

import java.io.Serializable
import java.time.LocalDate

interface Star: Serializable {
    val date: LocalDate
}