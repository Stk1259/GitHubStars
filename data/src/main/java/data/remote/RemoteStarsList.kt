package data.remote


import domain.data.StarList
import java.time.LocalDate

data class RemoteStarsList(
    override val starsInPeriod: MutableMap<Int, MutableList<domain.data.FormattedStar>>,
    override val legend: List<LocalDate>,
): StarList
