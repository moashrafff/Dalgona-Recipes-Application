package com.example.dalgona.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dalgona.utils.Constants.FAVORITE_RECIPES_TABLE

import com.example.dalgona.data.network.models.Result


@Entity(tableName = FAVORITE_RECIPES_TABLE)
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)