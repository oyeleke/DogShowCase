package com.example.dogsshowcase.features_components.data.repository

import com.example.dogsshowcase.MainDispatchRule
import com.example.dogsshowcase.Util.MockResponseFileReader
import com.example.dogsshowcase.features_components.data.models.DogBreedResponse
import com.example.dogsshowcase.features_components.data.remote.DogBreedsApi
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

class DogBreedsRepositoryImplTest {

    @get:Rule
    val mainDispatchRule = MainDispatchRule()

    private lateinit var dogBreedsRepository: DogBreedsRepository

    private lateinit var api: DogBreedsApi

    private lateinit var mockWebServer: MockWebServer

    private val contentType = "application/json".toMediaType()

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    private val errorResponse = "error_response.json"

    private val successJsonFileName = "get_dog_breeds_success.json"


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
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchDogBreeds returns Success  if successful and dog breeds not empty`() =
        runTest {
            val responseJson = MockResponseFileReader(successJsonFileName).content
            mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))
            val resourceEventsList = dogBreedsRepository.fetchDogBreeds().toList()
            assertThat(resourceEventsList).isNotEmpty()
            assertThat(resourceEventsList.size).isEqualTo(2)
            assertThat(resourceEventsList.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(resourceEventsList.last()).isInstanceOf(Resource.Success::class.java)
            assertThat((resourceEventsList.last() as Resource.Success<DogBreedResponse>).data.message).isNotEmpty()
        }

    @Test
    fun `fetchDogBreeds returns Error if api call is unSuccessful`() = runTest {
        val responseJson = MockResponseFileReader(errorResponse).content
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(401))
        val resourceEventsList = dogBreedsRepository.fetchDogBreeds().toList()
        assertThat(resourceEventsList).isNotEmpty()
        assertThat(resourceEventsList.size).isEqualTo(2)
        assertThat(resourceEventsList.first()).isInstanceOf(Resource.Loading::class.java)
        assertThat(resourceEventsList.last()).isInstanceOf(Resource.Error::class.java)
        assertThat((resourceEventsList.last() as Resource.Error<DogBreedResponse>).error).isNotNull()
    }
}