package com.example.bfaasubmission1.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bfaasubmission1.data.response.GithubSearchResponse
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _githubUsers = MutableLiveData<List<GithubUser>>()
    val githubUsers: LiveData<List<GithubUser>> = _githubUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "MainViewModel"
        private const val MOST_FOLLOWED_QUERY = "followers:\\>1000"
    }

    init {
        getMostFollowedGithubUsers()
    }

    fun searchGithubUsers(searchText: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(searchText)
        client.enqueue(
            object : Callback<GithubSearchResponse<GithubUser>> {
                override fun onResponse(
                    call: Call<GithubSearchResponse<GithubUser>>,
                    response: Response<GithubSearchResponse<GithubUser>>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _githubUsers.value = response.body()?.items
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<GithubSearchResponse<GithubUser>>,
                    t: Throwable,
                ) {
                    _isLoading.value = false
                    _errorMessage.value = t.message
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            },
        )
    }

    private fun getMostFollowedGithubUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(MOST_FOLLOWED_QUERY)
        client.enqueue(
            object : Callback<GithubSearchResponse<GithubUser>> {
                override fun onResponse(
                    call: Call<GithubSearchResponse<GithubUser>>,
                    response: Response<GithubSearchResponse<GithubUser>>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _githubUsers.value = response.body()?.items
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<GithubSearchResponse<GithubUser>>,
                    t: Throwable,
                ) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            },
        )
    }
}
