package com.example.bfaasubmission1.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.bfaasubmission1.data.local.entity.FavoriteUserEntity
import com.example.bfaasubmission1.data.local.room.FavoriteUserDao
import com.example.bfaasubmission1.data.local.room.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUsersRepository(application: Application) {
    private val favoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteUserEntity>> = favoriteUserDao.getAllFavorites()

    fun insert(favoriteUser: FavoriteUserEntity) {
        executorService.execute { favoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUserEntity) {
        executorService.execute { favoriteUserDao.delete(favoriteUser) }
    }

    fun update(favoriteUser: FavoriteUserEntity) {
        executorService.execute { favoriteUserDao.update(favoriteUser) }
    }

    fun isFavorite(id: Int): LiveData<Boolean> = favoriteUserDao.isFavorite(id)
}
