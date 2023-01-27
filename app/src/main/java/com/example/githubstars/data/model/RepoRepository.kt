package com.example.githubstars.data.model

import android.content.ContentValues.TAG
import android.util.Log
import com.example.githubstars.data.api.retrofit
import com.example.githubstars.data.database.remote.RepoRemoteData


class RepoRepository {
    suspend fun getRepoData(userName: String): List<RepoRemoteData> {
        val response = retrofit.getRepos(userName)
        Log.d(TAG, response.toString())
        return response
    }
}