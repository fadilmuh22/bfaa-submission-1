package com.example.bfaasubmission1.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bfaasubmission1.data.local.entity.FavoriteUserEntity

@Database(version = 1, entities = [FavoriteUserEntity::class])
abstract class FavoriteUserRoomDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var instance: FavoriteUserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteUserRoomDatabase {
            if (instance == null) {
                synchronized(FavoriteUserRoomDatabase::class.java) {
                    instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            FavoriteUserRoomDatabase::class.java,
                            "note_database",
                        )
                            .build()
                }
            }
            return instance as FavoriteUserRoomDatabase
        }
    }
}
