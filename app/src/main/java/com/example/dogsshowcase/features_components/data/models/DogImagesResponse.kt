package com.example.dogsshowcase.features_components.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogImagesResponse(
    @SerialName("message")
    val message: List<String>,
    @SerialName("status")
    val status: String
)