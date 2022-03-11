package com.example.dalgona.data.network.models


data class FoodRecipesResponse(
    val results: List<Result>,
    val totalResults: Int?
)