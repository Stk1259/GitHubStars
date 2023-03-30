package data.repository

import android.content.Context
import androidx.room.Room
import data.api.apiService
import data.converter.StarDateConverter
import data.database_module.AppDatabase
import data.entity.*
import data.remote.FormattedRemoteStar
import domain.data.*
import domain.data.period_state.PeriodState
import domain.repository.Repository
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import kotlin.math.ceil


object DataRepository : Repository {
    private const val PER_PAGE = 100
    private var starData = mutableListOf<FormattedRemoteStar>()
    private val converter = StarDateConverter
    private var lastRepoName = ""
    private var additionalStars = mutableListOf<FormattedRemoteStar>()
    private var repoData = mutableListOf<Repo>()
    private var repoId = 0
    private var mapGraphPeriods = mapOf(
        PeriodState.YEAR to Year,
        PeriodState.SEASON to Seasons,
        PeriodState.MONTH to Month,
        PeriodState.WEEK to Week
    )

    override suspend fun getRepoData(userName: String, pageList: Int, context: Context): RepoList {
        fun Repo.toEntity(starredAt: Long) =
            RepoEntity(id, repoName, stargazersCount, userName, false, starredAt)

        val db by lazy {
            Room.databaseBuilder(context, AppDatabase::class.java, "repo_database").build()
        }
        kotlin.runCatching {
            repoData = apiService.getRepos(userName, pageList, PER_PAGE).toMutableList()
        }.onSuccess {
                repoData.forEach { db.repoDao().insert(it.toEntity(System.currentTimeMillis())) }
            }.onFailure {
                repoData = db.repoDao().getRepo(userName).toMutableList()
            }
        val nextPageStatus = repoData.size == PER_PAGE
        val favouriteRepos = db.favouriteRepoDao().getRepoByUsername(userName)
        favouriteRepos.forEach { favourite ->
            repoData.forEach {
                if (favourite.id == it.id) {
                    val index = repoData.indexOf(it)
                    repoData[index] = favourite
                }
            }
        }
        return RepoList(repoData, nextPageStatus)
    }

    override suspend fun getGraphData(
        context: Context,
        graphButtonState: PeriodState,
        userName: String,
        repoName: String,
        stargazersCount: Int,
        comparableDate: LocalDate,
    ): StarList {
        val db by lazy {
            Room.databaseBuilder(context, AppDatabase::class.java, "repo_database").build()
        }
        repoId = try {
            db.favouriteRepoDao().getRepoId(repoName).id
        } catch (e: Exception) {
            db.repoDao().getRepoId(repoName).id
        }
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
            kotlin.runCatching {
                additionalStars = converter.convertDate(
                    apiService.getStars(
                        userName, repoName, pageNext, PER_PAGE
                    )
                ).toMutableList()
            }.onSuccess {
                    db.userDao().insert(additionalStars.map { it.toUserEntity() })
                    db.starDao().insert(additionalStars.map { it.toStarEntity() })
                }.onFailure {
                    additionalStars =
                        db.stargazerDao().getStargazers(repoName).map { it.toFormattedGraphData() }
                            .toMutableList()
                }
            starData.retainAll { it.date < additionalStars[0].date }
            starData += additionalStars
        }
        if (additionalStars.size == PER_PAGE) {
            pageNext += 1
            kotlin.runCatching {
                additionalStars = converter.convertDate(
                    apiService.getStars(
                        userName, repoName, pageNext, PER_PAGE
                    )
                ).toMutableList()
            }.onSuccess {
                    db.userDao().insert(additionalStars.map { it.toUserEntity() })
                    db.starDao().insert(additionalStars.map { it.toStarEntity() })
                }.onFailure {
                    additionalStars =
                        db.stargazerDao().getStargazers(repoName).map { it.toFormattedGraphData() }
                            .toMutableList()
                }
            starData.addAll(additionalStars)
        }

        pageBefore -= 1
        if (starData.isNotEmpty()) {
            while ((starData[0].date.year >= comparableDate.year) && pageBefore > 0) {
                kotlin.runCatching {
                    additionalStars = converter.convertDate(
                        apiService.getStars(
                            userName, repoName, pageNext, PER_PAGE
                        )
                    ).toMutableList()
                }.onSuccess {
                        db.userDao().insert(additionalStars.map { it.toUserEntity() })
                        db.starDao().insert(additionalStars.map { it.toStarEntity() })
                    }.onFailure {
                        additionalStars = db.stargazerDao().getStargazers(repoName)
                            .map { it.toFormattedGraphData() }.toMutableList()
                    }
                starData = (additionalStars + starData).toMutableList()
                pageBefore -= 1
            }
        }
        return mapGraphPeriod.filterStars(comparableDate, starData)
    }

    override suspend fun addFavouriteRepo(
        context: Context,
        repoName: String,
    ) {
        val db by lazy {
            Room.databaseBuilder(context, AppDatabase::class.java, "repo_database").build()
        }
        fun RepoEntity.toFavourite() =
            FavouriteRepoEntity(id, repoName, stargazersCount, userName, true, createdAt)
        val selectedRepo = db.repoDao().getRepoId(repoName)
        db.favouriteRepoDao().insert(selectedRepo.toFavourite())

    }

    override suspend fun deleteFavouriteRepo(context: Context, id: Int) {
        val db by lazy {
            Room.databaseBuilder(context, AppDatabase::class.java, "repo_database").build()
        }
        db.favouriteRepoDao().deleteRepo(id)
    }

    override suspend fun getNotificationsData(context: Context): List<FavouriteRepos> {
        val reposForPush = mutableListOf<FavouriteRepos>()
        val db by lazy {
            Room.databaseBuilder(context, AppDatabase::class.java, "repo_database").build()
        }
        val favouriteRepos = db.favouriteRepoDao().getAllRepo()
        favouriteRepos.forEach {
            val additionalStars = apiService.getExactRepo(it.userName, it.repoName)
            reposForPush.add(
                FavouriteRepos(
                    it.id,
                    it.repoName,
                    it.stargazersCount,
                    additionalStars.stargazersCount - it.stargazersCount,
                    it.userName,
                    it.favouriteStatus
                )
            )
            db.favouriteRepoDao().insert(
                FavouriteRepoEntity(
                    it.id,
                    it.repoName,
                    additionalStars.stargazersCount,
                    it.userName,
                    it.favouriteStatus,
                    it.createdAt
                )
            )
        }
        return reposForPush
    }

    override suspend fun checkFavouriteRepos(context: Context): Boolean {
        val db by lazy {
            Room.databaseBuilder(context, AppDatabase::class.java, "repo_database").build()
        }
        return db.favouriteRepoDao().getAllRepo().isNotEmpty()
    }

    private fun FormattedRemoteStar.toUserEntity() = UserEntity(user.id, user.name, user.avatarUrl)
    private fun FormattedRemoteStar.toStarEntity() = StarEntity(date, user.id, repoId)
    private fun StargazersEntity.toFormattedGraphData() =
        FormattedRemoteStar(starEntity.date, userEntity)
}