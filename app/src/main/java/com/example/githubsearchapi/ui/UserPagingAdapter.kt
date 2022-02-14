package com.example.githubsearchapi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubsearchapi.R
import com.example.githubsearchapi.databinding.UserItemBinding
import com.example.githubsearchapi.model.User

class UserPagingAdapter : PagingDataAdapter<User, UserPagingAdapter.ViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            viewBinding.tvName.text = getItem(position)?.name
            Glide.with(holder.itemView.context)
                .load(getItem(position)?.avatarUrl ?: "")
                .placeholder(R.mipmap.ic_launcher_round)
                .circleCrop()
                .into(viewBinding.ivAvatar)
        }
    }

    class ViewHolder(val viewBinding: UserItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
}