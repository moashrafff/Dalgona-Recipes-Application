package com.example.dalgona.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dalgona.data.network.models.FoodRecipesResponse
import com.example.dalgona.utils.Constants.RECIPES_TABLE


@Entity(tableName = RECIPES_TABLE)
data class RecipesEntity(
    var foodRecipe: FoodRecipesResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}