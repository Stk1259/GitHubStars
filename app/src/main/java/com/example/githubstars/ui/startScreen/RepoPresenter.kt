package com.example.githubstars.ui.startScreen

import com.example.githubstars.R
import com.example.githubstars.data.model.RepoRepository
import com.example.githubstars.ui.base.BasePresenter
import kotlinx.coroutines.*
import moxy.InjectViewState
import retrofit2.HttpException
import java.io.IOException

@InjectViewState
class RepoPresenter : BasePresenter<RepoView>() {
    private val getRepos = RepoRepository()

    fun onGetUserNameValue(userName: String?) {
        if (userName.isNullOrEmpty()) {
            val errorMessage = R.string.error_message_empty_username
            viewState.showMessage(errorMessage)
        } else {
            requestRepo(userName)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestRepo(userName: String) {
        viewState.setLoading(true)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val list = getRepos.getRepoData(userName)
                viewState.showRequestResults(list)
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> R.string.error_message_no_internet
                    is HttpException -> when (e.response()?.code()) {
                        404 -> R.string.error_message_user_not_found
                        403 -> R.string.error_message_limit_exceeded
                        else -> R.string.error_unknown
                    }
                    else -> R.string.error_unknown
                }
                viewState.showMessage(errorMessage)
            } finally {
                viewState.setLoading(false)
            }
        }

    }
}
