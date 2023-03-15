package data.remote

import java.time.LocalDate

data class GraphRemoteList(
    val starsInPeriod: Map<Int, MutableList<FormattedGraphData>>,
    val legend: List<LocalDate>,
)
