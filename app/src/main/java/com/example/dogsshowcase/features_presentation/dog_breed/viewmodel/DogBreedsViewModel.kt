package com.example.dogsshowcase.features_presentation.dog_breed.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dogsshowcase.features_components.domain.use_cases.FetchDogBreedsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DogBreedViewModel @Inject constructor(
    private val dogBreedUseCase: FetchDogBreedsUseCase,
) : ViewModel() {

    var dogBreedState by mutableStateOf(DogBreedState())
        private set
}
