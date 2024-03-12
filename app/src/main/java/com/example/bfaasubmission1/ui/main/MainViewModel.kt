package com.example.bfaasubmission1.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.newsapp.data.GithubUsersRepository
import com.example.bfaasubmission1.data.repository.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val githubUsersRepository: GithubUsersRepository,
    private val pref: SettingPreferences,
) : ViewModel() {
    companion object {
        private const val MOST_FOLLOWED_QUERY = "followers:\\>1000"
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun searchGithubUsers(searchText: String) = githubUsersRepository.searchUsers(searchText)

    fun getMostFollowedGithubUsers() =
        githubUsersRepository.searchUsers(
            MOST_FOLLOWED_QUERY,
        )
}
