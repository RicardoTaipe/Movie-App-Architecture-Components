package com.example.moviearchitecturecomponents.network.response


import com.squareup.moshi.Json

data class ProductionCountry(
    @Json(name = "iso_3166_1")
    val iso31661: String? = null,
    @Json(name = "name")
    val name: String? = null
)