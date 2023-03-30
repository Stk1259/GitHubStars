package ui.stargazers_screen

import domain.data.FormattedStar
import ui.base.BasePresenter

class StargazersPresenter(private val stargazersList: ArrayList<FormattedStar>) :
    BasePresenter<StargazersView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        showUsers()
    }

    private fun showUsers() {
        viewState.showUsers(stargazersList)
    }
}