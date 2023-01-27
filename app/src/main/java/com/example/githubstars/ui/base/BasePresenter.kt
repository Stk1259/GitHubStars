package com.example.githubstars.ui.base

import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
open class BasePresenter <View : BaseView>: MvpPresenter<View>()