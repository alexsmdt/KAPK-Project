package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityFoodAddBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel

class FoodAddActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFoodAddBinding
    var food = FoodModel()
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoodAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.toolbarContinue.title = title
        //setSupportActionBar(binding.toolbarContinue)

        app = application as MainApp

        food = intent.getParcelableExtra<FoodModel>("food")!!

        binding.servingSizeEditTextNumber.setText(food.nutritionalValues.servingSize.amount)
        binding.unitTextView.text = food.nutritionalValues.servingSize.unit

        binding.caloriesEditTextNumber.setText(food.nutritionalValues.calories)
        binding.carbsEditTextNumber.setText(food.nutritionalValues.carbs)
        binding.proteinEditTextNumber.setText(food.nutritionalValues.protein)
        binding.fatEditTextNumber.setText(food.nutritionalValues.fat)



        binding.button.setOnClickListener() {
            var factor = binding.servingSizeEditTextNumber.text.toString().toInt() / 100
            // kalorien, carbs, proteine und fat mit faktor multiplizieren
            app.consumedFoods.changeNutritionalInformation(factor, food.id)
            // food item zu FoodList der konsumierten Produkte hinzufügen
            app.consumedFoods.add(food)
        }

        setResult(RESULT_OK)
        finish()
    }
}