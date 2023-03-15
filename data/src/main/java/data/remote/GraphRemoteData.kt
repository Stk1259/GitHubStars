package data.remote

import com.squareup.moshi.Json

data class GraphRemoteData(
    @field:Json (name = "starred_at") var starDate: String,
    @field:Json (name = "user") val user: UserRemoteData
)
