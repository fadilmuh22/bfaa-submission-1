package com.example.bfaasubmission1.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.example.bfaasubmission1.databinding.UserItemRowBinding
import com.example.bfaasubmission1.ui.profile.ProfileDetailsActivity

class UserListAdapter : ListAdapter<GithubUser, UserListAdapter.UserViewHolder>(DIFF_CALLBACK) {
    inner class UserViewHolder(private val binding: UserItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GithubUser) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(profileImage)
                tvUsername.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UserViewHolder {
        val binding = UserItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            val moveIntent =
                Intent(holder.itemView.context, ProfileDetailsActivity::class.java).run {
                    putExtra(ProfileDetailsActivity.EXTRA_GITHUB_USER, getItem(position))
                }
            holder.itemView.context.startActivity(moveIntent)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GithubUser> =
            object : DiffUtil.ItemCallback<GithubUser>() {
                override fun areItemsTheSame(
                    oldItem: GithubUser,
                    newItem: GithubUser,
                ): Boolean {
                    return oldItem.login == newItem.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: GithubUser,
                    newItem: GithubUser,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
