package ui.repo_screen


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.githubstars.R
import com.google.android.material.textfield.TextInputEditText
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener
import domain.data.RepoList
import moxy.ktx.moxyPresenter
import notifications.SetAlarm
import ui.base.BaseActivity
import ui.graph_screen.GraphActivity


class RepoActivity : BaseActivity(), RepoView, OnPageRequestListener {
//    private val notificationReceiver = NotificationReceiver()
    private val repoPresenter by moxyPresenter { RepoPresenter() }
    private val setAlarm = SetAlarm()
    private lateinit var repoAdapter: RepoRecyclerAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var userName: String
    private lateinit var recycler: OmegaRecyclerView
    private var listPage: Int = 0

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, RepoActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)
//        val filter = IntentFilter().apply {
//            addAction("com.example.ACTION_ALARM")
//        }
//        registerReceiver(notificationReceiver, filter)
        val textInput = findViewById<TextInputEditText>(R.id.textInputUsername)
        progressBar = findViewById(R.id.progressBarStartActivity)
        recycler = findViewById(R.id.repos_recycler_view)
        recycler.setPaginationCallback(this)
        repoAdapter = RepoRecyclerAdapter()
        recycler.adapter = repoAdapter

        val button = findViewById<Button>(R.id.button_search)
        button.setOnClickListener {
            recycler.hidePagination()
            listPage = 0
            hideKeyBoard()
            repoAdapter.list = emptyList()
            userName = textInput.text.toString()
            repoPresenter.loadRepos(userName, listPage, this)
        }
    }

    override fun setLoading(loading: Boolean) {
        progressBar.isVisible = loading
    }

    override fun showRepoList(repoResponse: RepoList) {
        repoResponse.listRepoData.let { repoAdapter.addValues(repoResponse) }
        if (repoResponse.nextPageStatus) {
            recycler.showProgressPagination()
        } else recycler.hidePagination()
        repoAdapter.onTextClick = {
            startActivity(
                GraphActivity.createIntent(
                    this,
                    userName,
                    it.repoName,
                    it.stargazersCount
                )
            )
        }

        repoAdapter.onImageClick = {
            if (!it.favouriteStatus) {
                repoPresenter.addFavouriteRepo(
                    this,
                    it.repoName,
                )
                    setAlarm.setAlarm(this)

            } else {
                repoPresenter.deleteFavouriteRepo(this, it.id)
            }
        }
    }

    override fun showMessage(errorMessage: Int) {
        Log.d("error", errorMessage.toString())
        repoAdapter.list = emptyList()
        Toast.makeText(
            this, errorMessage, Toast.LENGTH_LONG
        ).show()
    }

    override fun onPageRequest(page: Int) {
        loadMoreRepos()
    }
    private fun loadMoreRepos() {
        listPage += 1
        repoPresenter.loadRepos(userName, listPage, this)
    }

    override fun getPagePreventionForEnd(): Int {
        return 5
    }
}