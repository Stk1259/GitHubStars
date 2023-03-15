package ui.repo_screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.githubstars.R
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.PaginationViewCreator
import domain.entity.Repo
import domain.entity.RepoList


class RepoRecyclerAdapter : OmegaRecyclerView.Adapter<RepoRecyclerAdapter.ViewHolder>(),
    PaginationViewCreator {
    private val repoResponse = RepoList(emptyList(), false)
    var list = repoResponse.listRepoData
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClick: ((Repo) -> Unit?)? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val names = list[position]
        holder.textRepos.text = names.repoName
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(names)
        }
    }

    fun addValues(repoResponse: RepoList) {
        this.list = this.list + repoResponse.listRepoData
        notifyItemInserted(this.list.size - repoResponse.listRepoData.size)
    }

    override fun isDividerAllowedBelow(position: Int): Boolean {
        return super.isDividerAllowedBelow(position) && position % 2 == 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) :
        OmegaRecyclerView.ViewHolder(itemView) {
        val textRepos = itemView.findViewById<TextView>(R.id.textRepos)!!
    }

    override fun createPaginationView(parent: ViewGroup?, inflater: LayoutInflater?): View? {
        return inflater?.inflate(
            com.omega_r.libs.omegarecyclerview.R.layout.pagination_omega_layout,
            parent,
            false
        )
    }

    override fun createPaginationErrorView(parent: ViewGroup?, inflater: LayoutInflater?): View? {
        return inflater?.inflate(
            com.omega_r.libs.omegarecyclerview.R.layout.pagination_error_omega_layout,
            parent,
            false
        )
    }
}

