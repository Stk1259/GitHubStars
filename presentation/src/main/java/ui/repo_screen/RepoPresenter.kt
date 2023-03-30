package ui.repo_screen

import android.content.Context
import android.util.Log
import com.example.githubstars.R
import data.repository.DataRepository
import kotlinx.coroutines.launch
import moxy.presenterScope
import ui.base.BasePresenter

class RepoPresenter : BasePresenter<RepoView>() {
    private val repository = DataRepository
    fun loadRepos(userName: String?, listPage: Int, context: Context) = presenterScope.launch {
        if (userName.isNullOrEmpty()) {
            val errorMessage = R.string.error_message_empty_username
            viewState.showMessage(errorMessage)
        } else {
            viewState.setLoading(listPage <= 0)
            try {
                val list = repository.getRepoData(userName, listPage, context)
                viewState.showRepoList(list)
            } catch (e: Exception) {
                Log.e(e.cause.toString(), e.message.toString())
                val errorMessage = getErrorValue(e)
                viewState.showMessage(errorMessage)
            } finally {
                viewState.setLoading(false)
            }
        }
    }

    fun addFavouriteRepo(
        context: Context,
        repoName: String,
    ) {
        presenterScope.launch {
            repository.addFavouriteRepo(context, repoName)
        }
    }

    fun deleteFavouriteRepo(context: Context, id: Int) {
        presenterScope.launch {
            repository.deleteFavouriteRepo(context, id)
        }
    }
}

