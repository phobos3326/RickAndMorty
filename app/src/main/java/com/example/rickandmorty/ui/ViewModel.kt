package com.example.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.data.CharacterEntity
import com.example.rickandmorty.data.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: CharacterRepository
) : ViewModel() {



    val users = repo.getUsers().cachedIn(viewModelScope)
    fun getUserById(userId: String): Flow<CharacterEntity?> = repo.getUserById(userId)
}