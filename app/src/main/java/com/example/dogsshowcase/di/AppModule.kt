package com.example.dogsshowcase.di

import com.example.dogsshowcase.features_components.data.remote.DogBreedsApi
import com.example.dogsshowcase.features_components.data.repository.DogBreedsRepositoryImpl
import com.example.dogsshowcase.features_components.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.features_components.domain.use_cases.FetchDogBreedImagesUseCase
import com.example.dogsshowcase.features_components.domain.use_cases.FetchDogBreedsUseCase
import com.example.dogsshowcase.utils.K
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val json = Json {
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideDogsBreedApi(): DogBreedsApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(DogBreedsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDogBreedRepository(
        api: DogBreedsApi
    ): DogBreedsRepository {
        return DogBreedsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFetchDogBreedUseCase(
        repository: DogBreedsRepository
    ): FetchDogBreedsUseCase =
        FetchDogBreedsUseCase(repository)


    @Provides
    @Singleton
    fun provideFetchDogBreedImagesUseCase(repository: DogBreedsRepository): FetchDogBreedImagesUseCase =
        FetchDogBreedImagesUseCase(repository)

}