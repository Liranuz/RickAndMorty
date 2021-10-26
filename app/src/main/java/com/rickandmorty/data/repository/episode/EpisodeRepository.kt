package com.rickandmorty.data.repository.episode

import com.rickandmorty.data.remote.model.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {

    fun getEpisode(episodeUrl : String): Flow<Episode>
}