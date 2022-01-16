package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityNutritionalValuesBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.NutritionalValues
import oth.wit.kapk_project.models.ServingSize
import oth.wit.kapk_project.models.UnitSpecification
import timber.log.Timber.i

class NutritionalValuesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNutritionalValuesBinding
    lateinit var app: MainApp
    var food = FoodModel()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityNutritionalValuesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        food = intent.getParcelableExtra<FoodModel>("food")!!   //To-Do überarbeiten
        binding.toolbarCreate.title = title
        setSupportActionBar(binding.toolbarCreate)


        app = application as MainApp

        i("before intent usage")

        if (intent.hasExtra("food_edit")) {
            edit = true
            binding.calories.setText(food.nutritionalValues.caloriesInKcal.toString())
            binding.fat.setText(food.nutritionalValues.fatInG.toString())
            binding.protein.setText(food.nutritionalValues.proteinInG.toString())
            binding.carbs.setText(food.nutritionalValues.carbsInG.toString())
            binding.servingSize.setText(food.nutritionalValues.servingSize.amount.toString() + " " + food.nutritionalValues.servingSize.unit)
        }


        binding.btnCreate.setOnClickListener() {
            val nutritionalValues = NutritionalValues()
            nutritionalValues.caloriesInKcal = binding.calories.text.toString().toDouble()
            nutritionalValues.fatInG = binding.fat.text.toString().toDouble()
            nutritionalValues.proteinInG = binding.protein.text.toString().toDouble()
            nutritionalValues.carbsInG = binding.carbs.text.toString().toDouble()
            val sz = binding.servingSize.text.toString().split(" ")
            nutritionalValues.servingSize = ServingSize(sz[0].toDouble(), UnitSpecification.GRAM)
            /*
            if (nutritionalValues.servingSize == ServingSize()) {
                Snackbar
                    .make(it,R.string.enter_nutritional_information, Snackbar.LENGTH_LONG)
                    .show()
            } else {

             */
            food.nutritionalValues = nutritionalValues
            app.foods.add(food)
            i("ALEX $food")

            i("continue Button Pressed: $food.brand ${food.productName}")
            setResult(RESULT_OK)
            finish()
        }
    }

}