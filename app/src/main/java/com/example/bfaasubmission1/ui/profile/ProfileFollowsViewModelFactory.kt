package com.example.bfaasubmission1.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.newsapp.data.GithubUsersRepository
import com.example.bfaasubmission1.di.Injection

class ProfileFollowsViewModelFactory private constructor(private val githubUsersRepository: GithubUsersRepository) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileFollowsViewModel::class.java)) {
                return ProfileFollowsViewModel(githubUsersRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: ProfileFollowsViewModelFactory? = null

            fun getInstance(context: Context): ProfileFollowsViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ProfileFollowsViewModelFactory(
                        Injection.provideGithubUsersRepository(
                            context,
                        ),
                    )
                }.also { instance = it }
        }
    }
