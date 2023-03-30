package domain.repository

import android.content.Context
import domain.data.FavouriteRepos
import domain.data.RepoList
import domain.data.StarList
import domain.data.period_state.PeriodState
import java.time.LocalDate

interface Repository {
    suspend fun getRepoData(userName: String, pageList: Int, context: Context): RepoList
    suspend fun getGraphData(
        context: Context,
        graphButtonState: PeriodState,
        userName: String,
        repoName: String,
        stargazersCount: Int,
        comparableDate: LocalDate,
    ): StarList

    suspend fun addFavouriteRepo(
        context: Context,
        repoName: String,
    )

    suspend fun deleteFavouriteRepo(context: Context, id: Int)
    suspend fun getNotificationsData(context: Context): List<FavouriteRepos>
    suspend fun checkFavouriteRepos(context: Context): Boolean
}







