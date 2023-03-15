package data.repository

import data.api.apiService
import data.converter.StarDateConverter
import data.remote.FormattedGraphData
import data.remote.GraphRemoteList
import data.remote.RepoRemoteData
import data.remote.UserRemoteData
import domain.entity.*
import domain.entity.period_state.PeriodState
import domain.repository.Repository
import java.time.LocalDate
import kotlin.math.ceil


object DataRepository : Repository {
    private const val PER_PAGE = 100
    private var starData: MutableList<FormattedGraphData> = mutableListOf()
    private val converter = StarDateConverter
    private var lastRepoName = ""
    private var newestStars = mutableListOf<FormattedGraphData>()
    private var mapGraphPeriods = mapOf(
        PeriodState.YEAR to Year,
        PeriodState.SEASON to Seasons,
        PeriodState.MONTH to Month,
        PeriodState.WEEK to Week
    )

    override suspend fun getRepoData(userName: String, pageList: Int): RepoList {
        val nextPageStatus = apiService.getRepos(
            userName, pageList,
            PER_PAGE
        ).size == PER_PAGE

        fun RepoRemoteData.toLocalData() = Repo(repoName, stargazersCount)
        val repoLocaleData: List<Repo> =
            apiService.getRepos(userName, pageList, PER_PAGE)
                .map { it.toLocalData() }
        return RepoList(repoLocaleData, nextPageStatus)
    }

    override suspend fun getGraphData(
        graphButtonState: PeriodState,
        userName: String,
        repoName: String,
        stargazersCount: Int,
        comparableDate: LocalDate,
    ): GraphList {
        val pageNumber = ceil(stargazersCount / PER_PAGE.toFloat()).toInt()
        var pageNext = pageNumber
        var pageBefore = pageNumber
        if (lastRepoName != repoName) {
            starData = mutableListOf()
            lastRepoName = repoName
        }
        val mapGraphPeriod = mapGraphPeriods.getValue(graphButtonState)
        if (starData.isEmpty() || mapGraphPeriod.checkForUpdate(
                comparableDate
            )
        ) {
            newestStars = converter.convertDate(
                apiService.getStars(
                    userName, repoName, pageNext, PER_PAGE
                )
            )
            starData.retainAll { it.starDate < newestStars[0].starDate }
            starData += newestStars
        }
        if (newestStars.size == PER_PAGE) {
            pageNext += 1
            val newestStars = converter.convertDate(
                apiService.getStars(
                    userName, repoName, pageNext, PER_PAGE
                )
            )
            starData.addAll(newestStars)
        }
        pageBefore -= 1
        if (starData.isNotEmpty()) {
            while ((starData[0].starDate.year >= comparableDate.year) && pageBefore > 0) {
                starData = (converter.convertDate(
                    apiService.getStars(
                        userName, repoName, pageBefore, PER_PAGE
                    )
                ) + starData).toMutableList()
                pageBefore -= 1
            }
        }
        val gotMap = mapGraphPeriod.filterStars(comparableDate, starData)
        fun UserRemoteData.toUser() =
            User(login, avatarUrl)

        fun FormattedGraphData.toGraphData() =
            GraphData(starDate, user.toUser())

        fun GraphRemoteList.toGraphList() = GraphList(
            gotMap.starsInPeriod.mapValues {
                it.value.map { k -> k.toGraphData() }.toMutableList()
            }, legend
        )
        return gotMap.toGraphList()
    }
}
