package com.example.bfaasubmission1.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFollowsViewModel : ViewModel() {
    private val _followers = MutableLiveData<List<GithubUser>>()
    val followers: LiveData<List<GithubUser>> = _followers

    private val _following = MutableLiveData<List<GithubUser>>()
    val following: LiveData<List<GithubUser>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "ProfileFollowersViewModel"
    }

    fun getFollowers(githubUser: GithubUser?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(githubUser?.login ?: "")
        client.enqueue(
            object : Callback<List<GithubUser>> {
                override fun onResponse(
                    call: Call<List<GithubUser>>,
                    response: Response<List<GithubUser>>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _followers.value = response.body()
                    } else {
                        Log.e(ProfileFollowsViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<List<GithubUser>>,
                    t: Throwable,
                ) {
                    _isLoading.value = false
                    _errorMessage.value = t.message
                    Log.e(ProfileFollowsViewModel.TAG, "onFailure: ${t.message}")
                }
            },
        )
    }

    fun getFollowing(githubUser: GithubUser?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(githubUser?.login ?: "")
        client.enqueue(
            object : Callback<List<GithubUser>> {
                override fun onResponse(
                    call: Call<List<GithubUser>>,
                    response: Response<List<GithubUser>>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _following.value = response.body()
                    } else {
                        Log.e(ProfileFollowsViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<List<GithubUser>>,
                    t: Throwable,
                ) {
                    _isLoading.value = false
                    _errorMessage.value = t.message
                    Log.e(ProfileFollowsViewModel.TAG, "onFailure: ${t.message}")
                }
            },
        )
    }
}
