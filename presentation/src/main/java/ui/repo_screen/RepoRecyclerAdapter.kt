package ui.repo_screen

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.example.githubstars.R
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.PaginationViewCreator
import domain.data.Repo
import domain.data.RepoList


class RepoRecyclerAdapter : OmegaRecyclerView.Adapter<RepoRecyclerAdapter.ViewHolder>(),
    PaginationViewCreator {
    private val repoResponse = RepoList(emptyList(), false)
    var list = repoResponse.listRepoData
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onImageClick: ((Repo) -> Unit?)? = null
    var onTextClick: ((Repo) -> Unit?)? = null
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
        holder.textRepos.setOnClickListener {
            onTextClick?.invoke(names)
        }

        fun fullStar(){
            holder.imageStar.setImageResource(R.drawable.full_star_icon)
            holder.imageStar.alpha = 1.0f
        }
        fun emptyStar(){
            holder.imageStar.setImageResource(R.drawable.empty_star_icon)
            holder.imageStar.alpha = 0.3f
        }
        if (names.favouriteStatus)
        {fullStar()}
        else
        {emptyStar()}
        holder.imageStar.setOnClickListener{
            onImageClick?.invoke(names)
            if (names.favouriteStatus){
                names.favouriteStatus = false
                emptyStar()
            }else{
                names.favouriteStatus = true
                fullStar()
            }
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
        val textRepos = itemView.findViewById<TextView>(R.id.text_repos)!!
        val imageStar: ImageView = itemView.findViewById(R.id.image_star)
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

