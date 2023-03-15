package ui.base

import com.example.githubstars.R
import moxy.MvpPresenter
import retrofit2.HttpException
import java.io.IOException


open class BasePresenter<View: BaseView> : MvpPresenter<View>(){
    protected fun getErrorValue(e: Exception): Int {
        val errorMessage = when (e) {
            is IOException -> R.string.error_message_no_internet
            is HttpException -> when (e.response()?.code()) {
                404 -> R.string.error_message_user_not_found
                403 -> R.string.error_message_limit_exceeded
                else -> R.string.error_unknown
            }
            else -> R.string.error_unknown
        }
        return errorMessage
    }
}