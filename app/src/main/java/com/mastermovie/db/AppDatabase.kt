package com.mastermovie.db

import androidx.room.*
import android.content.Context
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM history ORDER BY lastPlayedAt DESC")
    fun history(): List<HistoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHistory(item: HistoryItem)

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun favorites(): List<FavoriteItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(item: FavoriteItem)

    @Delete
    suspend fun removeFavorite(item: FavoriteItem)
}

@Database(entities = [HistoryItem::class, FavoriteItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,"mastermovie.db").build()
                INSTANCE = inst
                inst
            }
    }
}
