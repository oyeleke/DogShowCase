package com.example.dogsshowcase.features_components.domain.use_cases

import com.example.dogsshowcase.features_components.data.models.DogImagesResponse
import com.example.dogsshowcase.features_components.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow

class FetchDogBreedImagesUseCase(
    private val repository: DogBreedsRepository
) {
    operator fun invoke(breed: String): Flow<Resource<DogImagesResponse>> {
        return repository.fetchDogBreedsImages(breed = breed, numberOfImages = 10)
    }
}