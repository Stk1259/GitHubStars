package com.example.githubstars.ui.startScreen

import com.example.githubstars.data.database.remote.RepoRemoteData
import com.example.githubstars.ui.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip


interface RepoView : BaseView {

    @AddToEndSingle
    fun setLoading(loading: Boolean)

    @AddToEndSingle
    fun showRequestResults(list: List<RepoRemoteData>?)

    @Skip
    fun showMessage(errorMessage: Int)
}