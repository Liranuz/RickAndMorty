package com.task.ui.character.list.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.task.data.remote.model.CharacterData
import com.task.data.repository.character.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private lateinit var _charactersFlow: Flow<PagingData<CharacterData>>

    val charactersFlow: Flow<PagingData<CharacterData>>
        get() = _charactersFlow



    fun getAllCharacters() =
        characterRepository.getCharacters().cachedIn(viewModelScope)


    fun getFilterCharacters(filterMap: Map<String, String>) =
        characterRepository.filterCharacters(filterMap).cachedIn(viewModelScope)

}

