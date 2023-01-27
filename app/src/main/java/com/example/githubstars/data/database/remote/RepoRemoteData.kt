package com.example.githubstars.data.database.remote

import com.squareup.moshi.Json

data class RepoRemoteData(
    @field:Json(name = "name") val userName: String,
    @field:Json(name = "stargazers_count") val stargazersCount: Int,
    )
