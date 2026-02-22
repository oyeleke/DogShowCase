package com.example.dogsshowcase.features_presentation.dog_breed_images.viewmodel

import com.example.dogsshowcase.features_components.data.models.DogImagesResponse
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DogBreedImagesState(
    val imageList: Flow<Resource<DogImagesResponse>> = emptyFlow(),
)