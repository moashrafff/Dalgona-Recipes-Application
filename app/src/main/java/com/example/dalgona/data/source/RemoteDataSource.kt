package com.example.dalgona.data.source

import com.example.dalgona.data.network.models.FoodJokeResponse
import com.example.dalgona.data.network.models.FoodRecipesResponse
import com.example.dalgona.data.network.FoodRecipesApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipesResponse> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipesResponse> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJokeResponse> {
        return foodRecipesApi.getFoodJoke(apiKey)
    }

}