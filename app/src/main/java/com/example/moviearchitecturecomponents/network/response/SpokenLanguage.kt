package com.example.moviearchitecturecomponents.network.response


import com.squareup.moshi.Json

data class SpokenLanguage(
    @Json(name = "english_name")
    val englishName: String? = null,
    @Json(name = "iso_639_1")
    val iso6391: String? = null,
    @Json(name = "name")
    val name: String? = null
)