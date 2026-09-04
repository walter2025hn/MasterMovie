package com.mastermovie.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey val id: String,
    val title: String,
    val poster: String,
    val lastPosition: Long,
    val duration: Long,
    val lastPlayedAt: Long
)

@Entity(tableName = "favorites")
data class FavoriteItem(
    @PrimaryKey val id: String,
    val title: String,
    val poster: String,
    val addedAt: Long
)
