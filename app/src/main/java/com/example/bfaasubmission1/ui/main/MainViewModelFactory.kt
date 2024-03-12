package com.example.bfaasubmission1.ui.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.newsapp.data.GithubUsersRepository
import com.example.bfaasubmission1.data.repository.SettingPreferences
import com.example.bfaasubmission1.di.Injection

class MainViewModelFactory private constructor(
    private val githubUsersRepository: GithubUsersRepository,
    private val pref: SettingPreferences,
) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(githubUsersRepository, pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: MainViewModelFactory? = null

            fun getInstance(
                context: Context,
                dataStore: DataStore<Preferences>,
            ): MainViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: MainViewModelFactory(
                        Injection.provideGithubUsersRepository(context),
                        Injection.provideSettingPreferences(dataStore),
                    )
                }.also { instance = it }
        }
    }
