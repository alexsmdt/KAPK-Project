package oth.wit.kapk_project.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oth.wit.kapk_project.databinding.CardFoodBinding
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.FoodStore

interface FoodListener {
    fun onFoodClick(food: FoodModel)
}

class FoodAdapter constructor(private var foods: FoodStore,
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

    override fun getItemCount(): Int {
        return foods.count()
    }

    class MainHolder(private val binding : CardFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodModel, listener : FoodListener) {
            binding.brand.text = food.brand
            binding.productName.text = food.productName
            binding.root.setOnClickListener { listener.onFoodClick(food) }
        }
    }
}