package com.example.githubstars.data.api


import com.example.githubstars.data.database.remote.RepoRemoteData
import retrofit2.Retrofit

import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.github.com/"
val retrofit: GithubApiService = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
    .create(GithubApiService::class.java)

interface GithubApiService {
    @GET("users/{username}/repos")
    suspend fun getRepos(@Path("username") userName: String): List<RepoRemoteData>
}


