package oth.wit.kapk_project.adapters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import oth.wit.kapk_project.databinding.ActivityFoodListBinding
import oth.wit.kapk_project.databinding.CardFoodBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel

interface FoodListener {
    fun onPlacemarkClick(food: FoodModel)
}

class FoodAdapter constructor(private var foods: List<FoodModel>,
                              private val listener: FoodListener) :
    RecyclerView.Adapter<FoodAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFoodBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = foods[holder.adapterPosition]
        holder.bind(placemark, listener)
    }

    override fun getItemCount(): Int = foods.size

    class MainHolder(private val binding : CardFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodModel, listener : FoodListener) {
            binding.brand.text = food.brand
            binding.productName.text = food.productName
            binding.root.setOnClickListener { listener.onPlacemarkClick(food) }
        }
    }
}