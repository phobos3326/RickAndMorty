package com.example.rickandmorty.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val userId: String,
    val prevKey: Int?,
    val nextKey: Int?
)