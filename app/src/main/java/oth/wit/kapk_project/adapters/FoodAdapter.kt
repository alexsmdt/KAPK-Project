package oth.wit.kapk_project.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import oth.wit.kapk_project.databinding.CardFoodBinding
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.FoodStore
import java.util.*

interface FoodListener {
    fun onFoodClick(food: FoodModel)
}

class FoodAdapter(private var foods: ArrayList<FoodModel>,
                              private val listener: FoodListener) :
    RecyclerView.Adapter<FoodAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFoodBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val food = foods[holder.adapterPosition]
        holder.bind(food, listener)
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