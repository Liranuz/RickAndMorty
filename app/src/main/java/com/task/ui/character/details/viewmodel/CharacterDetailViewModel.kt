package com.task.ui.character.details.viewmodel

import androidx.lifecycle.ViewModel
import com.task.data.remote.model.Episode
import com.task.data.repository.character.CharacterRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterRepositoryImpl: CharacterRepositoryImpl
) : ViewModel() {}
