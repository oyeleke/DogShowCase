package com.example.dogsshowcase.core.domain.repository

import com.example.dogsshowcase.core.data.models.DogBreedResponse
import com.example.dogsshowcase.core.data.models.DogImagesResponse
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DogBreedsRepository {

    fun fetchDogBreeds(): Flow<Resource<DogBreedResponse>>

    fun fetchDogBreedsImages(numberOfImages: Int): Flow<Resource<DogImagesResponse>>
}