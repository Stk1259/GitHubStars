package data.api


import data.remote.ExactRepo
import data.remote.RemoteStar
import data.remote.RemoteRepo
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*


private const val BASE_URL = "https://api.github.com/"
val apiService: GithubApiService = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
    .create(GithubApiService::class.java)

interface GithubApiService {
    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") userName: String,
        @Query("page") pageList: Int,
        @Query("per_page") PER_PAGE: Int,
    ): List<RemoteRepo>

    @GET("/repos/{owner}/{repo}")
    suspend fun getExactRepo(
        @Path("owner") userName: String,
        @Path("repo") repoName: String
    ): ExactRepo

    @GET("repos/{owner}/{repo}/stargazers")
    suspend fun getStars(
        @Path("owner") userName: String,
        @Path("repo") repoName: String,
        @Query("page") pageAmount: Int,
        @Query("per_page") PER_PAGE: Int,
        @Header("accept") header: String = "application/vnd.github.star+json",
    ): MutableList<RemoteStar>
}


