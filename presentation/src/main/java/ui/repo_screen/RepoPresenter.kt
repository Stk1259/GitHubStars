package ui.repo_screen

import android.util.Log
import com.example.githubstars.R
import data.repository.DataRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import moxy.presenterScope
import ui.base.BasePresenter

class RepoPresenter : BasePresenter<RepoView>() {
    private val repository = DataRepository
    fun loadRepos(userName: String?, listPage: Int) = presenterScope.launch {
        if (userName.isNullOrEmpty()) {
            val errorMessage = R.string.error_message_empty_username
            viewState.showMessage(errorMessage)
        } else {
            viewState.setLoading(listPage <= 0)
            try {
                val list = repository.getRepoData(userName, listPage)
                viewState.showRepoList(list)
            } catch (e: Exception) {
                Log.e(e.cause.toString(), e.message.toString())
                val errorMessage = getErrorValue(e)
                viewState.showMessage(errorMessage)
            } finally {
                viewState.setLoading(false)
                coroutineContext.cancel()
            }
        }
    }
}

