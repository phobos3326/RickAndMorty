package com.example.rickandmorty.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val db: AppDatabase
) {
    private val dao = db.userDao()
    @OptIn(ExperimentalPagingApi::class)
    fun getUsers(): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = CharacterRemoteMediator(api, db),
            pagingSourceFactory = { db.userDao().pagingSource() }
        ).flow
    }

    fun getUserById(userId: String): Flow<CharacterEntity?> = dao.getUserById(userId)
}