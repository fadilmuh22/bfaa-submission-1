package com.example.bfaasubmission1.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.newsapp.data.GithubUsersRepository
import com.example.bfaasubmission1.data.local.entity.FavoriteUserEntity
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.example.bfaasubmission1.data.repository.FavoriteUsersRepository
import kotlinx.coroutines.launch

class ProfileDetailsViewModel(
    private val githubUsersRepository: GithubUsersRepository,
    private val favoriteUsersRepository: FavoriteUsersRepository,
) : ViewModel() {
    fun getUserDetails(githubUser: GithubUser?) = githubUsersRepository.getUserDetails(githubUser?.login ?: "")

    fun setFavorite(
        githubUser: GithubUser,
        isFavorite: Boolean,
    ) {
        val entity = FavoriteUserEntity.fromGithubUser(githubUser)
        if (isFavorite) {
            viewModelScope.launch {
                favoriteUsersRepository.insert(entity)
            }
        } else {
            favoriteUsersRepository.delete(entity)
        }
    }

    fun isFavorite(id: Int) = favoriteUsersRepository.isFavorite(id)
}
