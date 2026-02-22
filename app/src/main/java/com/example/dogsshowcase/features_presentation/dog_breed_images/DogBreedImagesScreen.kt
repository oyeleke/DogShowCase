package com.example.dogsshowcase.features_presentation.dog_breed_images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dogsshowcase.features_components.data.models.DogImagesResponse
import com.example.dogsshowcase.features_presentation.dog_breed_images.viewmodel.DogBreedImagesViewModel
import com.example.dogsshowcase.utils.Resource


@Composable
fun DogBreedImagesScreen(
    dogBreedImagesViewModel: DogBreedImagesViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val dogBreedImagesState: Resource<DogImagesResponse> by dogBreedImagesViewModel
        .dogBreedImagesState
        .imageList
        .collectAsStateWithLifecycle(initialValue = Resource.Loading())

}