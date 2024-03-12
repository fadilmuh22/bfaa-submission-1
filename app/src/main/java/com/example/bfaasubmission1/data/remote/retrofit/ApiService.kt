package com.example.bfaasubmission1.data.remote.retrofit

import com.example.bfaasubmission1.data.remote.response.GithubSearchResponse
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.example.bfaasubmission1.data.remote.response.GithubUserDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") queryString: String,
        @Query("page") page: Int? = 1,
        @Query("per_page") perPage: Int? = 20,
        @Query("sort") sort: String? = "followers",
        @Query("order") order: String? = "desc",
    ): GithubSearchResponse<GithubUser>

    @GET("/users/{username}")
    suspend fun getUserDetails(
        @Path("username") queryString: String,
    ): GithubUserDetails

    @GET("/users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") queryString: String,
    ): List<GithubUser>

    @GET("/users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") queryString: String,
    ): List<GithubUser>
}
