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

class ProfileFollowingViewModel : ViewModel() {
    private val _following = MutableLiveData<List<GithubUser>>()
    val following: LiveData<List<GithubUser>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "ProfileFollowingViewModel"
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
                        Log.e(ProfileFollowingViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<List<GithubUser>>,
                    t: Throwable,
                ) {
                    _isLoading.value = false
                    _errorMessage.value = t.message
                    Log.e(ProfileFollowingViewModel.TAG, "onFailure: ${t.message}")
                }
            },
        )
    }
}
