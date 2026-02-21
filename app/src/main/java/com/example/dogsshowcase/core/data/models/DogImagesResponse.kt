package com.example.dogsshowcase.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogImagesResponse(
    @SerialName("message")
    val message: List<String>,
    @SerialName("status")
    val status: String
)