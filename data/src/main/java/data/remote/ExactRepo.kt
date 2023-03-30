package data.remote

import com.squareup.moshi.Json

data class ExactRepo(
    @field:Json(name = "stargazers_count") val stargazersCount: Int
)
