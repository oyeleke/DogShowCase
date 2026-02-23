package com.example.dogsshowcase.features_components.domain.use_cases

import com.example.dogsshowcase.features_components.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchDogBreedsUseCase(
    private val repository: DogBreedsRepository
) {
    operator fun invoke(): Flow<Resource<List<String>>> {
        return repository.fetchDogBreeds().map { res ->
            when (res) {
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(error = res.error)
                is Resource.Success -> if (res.data.message.isEmpty()) Resource.Error(
                    error = IllegalStateException(
                        "We couldn’t find any dog breeds."
                    )
                ) else Resource.Success(data = res.data.message.keys.toList())
            }
        }
    }
}