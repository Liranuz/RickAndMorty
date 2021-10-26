package com.task.ui.episode.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickandmorty.data.Resource
import com.rickandmorty.data.remote.model.Episode
import com.rickandmorty.data.repository.episode.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val episodeRepository: EpisodeRepository
) : ViewModel() {

    private val _episode = MutableStateFlow<Resource<Episode>>(Resource.Loading())
    val episode: StateFlow<Resource<Episode>> = _episode


    fun getEpisode(episodeUrl: String) =
        viewModelScope.launch {
            episodeRepository.getEpisode(episodeUrl)
                .map { resource -> Resource.Success(resource) }
                .collect { state -> _episode.value = state }
        }

}
