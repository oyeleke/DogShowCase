package com.example.dogsshowcase.features_presentation.dog_breed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dogsshowcase.features_components.data.models.DogBreedResponse
import com.example.dogsshowcase.features_presentation.dog_breed.viewmodel.DogBreedViewModel
import com.example.dogsshowcase.utils.Resource


@Composable
fun DogBreedsScreen(
    dogBreedViewModel: DogBreedViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit
) {

    val dogBreedState: Resource<DogBreedResponse> by dogBreedViewModel
        .dogBreedState
        .dogBreeds
        .collectAsStateWithLifecycle(
            initialValue = Resource.Loading()
        )
}