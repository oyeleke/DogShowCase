package com.example.dogsshowcase.features_components.data.repository

import com.example.dogsshowcase.features_components.data.models.DogBreedResponse
import com.example.dogsshowcase.features_components.data.models.DogImagesResponse
import com.example.dogsshowcase.features_components.data.remote.DogBreedsApi
import com.example.dogsshowcase.features_components.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DogBreedsRepositoryImpl(
    private val api: DogBreedsApi
) : DogBreedsRepository {

    override fun fetchDogBreeds(): Flow<Resource<DogBreedResponse>> = callbackFlow {
        trySend(Resource.Loading())
        try {
            val dogBreeds = api.getAllDogBreeds()
            trySend(Resource.Success(data = dogBreeds))
            close()
        } catch (error: Exception) {
            trySend(Resource.Error(error = error))
            close()
        }
        awaitClose { }
    }

    override fun fetchDogBreedsImages(
        breed: String,
        numberOfImages: Int
    ): Flow<Resource<DogImagesResponse>> =
        callbackFlow {
            trySend(Resource.Loading())
            try {
                val res = api.getRandomDogImages(breed = breed, numberOfImages = numberOfImages)
                trySend(Resource.Success(data = res))
                close()
            } catch (error: Exception) {
                trySend(Resource.Error(error = error))
                close()
            }
            awaitClose { }
        }
}