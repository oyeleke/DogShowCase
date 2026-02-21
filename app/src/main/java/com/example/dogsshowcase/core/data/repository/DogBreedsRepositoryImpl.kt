package com.example.dogsshowcase.core.data.repository

import com.example.dogsshowcase.core.data.models.DogBreedResponse
import com.example.dogsshowcase.core.data.models.DogImagesResponse
import com.example.dogsshowcase.core.data.remote.DogBreedsApi
import com.example.dogsshowcase.core.domain.repository.DogBreedsRepository
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
        } catch (error: Exception) {
            trySend(Resource.Error(error = error))
        }
        awaitClose { }
    }

    override fun fetchDogBreedsImages(numberOfImages: Int): Flow<Resource<DogImagesResponse>> =
        callbackFlow {
            trySend(Resource.Loading())
            try {
                val res = api.getRandomDogImages(numberOfImages = numberOfImages)
                trySend(Resource.Success(data = res))
            } catch (error: Exception) {
                trySend(Resource.Error(error = error))
            }
            awaitClose {  }
        }
}