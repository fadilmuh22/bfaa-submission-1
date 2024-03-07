package com.example.bfaasubmission1.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.databinding.UserItemRowBinding
import com.example.bfaasubmission1.ui.profile.ProfileDetailsActivity

class UserListAdapter(
    private val users: List<GithubUser>,
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
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

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int,
    ) {
        holder.bind(users[position])

        holder.itemView.setOnClickListener {
            val moveIntent =
                Intent(holder.itemView.context, ProfileDetailsActivity::class.java).run {
                    putExtra(ProfileDetailsActivity.EXTRA_GITHUB_USER, users[position])
                }
            holder.itemView.context.startActivity(moveIntent)
        }
    }
}
