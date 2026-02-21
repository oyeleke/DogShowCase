package com.example.dogsshowcase.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DogBreedResponse(
    @SerialName("message")
    val message: Map<String, List<String>>,

    @SerialName("status")
    val status: String
)