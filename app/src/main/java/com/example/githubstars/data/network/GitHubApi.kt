package com.example.githubstars.data.network


import com.example.githubstars.data.database.GitHubRepoData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface GithubApiService {
    @GET("/repos/{login}")
    fun getUserData(@Path("repos") repos: String):
            Call<GitHubRepoData>

    object GithubApi {
        val retrofitService: GithubApiService by lazy {
            retrofit.create(GithubApiService::class.java)
        }
    }

}