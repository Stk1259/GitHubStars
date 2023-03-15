package data.remote

import com.squareup.moshi.Json

data class RepoRemoteData(
    @field:Json(name = "name") val repoName: String,
    @field:Json(name = "stargazers_count") val stargazersCount: Int
)
