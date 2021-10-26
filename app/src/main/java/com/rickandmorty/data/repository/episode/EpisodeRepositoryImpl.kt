package com.rickandmorty.data.repository.episode

import com.rickandmorty.data.remote.api.EpisodeApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val service: EpisodeApi,
) : EpisodeRepository {


    override fun getEpisode(episodeUrl: String) = flow { emit(service.getEpisode(episodeUrl))}
}