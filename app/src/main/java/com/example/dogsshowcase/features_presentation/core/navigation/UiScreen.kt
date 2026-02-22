package com.example.dogsshowcase.features_presentation.core.navigation

import com.example.dogsshowcase.utils.K

sealed class UiScreen {
    data class DogBreedScreen(val route: String = "dog_breed") : UiScreen()
    data class DogBreedImagesScreen(
        val route: String = "dog_breed_images",
        val breed: String = K.DOG_BREED,
        val routeWithArgs: String = "$route/{${breed}}",
    ) : UiScreen()
}