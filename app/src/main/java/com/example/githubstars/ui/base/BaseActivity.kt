package com.example.githubstars.ui.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

open class BaseActivity : MvpAppCompatActivity(), BaseView {
    @InjectPresenter
    open lateinit var presenter: BasePresenter<out BaseView>
    protected fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}