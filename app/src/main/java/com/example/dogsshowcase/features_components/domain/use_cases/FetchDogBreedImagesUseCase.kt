package com.example.dogsshowcase.features_components.domain.use_cases

import com.example.dogsshowcase.features_components.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FetchDogBreedImagesUseCase(
    private val repository: DogBreedsRepository
) {
    operator fun invoke(breed: String): Flow<Resource<List<String>>> {
        if(breed.isEmpty()){
            return flowOf(Resource.Error(error = IllegalArgumentException("Breed must not be empty")))
        }
        return repository.fetchDogBreedsImages(breed = breed, numberOfImages = 10).map { res ->
            when (res) {
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(error = res.error)
                is Resource.Success -> if (res.data.message.isEmpty()) Resource.Error(
                    error = IllegalStateException(
                        "No Images Gotten For Breed status: ${res.data.status}"
                    )
                ) else Resource.Success(data = res.data.message)
            }
        }
    }
}