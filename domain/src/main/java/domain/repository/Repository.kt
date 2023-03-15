package domain.repository

import domain.entity.GraphList
import domain.entity.RepoList
import domain.entity.period_state.PeriodState
import java.time.LocalDate

interface Repository {
    suspend fun getRepoData(userName: String, pageList: Int): RepoList
    suspend fun getGraphData(
        graphButtonState: PeriodState,
        userName: String,
        repoName: String,
        stargazersCount: Int,
        comparableDate: LocalDate,
    ): GraphList
}







