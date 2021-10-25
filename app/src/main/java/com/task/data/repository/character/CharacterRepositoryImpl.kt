package com.task.data.repository.character

import androidx.collection.arrayMapOf
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.task.data.remote.api.CharacterApi
import com.task.data.remote.model.CharacterData
import com.task.data.remote.paging.datasource.CharactersPagingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val character: CharacterApi,
    ) : CharacterRepository {



    override fun getCharacters(): Flow<PagingData<CharacterData>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = {
            CharactersPagingDataSource(character, arrayMapOf())
        }
    ).flow


    override fun filterCharacters(query: Map<String, String>) : Flow<PagingData<CharacterData>> = Pager (
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = {
            CharactersPagingDataSource(character, query)
        }
    ).flow


}