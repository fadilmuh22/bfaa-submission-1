package com.example.bfaasubmission1.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.newsapp.data.GithubUsersRepository
import com.example.bfaasubmission1.data.repository.FavoriteUsersRepository
import com.example.bfaasubmission1.di.Injection
import com.example.bfaasubmission1.ui.main.ProfileDetailsViewModel

class ProfileDetailsViewModelFactory private constructor(
    private val githubUsersRepository: GithubUsersRepository,
    private val favoriteUsersRepository: FavoriteUsersRepository,
) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileDetailsViewModel::class.java)) {
                return ProfileDetailsViewModel(githubUsersRepository, favoriteUsersRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: ProfileDetailsViewModelFactory? = null

            fun getInstance(
                context: Context,
                mApplication: Application,
            ): ProfileDetailsViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ProfileDetailsViewModelFactory(
                        Injection.provideGithubUsersRepository(context),
                        Injection.provideFavoriteUsersRepository(mApplication),
                    )
                }.also { instance = it }
        }
    }
