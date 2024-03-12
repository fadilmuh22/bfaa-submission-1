package com.example.bfaasubmission1.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dicoding.newsapp.data.GithubUsersRepository
import com.example.bfaasubmission1.data.remote.retrofit.ApiConfig
import com.example.bfaasubmission1.data.repository.FavoriteUsersRepository
import com.example.bfaasubmission1.data.repository.SettingPreferences

object Injection {
    fun provideGithubUsersRepository(context: Context): GithubUsersRepository {
        val apiService = ApiConfig.getApiService()
        return GithubUsersRepository.getInstance(apiService)
    }

    fun provideFavoriteUsersRepository(application: Application): FavoriteUsersRepository {
        return FavoriteUsersRepository(application)
    }

    fun provideSettingPreferences(dataStore: DataStore<Preferences>) = SettingPreferences.getInstance(dataStore)
}
