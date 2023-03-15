package ui.repo_screen

import domain.entity.RepoList
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ui.base.BaseView


interface RepoView : BaseView {

    @AddToEndSingle
    fun setLoading(loading: Boolean)

    @AddToEndSingle
    fun showRepoList(repoResponse: RepoList)

    @OneExecution
    fun showMessage(errorMessage: Int)
}