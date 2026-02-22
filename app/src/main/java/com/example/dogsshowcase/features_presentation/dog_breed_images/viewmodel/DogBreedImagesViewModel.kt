package com.example.dogsshowcase.features_presentation.dog_breed_images.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogsshowcase.features_components.domain.use_cases.FetchDogBreedImagesUseCase
import com.example.dogsshowcase.utils.K
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogBreedImagesViewModel @Inject constructor(
    private val fetchDogBreedImagesUseCase: FetchDogBreedImagesUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val breed: String = savedStateHandle.get<String>(K.DOG_BREED) ?: ""

    var dogBreedImagesState by mutableStateOf(DogBreedImagesState())
        private set

    init {
        loadDogImages()
    }

    private fun loadDogImages() {
        viewModelScope.launch {
            dogBreedImagesState = dogBreedImagesState.copy(
                imageList = fetchDogBreedImagesUseCase.invoke(breed = breed)
            )
        }

    }
}