package com.example.dogsshowcase.features_presentation.dog_breed

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dogsshowcase.features_presentation.dog_breed.viewmodel.DogBreedViewModel
import com.example.dogsshowcase.utils.Resource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogBreedsScreen(
    dogBreedViewModel: DogBreedViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit
) {

    val dogBreedStateList: Resource<List<String>> by dogBreedViewModel
        .dogBreedState
        .dogBreeds
        .collectAsStateWithLifecycle(
            initialValue = Resource.Loading()
        )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Dog Breeds",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                },
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (dogBreedStateList) {
                is Resource.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is Resource.Success -> {
                    val dogBreeds = (dogBreedStateList as Resource.Success<List<String>>).data
                    items(count = dogBreeds.size) { value ->
                        DogBreedItem(
                            breed = dogBreeds[value],
                            onClick = { clickedItem -> onItemClick(clickedItem) })
                    }
                }

                is Resource.Error -> {
                    item {
                        Toast.makeText(
                            LocalContext.current,
                            (dogBreedStateList as Resource.Error).error?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Log.e(
                        "Detail Screen",
                        "DetailScreen: ",
                        (dogBreedStateList as Resource.Error).error
                    )
                }
            }

        }
    }

}

@Composable
fun DogBreedItem(breed: String, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                onClick(breed)
            }
    ) {
        Text(
            text = breed,
            color = Color.Black,
            modifier = Modifier.padding(paddingValues = PaddingValues(bottom = 5.dp))
        )
        HorizontalDivider()

    }
}