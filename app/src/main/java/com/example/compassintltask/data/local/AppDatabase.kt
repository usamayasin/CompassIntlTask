package com.example.compassintltask.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.compassintltask.data.local.dao.UserDao
import com.example.compassintltask.data.local.entity.UsersEntity
import com.example.compassintltask.utils.Constants.DATABASE_NAME

@Database(
    entities = [UsersEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usersDao(): UserDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}