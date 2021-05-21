package com.example.moviearchitecturecomponents.network.response


import com.squareup.moshi.Json

data class Credits(
    @Json(name = "cast")
    val cast: List<Cast>? = null,
    @Json(name = "crew")
    val crew: List<Crew>? = null
)