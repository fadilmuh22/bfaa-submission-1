package com.dicoding.newsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bfaasubmission1.data.Result
import com.example.bfaasubmission1.data.remote.response.GithubSearchResponse
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.example.bfaasubmission1.data.remote.response.GithubUserDetails
import com.example.bfaasubmission1.data.remote.retrofit.ApiService

class GithubUsersRepository private constructor(
    private val apiService: ApiService,
) {
    fun searchUsers(queryText: String): LiveData<Result<GithubSearchResponse<GithubUser>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.searchUsers(queryText)
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d(TAG, "searchUsers: ${e.message} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getUserDetails(username: String): LiveData<Result<GithubUserDetails>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getUserDetails(username)
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d(TAG, "getUserDetails: ${e.message} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getUserFollowers(username: String): LiveData<Result<List<GithubUser>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getUserFollowers(username)
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d(TAG, "getUserFollowers: ${e.message} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getUserFollowing(username: String): LiveData<Result<List<GithubUser>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getUserFollowing(username)
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d(TAG, "getUserFollowing: ${e.message} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        const val TAG = "GithubUsersRepository"

        @Volatile
        private var instance: GithubUsersRepository? = null

        fun getInstance(apiService: ApiService): GithubUsersRepository =
            instance ?: synchronized(this) {
                instance ?: GithubUsersRepository(apiService)
            }.also { instance = it }
    }
}
