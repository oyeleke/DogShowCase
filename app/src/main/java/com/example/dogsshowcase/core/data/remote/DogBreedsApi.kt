package com.example.dogsshowcase.core.data.remote

import com.example.dogsshowcase.core.data.models.DogBreedResponse
import com.example.dogsshowcase.core.data.models.DogImagesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DogBreedsApi {
    companion object {
        private const val LIST_ALL_BREEDS_ENDPOINT = "/breeds/list/all"
        private const val GET_RANDOM_DOG_IMAGES_ENDPOINT = "/breeds/image/random"
    }

    @GET(LIST_ALL_BREEDS_ENDPOINT)
    suspend fun getAllDogBreeds(): DogBreedResponse

    @GET(GET_RANDOM_DOG_IMAGES_ENDPOINT)
    suspend fun getRandomDogImages(@Path("numberOfImages") numberOfImages: Int): DogImagesResponse
}