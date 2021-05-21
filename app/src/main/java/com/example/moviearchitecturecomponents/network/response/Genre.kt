package com.example.moviearchitecturecomponents.network.response


import com.squareup.moshi.Json

data class Genre(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "name")
    val name: String? = null
)