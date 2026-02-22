package com.example.dogsshowcase.features_presentation.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class DogBreedNavigationActions(
    navController: NavController
) {

    val navigateToHome: () -> Unit = {
        navController.navigateToSingleTop(
            UiScreen.DogBreedScreen().route
        )
    }

    val navigateToDogBreedImages: (dogBreed: String) -> Unit = { breed ->
        navController.navigateToSingleTop(
            route = UiScreen.DogBreedImagesScreen(
                breed = breed
            ).routeWithArgs
        )
    }
}

fun NavController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}