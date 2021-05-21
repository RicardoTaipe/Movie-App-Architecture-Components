package com.example.moviearchitecturecomponents.network.response


import com.squareup.moshi.Json

data class Videos(
    @Json(name = "results")
    val results: List<Video>? = null
)