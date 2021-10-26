package com.rickandmorty.data.remote.model

data class CharacterList(
    val info: Info,
    val results: List<CharacterData>
)