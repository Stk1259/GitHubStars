package com.example.githubstars.data.database

import com.squareup.moshi.Json

data class GitHubRepoData(
    @Json(name = "name") val name: String,
    @Json(name = "stargazers_count") val stargazersCount: Int,
    )