package com.task.data.remote.api

import com.task.data.remote.model.Episode
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Url

interface EpisodeApi {

    @GET
    suspend fun getEpisode(
        @Url url: String,
        ): Episode


}