package ui.stargazers_screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.githubstars.R
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import domain.data.FormattedStar

class StargazersRecyclerAdapter(private val context: Context) :
    OmegaRecyclerView.Adapter<StargazersRecyclerAdapter.ViewHolder>() {
    var list = ArrayList<FormattedStar>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StargazersRecyclerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stargazers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StargazersRecyclerAdapter.ViewHolder, position: Int) {
        val users = list[position]
        holder.login.text = users.user.name
        Glide.with(context)
            .load(users.user.avatarUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.downloading_icon)
            )
            .skipMemoryCache(true)
            .error(R.drawable.error_icon)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    Log.e("TAG", "Error loading image", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    return false
                }
            })
            .into(holder.avatar)
    }

    inner class ViewHolder(itemView: View) :
        OmegaRecyclerView.ViewHolder(itemView) {
        val login: TextView = itemView.findViewById(R.id.userName)
        val avatar: ImageView = itemView.findViewById(R.id.avatarView)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}