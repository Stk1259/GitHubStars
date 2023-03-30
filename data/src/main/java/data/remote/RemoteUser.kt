package data.remote

import com.squareup.moshi.Json
import domain.data.User

data class RemoteUser(
    @field:Json(name = "login") override val name: String,
    @field:Json(name = "avatar_url") override val avatarUrl: String,
    @field:Json(name = "id") override val id: Int
): User