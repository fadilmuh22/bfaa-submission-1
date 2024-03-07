package com.example.bfaasubmission1.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.data.response.GithubUserDetails
import com.example.bfaasubmission1.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val _githubUserDetails = MutableLiveData<GithubUserDetails>()
    val githubUserDetails: LiveData<GithubUserDetails> = _githubUserDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _githubUser = MutableLiveData<GithubUser>()
    val githubUser: LiveData<GithubUser> = _githubUser

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    fun setGithubUser(pGithubUser: GithubUser) {
        _githubUser.value = pGithubUser
    }

    fun getUserDetails(githubUser: GithubUser?) {
        val client = ApiConfig.getApiService().getUserDetails(githubUser?.login ?: "")
        client.enqueue(
            object : Callback<GithubUserDetails> {
                override fun onResponse(
                    call: Call<GithubUserDetails>,
                    response: Response<GithubUserDetails>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _githubUserDetails.value = response.body()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<GithubUserDetails>,
                    t: Throwable,
                ) {
                    _isLoading.value = false
                    _errorMessage.value = t.message
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            },
        )
    }
}
