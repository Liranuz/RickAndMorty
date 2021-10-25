package com.metinozcura.rickandmorty.data.repository.episode

import com.task.data.remote.api.EpisodeApi
import com.task.data.remote.model.Episode
import com.task.data.repository.episode.EpisodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val service: EpisodeApi,
) : EpisodeRepository {


    override fun getEpisode(episodeUrl: String) = flow { emit(service.getEpisode(episodeUrl))}
}