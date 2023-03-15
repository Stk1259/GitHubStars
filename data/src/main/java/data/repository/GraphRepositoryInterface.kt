package data.repository

import data.remote.FormattedGraphData
import data.remote.GraphRemoteList
import java.time.LocalDate

interface GraphRepositoryInterface {
    fun filterStars(comparableDate: LocalDate, localStarData: MutableList<FormattedGraphData>): GraphRemoteList
    fun checkForUpdate(comparableDate: LocalDate): Boolean
}