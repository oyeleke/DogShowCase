package com.example.dogsshowcase.features_components.domain.repository

import com.example.dogsshowcase.features_components.data.models.DogBreedResponse
import com.example.dogsshowcase.features_components.data.models.DogImagesResponse
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DogBreedsRepository {

    fun fetchDogBreeds(): Flow<Resource<DogBreedResponse>>

    fun fetchDogBreedsImages(breed: String, numberOfImages: Int): Flow<Resource<DogImagesResponse>>
}