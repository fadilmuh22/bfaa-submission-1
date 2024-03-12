package com.example.bfaasubmission1.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bfaasubmission1.data.repository.FavoriteUsersRepository
import com.example.bfaasubmission1.di.Injection

class FavoriteUsersViewModelFactory private constructor(
    private val favoriteUsersRepository: FavoriteUsersRepository,
) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteUsersViewModel::class.java)) {
                return FavoriteUsersViewModel(favoriteUsersRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: FavoriteUsersViewModelFactory? = null

            fun getInstance(mApplication: Application): FavoriteUsersViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: FavoriteUsersViewModelFactory(
                        Injection.provideFavoriteUsersRepository(
                            mApplication,
                        ),
                    )
                }.also { instance = it }
        }
    }
