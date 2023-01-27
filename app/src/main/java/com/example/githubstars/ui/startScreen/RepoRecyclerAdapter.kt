package com.example.githubstars.ui.startScreen

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubstars.R
import com.example.githubstars.data.database.remote.RepoRemoteData


class RepoRecyclerAdapter(
    private val context: Context,
    private var listOfRepo: List<RepoRemoteData>,
) :
    RecyclerView.Adapter<RepoRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_repository, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val names = listOfRepo[position]
        holder.textRepos.text = names.userName
    }

    override fun getItemCount(): Int {
        return listOfRepo.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textRepos = itemView.findViewById<TextView>(R.id.textRepos)!!
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<RepoRemoteData>) {
        listOfRepo = newList
        this.notifyDataSetChanged()
    }
}
