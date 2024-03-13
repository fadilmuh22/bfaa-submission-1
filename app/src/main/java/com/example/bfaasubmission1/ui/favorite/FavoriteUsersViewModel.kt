package com.example.bfaasubmission1.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.bfaasubmission1.data.repository.FavoriteUsersRepository

class FavoriteUsersViewModel(private val favoriteUsersRepository: FavoriteUsersRepository) :
    ViewModel() {
    fun getAllFavoriteUsers() = favoriteUsersRepository.getAllFavorites()
}
