package data.remote

import com.squareup.moshi.Json

data class UserRemoteData(
    @field:Json(name = "login") val login: String,
    @field:Json(name = "avatar_url") val avatarUrl: String
)