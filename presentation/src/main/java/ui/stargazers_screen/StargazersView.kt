package ui.stargazers_screen

import domain.data.FormattedStar
import moxy.viewstate.strategy.alias.AddToEndSingle
import ui.base.BaseView

interface StargazersView : BaseView {
    @AddToEndSingle
    fun showUsers(stargazersList: ArrayList<FormattedStar>)
}