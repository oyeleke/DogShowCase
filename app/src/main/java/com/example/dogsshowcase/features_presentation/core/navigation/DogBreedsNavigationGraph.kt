package com.example.dogsshowcase.features_presentation.core.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogsshowcase.features_presentation.dog_breed.DogBreedsScreen
import com.example.dogsshowcase.features_presentation.dog_breed_images.DogBreedImagesScreen
import com.example.dogsshowcase.utils.K

@Composable
fun DogBreedsNavigationGraph(
    navController: NavHostController = rememberNavController(),
    navActions: DogBreedNavigationActions,
) {
    NavHost(
        navController = navController,
        startDestination = UiScreen.DogBreedScreen().route
    ) {
        composable(route = UiScreen.DogBreedScreen().route) {
            DogBreedsScreen(
                onItemClick = {
                    navActions.navigateToDogBreedImages(
                        it
                    )
                }
            )
        }

        composable(
            route = UiScreen.DogBreedImagesScreen().routeWithArgs, arguments = listOf(
            navArgument(name = K.DOG_BREED) {
                type = NavType.StringType
            }
        )) {
            DogBreedImagesScreen(
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}