package com.example.moviearchitecturecomponents.network.response


import com.squareup.moshi.Json

data class Video(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "iso_3166_1")
    val iso31661: String? = null,
    @Json(name = "iso_639_1")
    val iso6391: String? = null,
    @Json(name = "key")
    val key: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "site")
    val site: String? = null,
    @Json(name = "size")
    val size: Int? = null,
    @Json(name = "type")
    val type: String? = null
)