package com.example.dogsshowcase.features_components.domain.use_cases

import com.example.dogsshowcase.features_components.data.models.DogBreedResponse
import com.example.dogsshowcase.features_components.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow

class FetchDogBreedsUseCase(
    private val repository: DogBreedsRepository
) {
    operator fun invoke(): Flow<Resource<DogBreedResponse>> {
        return repository.fetchDogBreeds()
    }
}