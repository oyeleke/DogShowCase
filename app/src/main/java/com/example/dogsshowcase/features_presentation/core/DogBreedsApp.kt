package com.example.dogsshowcase.features_presentation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.dogsshowcase.features_presentation.core.navigation.DogBreedNavigationActions
import com.example.dogsshowcase.features_presentation.core.navigation.DogBreedsNavigationGraph

@Composable
fun DogBreedsApp() {
    val navController = rememberNavController()
    val navActions = remember { DogBreedNavigationActions(navController) }
    DogBreedsNavigationGraph(
        navController = navController,
        navActions = navActions,
    )
}