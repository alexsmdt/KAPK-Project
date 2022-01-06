package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.snackbar.Snackbar
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityMainBinding
import oth.wit.kapk_project.databinding.ActivityNutritionalValuesBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.NutritionalValues
import oth.wit.kapk_project.models.ServingSize
import timber.log.Timber

class NutritionalValuesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNutritionalValuesBinding
    lateinit var app: MainApp
    var food = FoodModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityNutritionalValuesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        food = intent.getParcelableExtra<FoodModel>("food")!!   //To-Do überarbeiten
        binding.toolbarCreate.title = title
        setSupportActionBar(binding.toolbarCreate)


        app = application as MainApp

        /*
        if (intent.hasExtra("food_edit")) {
            edit = true
            food = intent.extras?.getParcelable("food_edit")!!
            binding.brand.setText(food.brand)
            binding.productName.setText(food.productName)
            binding.productCategory.setText(food.productCategory)
            binding.barcode.setText(food.barcode)
        }

         */

        binding.btnCreate.setOnClickListener() {
            val nutritionalValues = NutritionalValues()
            nutritionalValues.calories = binding.calories.text.toString().toInt()
            nutritionalValues.fat = binding.fat.text.toString().toInt()
            nutritionalValues.protein = binding.protein.text.toString().toInt()
            val sz = binding.servingSize.text.toString().split(" ")
            nutritionalValues.servingSize = ServingSize(sz[0].toInt(), sz[1])

            if (nutritionalValues.servingSize.equals(ServingSize()) ) {
                Snackbar
                    .make(it,R.string.enter_nutritional_information, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                app.foods.addNutritionalInformation(nutritionalValues.copy(), food)
            }
            Timber.i("continue Button Pressed: $food.brand ${food.productName}")
            setResult(RESULT_OK)
            finish()
        }
    }

}