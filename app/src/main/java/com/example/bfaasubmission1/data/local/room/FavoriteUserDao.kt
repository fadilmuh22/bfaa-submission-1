package com.example.bfaasubmission1.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bfaasubmission1.data.local.entity.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: FavoriteUserEntity)

    @Delete
    fun delete(note: FavoriteUserEntity)

    @Query("SELECT * from favorite_users ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_users WHERE id = :id)")
    fun isFavorite(id: Int): LiveData<Boolean>
}
