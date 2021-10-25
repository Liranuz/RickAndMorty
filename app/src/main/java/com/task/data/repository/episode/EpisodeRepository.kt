package com.task.data.repository.episode

import com.task.data.remote.model.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {

    fun getEpisode(episodeUrl : String): Flow<Episode>
}