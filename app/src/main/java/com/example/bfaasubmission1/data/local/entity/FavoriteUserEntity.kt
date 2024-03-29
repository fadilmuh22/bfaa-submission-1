package com.example.bfaasubmission1.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bfaasubmission1.data.remote.response.GithubUser
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_users")
@Parcelize
data class FavoriteUserEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "login")
    var login: String,
    @ColumnInfo("avatar_url")
    val avatarUrl: String,
    @field:SerializedName("html_url")
    val htmlUrl: String,
) : Parcelable {
    companion object {
        fun fromGithubUser(githubUser: GithubUser) =
            FavoriteUserEntity(
                id = githubUser.id,
                login = githubUser.login,
                avatarUrl = githubUser.avatarUrl,
                htmlUrl = githubUser.htmlUrl,
            )
    }
}
