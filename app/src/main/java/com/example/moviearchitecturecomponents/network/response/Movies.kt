package com.example.moviearchitecturecomponents.network.response


import com.squareup.moshi.Json

data class Movies(
    @Json(name = "page")
    val page: Int? = null,
    @Json(name = "results")
    val results: List<Result>? = null,
    @Json(name = "total_pages")
    val totalPages: Int? = null,
    @Json(name = "total_results")
    val totalResults: Int? = null
)