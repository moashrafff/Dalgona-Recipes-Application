package com.example.dalgona.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dalgona.data.network.models.FoodJokeResponse
import com.example.dalgona.utils.Constants.FOOD_JOKE_TABLE



@Entity(tableName = FOOD_JOKE_TABLE)
data class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJokeResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}