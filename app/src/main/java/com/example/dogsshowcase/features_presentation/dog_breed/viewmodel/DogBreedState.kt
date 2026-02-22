package com.example.dogsshowcase.features_presentation.dog_breed.viewmodel

import com.example.dogsshowcase.features_components.data.models.DogBreedResponse
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DogBreedState (
    val dogBreeds : Flow<Resource<DogBreedResponse>> = emptyFlow(),
)