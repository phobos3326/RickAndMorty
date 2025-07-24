package com.example.rickandmorty.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val image: String
)