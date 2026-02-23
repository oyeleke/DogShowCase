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

class FetchDogBreedsUseCaseTest {
    @get:Rule
    val mainDispatchRule = MainDispatchRule()

    private lateinit var api: DogBreedsApi
    private lateinit var mockWebServer: MockWebServer
    private lateinit var dogBreedsRepository: DogBreedsRepository
    private lateinit var fetchDogBreedsUseCase: FetchDogBreedsUseCase
    private val contentType = "application/json".toMediaType()

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    private val successJsonFileName = "get_dog_breeds_success.json"
    private val successEmptyJsonFileName = "get_dog_breeds_success_empty_breeds.json"


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
        fetchDogBreedsUseCase = FetchDogBreedsUseCase(dogBreedsRepository)
    }

    @Test
    fun `fetchDogBreedsUseCase returns dog breeds data correctly as a list`() = runTest {
        val responseJson = MockResponseFileReader(successJsonFileName).content
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))
        val invokeEventsList = fetchDogBreedsUseCase.invoke().toList()
        assertThat(invokeEventsList.first()).isInstanceOf(Resource.Loading::class.java)
        assertThat(invokeEventsList.last()).isInstanceOf(Resource.Success::class.java)
        assertThat((invokeEventsList.last() as Resource.Success<List<String>>).data)
            .isNotEmpty()
    }

    @Test
    fun `fetchDogBreedsUseCase returns Illegal State Exception if dog breed response is empty`() =
        runTest {
            val responseJson = MockResponseFileReader(successEmptyJsonFileName).content
            mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))
            val invokeEventsList = fetchDogBreedsUseCase.invoke().toList()
            assertThat(invokeEventsList.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(invokeEventsList.last()).isInstanceOf(Resource.Error::class.java)
            assertThat((invokeEventsList.last() as Resource.Error<List<String>>).error).isInstanceOf(
                IllegalStateException::class.java
            )
        }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}