package com.task.di

import com.metinozcura.rickandmorty.data.repository.episode.EpisodeRepositoryImpl
import com.task.data.repository.character.CharacterRepository
import com.task.data.repository.character.CharacterRepositoryImpl
import com.task.data.repository.episode.EpisodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository


    @Binds
    abstract fun bindEpisodeRepository(
        episodeRepositoryImpl : EpisodeRepositoryImpl
    ): EpisodeRepository

}