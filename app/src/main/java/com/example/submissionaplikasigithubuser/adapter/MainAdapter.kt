package com.example.submissionaplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.response.ItemsItem

class MainAdapter(private val list: ArrayList<ItemsItem>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_review, viewGroup, false)
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val list = list[position]
        viewHolder.tvItem.text = list.login
        viewHolder.tvLink.text = list.htmlUrl
        Glide.with(viewHolder.itemView.context)
            .load(list.avatarUrl)
            .into(viewHolder.ivPhoto)
        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(list)
        }
    }

    override fun getItemCount() = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.findViewById(R.id.tv_item_name)
        val ivPhoto: ImageView = view.findViewById(R.id.img_item_photo)
        val tvLink: TextView = view.findViewById(R.id.tv_link)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}