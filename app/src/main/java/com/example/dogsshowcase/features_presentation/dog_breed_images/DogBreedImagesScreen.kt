package com.example.dogsshowcase.features_presentation.dog_breed_images

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dogsshowcase.features_presentation.dog_breed_images.viewmodel.DogBreedImagesViewModel
import com.example.dogsshowcase.utils.Resource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogBreedImagesScreen(
    dogBreedImagesViewModel: DogBreedImagesViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val dogBreedImagesState: Resource<List<String>> by dogBreedImagesViewModel
        .dogBreedImagesState
        .imageList
        .collectAsStateWithLifecycle(initialValue = Resource.Loading())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${dogBreedImagesViewModel.breed.replaceFirstChar { it.uppercase() }} Images",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (dogBreedImagesState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                }

                is Resource.Success -> {
                    val dogBreedImages =
                        (dogBreedImagesState as Resource.Success<List<String>>).data
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(dogBreedImages) { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f).size(64.dp)
                                    .clip(RoundedCornerShape(12.dp)), // Makes square images
                            )
                        }
                    }


                }

                is Resource.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "warning",
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            (dogBreedImagesState as Resource.Error).error?.message?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            }


                        }
                    }
                    Toast.makeText(
                        LocalContext.current,
                        (dogBreedImagesState as Resource.Error).error?.message,
                        Toast.LENGTH_SHORT
                    ).show()


                    Log.e(
                        "Detail Screen",
                        "DetailScreen: ",
                        (dogBreedImagesState as Resource.Error).error
                    )
                }
            }
        }

    }

}