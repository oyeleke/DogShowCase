package com.example.dogsshowcase.core.domain.use_cases

import com.example.dogsshowcase.core.data.models.DogImagesResponse
import com.example.dogsshowcase.core.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow

class FetchDogBreedImagesUseCase(
    private val repository: DogBreedsRepository
) {
    operator fun invoke(): Flow<Resource<DogImagesResponse>> {
        return repository.fetchDogBreedsImages(10)
    }
}