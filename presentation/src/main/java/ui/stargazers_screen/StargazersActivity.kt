package ui.stargazers_screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.example.githubstars.R
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import domain.data.FormattedStar
import moxy.ktx.moxyPresenter
import ui.base.BaseActivity
import java.io.Serializable

class StargazersActivity : BaseActivity(), StargazersView {
    companion object {
        private const val EXTRA_STARGAZERS_LIST = "stargazersList"
        fun createIntent(context: Context, stargazersList: ArrayList<FormattedStar>?): Intent {
            return Intent(context, StargazersActivity::class.java)
                .putExtra(EXTRA_STARGAZERS_LIST, stargazersList)
        }
    }

    private val stargazersPresenter by moxyPresenter {
        StargazersPresenter(
            intent.serializable(EXTRA_STARGAZERS_LIST)!!
        )
    }
    private lateinit var stargazersAdapter: StargazersRecyclerAdapter
    private lateinit var recycler: OmegaRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stargazers)
        recycler = findViewById(R.id.recycler_stargazers)
        stargazersAdapter = StargazersRecyclerAdapter(this)
        recycler.adapter = stargazersAdapter
    }

    override fun showUsers(stargazersList: ArrayList<FormattedStar>) {
        stargazersAdapter.list = stargazersList
    }
}
private inline fun <reified T : Serializable> Intent.serializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key) as? T
    }
}

