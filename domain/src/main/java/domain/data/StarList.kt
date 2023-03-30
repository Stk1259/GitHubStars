package domain.data

import java.time.LocalDate

interface StarList {
    val starsInPeriod: MutableMap<Int, MutableList<FormattedStar>>
    val legend: List<LocalDate>
}
