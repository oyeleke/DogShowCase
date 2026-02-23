package com.example.dogsshowcase.features_components.domain.use_cases

import com.example.dogsshowcase.MainDispatchRule
import com.example.dogsshowcase.Util.MockResponseFileReader
import com.example.dogsshowcase.features_components.data.remote.DogBreedsApi
import com.example.dogsshowcase.features_components.data.repository.DogBreedsRepositoryImpl
import com.example.dogsshowcase.features_components.domain.repository.DogBreedsRepository
import com.example.dogsshowcase.utils.Resource
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

class FetchDogBreedImagesUseCaseTest {

    @get:Rule
    val mainDispatchRule = MainDispatchRule()

    private lateinit var api: DogBreedsApi
    private lateinit var mockWebServer: MockWebServer
    private lateinit var dogBreedsRepository: DogBreedsRepository
    private lateinit var fetchDogBreedImagesUseCase: FetchDogBreedImagesUseCase
    private val contentType = "application/json".toMediaType()

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    private val successJsonFileName = "get_dog_images_success.json"
    private val successEmptyJsonFileName = "get_dog_images_empty_images_response.json"


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(DogBreedsApi::class.java)
        dogBreedsRepository = DogBreedsRepositoryImpl(api)
        fetchDogBreedImagesUseCase = FetchDogBreedImagesUseCase(dogBreedsRepository)
    }

    @Test
    fun `fetchDogBreedImagesUseCase returns dog breed images data correctly`() = runTest {
        val responseJson = MockResponseFileReader(successJsonFileName).content
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))
        val invokeEventsList = fetchDogBreedImagesUseCase.invoke("breed").toList()
        assertThat(invokeEventsList).isNotEmpty()
        assertThat(invokeEventsList.size).isEqualTo(2)
        assertThat(invokeEventsList.first()).isInstanceOf(Resource.Loading::class.java)
        assertThat(invokeEventsList.last()).isInstanceOf(Resource.Success::class.java)
        assertThat((invokeEventsList.last() as Resource.Success<List<String>>).data)
            .isNotEmpty()

    }

    @Test
    fun `fetchDogBreedImagesUseCase returns an exception if dog breed is empty`() = runTest {
        val responseJson = MockResponseFileReader(successJsonFileName).content
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))
        val invokeEventsList = fetchDogBreedImagesUseCase.invoke("").toList()
        assertThat(invokeEventsList).isNotEmpty()
        assertThat(invokeEventsList.size).isEqualTo(1)
        assertThat(invokeEventsList.first()).isInstanceOf(Resource.Error::class.java)
        assertThat((invokeEventsList.first() as Resource.Error<List<String>>).error)
            .isNotNull()
    }

    @Test
    fun `fetchDogBreedImagesUseCase returns an exception if image list is empty`() = runTest {
        val responseJson = MockResponseFileReader(successEmptyJsonFileName).content
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))
        val invokeEventsList = fetchDogBreedImagesUseCase.invoke("breed").toList()
        assertThat(invokeEventsList).isNotEmpty()
        assertThat(invokeEventsList.size).isEqualTo(2)
        assertThat(invokeEventsList.first()).isInstanceOf(Resource.Loading::class.java)
        assertThat(invokeEventsList.last()).isInstanceOf(Resource.Error::class.java)
        assertThat((invokeEventsList.last() as Resource.Error<List<String>>).error?.message)
            .isNotEmpty()
        assertThat((invokeEventsList.last() as Resource.Error<List<String>>).error).isInstanceOf(
            IllegalStateException::class.java
        )

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}