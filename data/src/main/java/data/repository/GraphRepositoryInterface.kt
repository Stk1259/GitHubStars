package data.repository

import data.remote.RemoteStarsList
import java.time.LocalDate

interface GraphRepositoryInterface {
    fun filterStars(comparableDate: LocalDate, localStarData: MutableList<data.remote.FormattedRemoteStar>): RemoteStarsList
    fun checkForUpdate(comparableDate: LocalDate): Boolean
}