package com.rickandmorty.data.repository.character

import androidx.paging.PagingData
import com.rickandmorty.data.remote.model.CharacterData
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacters(): Flow<PagingData<CharacterData>>

    fun filterCharacters(query: Map<String, String>) : Flow<PagingData<CharacterData>>

}