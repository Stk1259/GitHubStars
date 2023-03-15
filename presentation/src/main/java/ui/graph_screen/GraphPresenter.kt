package ui.graph_screen

import com.example.githubstars.R
import data.repository.DataRepository
import domain.entity.period_state.PeriodState
import kotlinx.coroutines.launch
import moxy.presenterScope
import ui.base.BasePresenter
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class GraphPresenter(
    private val userName: String,
    private val repoName: String,
    private val stargazerCount: Int,
) : BasePresenter<GraphView>(){
    companion object {
        private val periodUnitMap = mapOf(
            PeriodState.YEAR to ChronoUnit.YEARS,
            PeriodState.SEASON to ChronoUnit.YEARS,
            PeriodState.MONTH to ChronoUnit.MONTHS,
            PeriodState.WEEK to ChronoUnit.WEEKS
        )
    }
    private val repository = DataRepository
    private var comparableDate = LocalDate.now()
    private val dateMap = PeriodState.values().associateWith { comparableDate }.toMutableMap()
    private var currentPeriodState = PeriodState.YEAR
        set(value) {
            if (field != value)
                field = value
            viewState.setPeriodState(value)
        }

    fun getStarsData() = presenterScope.launch{
        viewState.setLoading(true)
            try {
                val graphResponse = repository.getGraphData(
                    currentPeriodState, userName, repoName, stargazerCount, comparableDate
                )
                viewState.setLoading(false)
                if (graphResponse.starsInPeriod.all { it.value.isEmpty() }) {
                    viewState.showMessage(R.string.empty_list)
                }
                viewState.showGraph(currentPeriodState, graphResponse)
            } catch (e: Exception) {
                val errorMessage = getErrorValue(e)
                viewState.showMessage(errorMessage)
            }
    }

    fun nextPeriod() {
        dateMap[currentPeriodState] =
            dateMap[currentPeriodState]?.plus(1, periodUnitMap[currentPeriodState])
        comparableDate = dateMap[currentPeriodState]
        getStarsData()
    }

    fun prefPeriod() {
        dateMap[currentPeriodState] =
            dateMap[currentPeriodState]?.minus(1, periodUnitMap[currentPeriodState])
        comparableDate = dateMap[currentPeriodState]
        getStarsData()
    }

    fun requestPeriodChange() {
        currentPeriodState = currentPeriodState.next()
        comparableDate = dateMap[currentPeriodState]
        getStarsData()
    }

    inline fun <reified T : Enum<T>> T.next(): T {
        val values = enumValues<T>()
        val nextOrdinal = (ordinal + 1) % values.size
        return values[nextOrdinal]
    }
}