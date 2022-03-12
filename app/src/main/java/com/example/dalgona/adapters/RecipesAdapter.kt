package com.example.dalgona.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


import com.example.dalgona.data.network.models.FoodRecipesResponse
import com.example.dalgona.data.network.models.Result
import com.example.dalgona.databinding.RecipesRowLayoutBinding

import com.example.dalgona.utils.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    var recipes = listOf<Result>()

    fun setData(newData : FoodRecipesResponse){
        val recipeDiffUtil = RecipesDiffUtil(recipes,newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipeDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(result: Result){
                binding.result = result
                binding.executePendingBindings()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecipesRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = recipes[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int = recipes.size
}