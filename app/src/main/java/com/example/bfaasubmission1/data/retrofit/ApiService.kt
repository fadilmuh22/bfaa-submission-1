package com.example.bfaasubmission1.data.retrofit

import com.example.bfaasubmission1.data.response.GithubSearchResponse
import com.example.bfaasubmission1.data.response.GithubUser
import com.example.bfaasubmission1.data.response.GithubUserDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    fun searchUsers(
        @Query("q") queryString: String,
        @Query("page") page: Int? = 1,
        @Query("per_page") perPage: Int? = 20,
        @Query("sort") sort: String? = "followers",
        @Query("order") order: String? = "desc",
    ): Call<GithubSearchResponse<GithubUser>>

    @GET("/users/{username}")
    fun getUserDetails(
        @Path("username") queryString: String,
    ): Call<GithubUserDetails>

    @GET("/users/{username}/followers")
    fun getUserFollowers(
        @Path("username") queryString: String,
    ): Call<List<GithubUser>>

    @GET("/users/{username}/following")
    fun getUserFollowing(
        @Path("username") queryString: String,
    ): Call<List<GithubUser>>
}
