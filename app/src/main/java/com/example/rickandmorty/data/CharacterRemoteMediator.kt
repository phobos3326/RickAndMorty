package com.example.rickandmorty.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: RickAndMortyApi,
    private val db: AppDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = db.userDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = api.getCharacters(page) // Возвращает RAndMDTO
            val characters = apiResponse.results?.filterNotNull()?.map {
                CharacterEntity(
                    id = it.id ?: 0,
                    name = it.name.orEmpty(),
                    status = it.status.orEmpty(),
                    species = it.species.orEmpty(),
                    type = it.type.orEmpty(),
                    gender = it.gender.orEmpty(),
                    originName = it.origin?.name.orEmpty(),
                    locationName = it.location?.name.orEmpty(),
                    image = it.image.orEmpty()
                )
            } ?: emptyList()

            val endOfPaginationReached = apiResponse.info?.next == null

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    characterDao.clearAll()
                }

                val keys = characters.map {
                    RemoteKeys(
                        userId = it.id.toString(),
                        prevKey = getPageFromUrl(apiResponse.info?.prev),
                        nextKey = getPageFromUrl(apiResponse.info?.next)
                    )
                }

                remoteKeysDao.insertAll(keys)
                characterDao.insertAll(characters)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private fun getPageFromUrl(url: Any?): Int? {
        return (url as? String)?.substringAfter("page=", "")?.toIntOrNull()
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { character ->
            remoteKeysDao.remoteKeysUserId(character.id.toString())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharacterEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { character ->
            remoteKeysDao.remoteKeysUserId(character.id.toString())
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.remoteKeysUserId(id.toString())
            }
        }
    }
}