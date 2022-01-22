package oth.wit.kapk_project.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import oth.wit.kapk_project.databinding.CardFoodBinding
import oth.wit.kapk_project.models.FoodModel

interface ConsumedFoodListener {
    fun onFoodClick(food : FoodModel)
}

class ConsumedFoodAdapter constructor(private var foods: MutableList<FoodModel>,
                              private val listener: ConsumedFoodListener) :
    RecyclerView.Adapter<ConsumedFoodAdapter.MainHolder>() {

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

        fun bind(food: FoodModel, listener : ConsumedFoodListener) {
            binding.brand.text = food.brand
            binding.productName.text = food.productName
            binding.root.setOnClickListener { listener.onFoodClick(food) }
        }
    }
}