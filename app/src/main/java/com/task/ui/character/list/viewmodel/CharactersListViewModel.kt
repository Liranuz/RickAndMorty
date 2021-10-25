package com.task.ui.character.list.viewmodel

import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.metinozcura.rickandmorty.base.BaseViewModel

import com.task.data.remote.model.CharacterData
import com.task.data.repository.character.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : BaseViewModel() {

    private lateinit var _charactersFlow: Flow<PagingData<CharacterData>>

    val charactersFlow: Flow<PagingData<CharacterData>>
        get() = _charactersFlow

    init {
        getAllCharacters()
    }

    private fun getAllCharacters() = launchPagingAsync({
        characterRepository.getCharacters().cachedIn(viewModelScope)
    }, {
        _charactersFlow = it
    })


    fun getFilterCharacters(filterMap: Map<String, String>) = launchPagingAsync({
        characterRepository.filterCharacters(filterMap).cachedIn(viewModelScope)
    },{
        _charactersFlow = it
    })

}


//    fun setFilter(filterMap: Map<String, String>) : Flow<PagingData<CharacterData>> {
//        return characterRepository.filterCharacters(filterMap).cachedIn(viewModelScope)
//    }


