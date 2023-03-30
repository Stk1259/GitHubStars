package data.remote

import com.squareup.moshi.Json
import domain.data.Repo

data class RemoteRepo(
    @field:Json(name = "name") override val repoName: String,
    @field:Json(name = "stargazers_count") override val stargazersCount: Int,
    @field:Json(name = "id")override val id: Int,
    override var favouriteStatus: Boolean = false
): Repo
