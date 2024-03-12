package com.example.bfaasubmission1.ui.profile

import androidx.lifecycle.ViewModel
import com.dicoding.newsapp.data.GithubUsersRepository
import com.example.bfaasubmission1.data.remote.response.GithubUser

class ProfileFollowsViewModel(private val githubUsersRepository: GithubUsersRepository) : ViewModel() {
    companion object {
        private const val TAG = "ProfileFollowersViewModel"
    }

    fun getFollowers(githubUser: GithubUser?) = githubUsersRepository.getUserFollowers(githubUser?.login ?: "")

    fun getFollowing(githubUser: GithubUser?) = githubUsersRepository.getUserFollowing(githubUser?.login ?: "")
}
