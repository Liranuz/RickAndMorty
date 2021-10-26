package com.rickandmorty.data.remote.api

import com.rickandmorty.data.remote.model.CharacterList
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CharacterApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") query: Int): CharacterList

    @GET("character/?")
    suspend fun filterCharacters(@Query("page") query: Int, @QueryMap query1: Map<String, String>): CharacterList

}