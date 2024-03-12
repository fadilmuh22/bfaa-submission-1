package com.example.bfaasubmission1.data.remote.response

import android.os.Parcelable
import com.example.bfaasubmission1.data.local.entity.FavoriteUserEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GithubSearchResponse<T>(
    @field:SerializedName("total_count")
    val totalCount: Int,
    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @field:SerializedName("items")
    val items: List<T>,
)

@Parcelize
data class GithubUser(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("avatar_url")
    val avatarUrl: String,
    @field:SerializedName("html_url")
    val htmlUrl: String,
) : Parcelable {
    companion object {
        fun fromFavoriteUserEntity(favoriteUserEntity: FavoriteUserEntity) =
            GithubUser(
                id = favoriteUserEntity.id,
                login = favoriteUserEntity.login,
                avatarUrl = favoriteUserEntity.avatarUrl,
                htmlUrl = favoriteUserEntity.htmlUrl,
            )
    }
}

@Parcelize
data class GithubUserDetails(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("name")
    val name: String?,
    @field:SerializedName("avatar_url")
    val avatarUrl: String,
    @field:SerializedName("html_url")
    val htmlUrl: String,
    @field:SerializedName("followers")
    val followers: Int,
    @field:SerializedName("following")
    val following: Int,
    @field:SerializedName("location")
    val location: String,
) : Parcelable
