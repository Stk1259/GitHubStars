package com.example.githubstars.ui.startScreen


import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubstars.R
import com.example.githubstars.data.database.remote.RepoRemoteData
import com.example.githubstars.ui.base.BaseActivity
import com.google.android.material.textfield.TextInputEditText
import moxy.presenter.InjectPresenter


class RepoActivity : BaseActivity(), RepoView {

    @InjectPresenter
    lateinit var startPresenter: RepoPresenter
    private lateinit var adapter: RepoRecyclerAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)
        val textInput = findViewById<TextInputEditText>(R.id.textInputUsername)
        progressBar = findViewById(R.id.progressBarStartActivity)
        val recycler = findViewById<RecyclerView>(R.id.repos_recycler_view)
        adapter = RepoRecyclerAdapter(this, listOf())
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        val button = findViewById<Button>(R.id.button_search)
        button.setOnClickListener {
            hideKeyBoard()
            startPresenter.onGetUserNameValue(textInput.text.toString())
        }
    }


    override fun setLoading(loading: Boolean) {
        progressBar.isVisible = loading
    }


    override fun showRequestResults(list: List<RepoRemoteData>?) {
        if (list != null) {
            adapter.setList(list)
        }
    }

    override fun showMessage(errorMessage: Int) {
        adapter.setList(emptyList())
        Toast.makeText(
            this,
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
}
