package domain.entity

import java.time.LocalDate

data class GraphList(
    val starsInPeriod: Map<Int, MutableList<GraphData>>,
    val legend: List<LocalDate>,
)
