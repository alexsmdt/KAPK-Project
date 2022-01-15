package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityMainBinding
import oth.wit.kapk_project.databinding.ActivityOverviewBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import timber.log.Timber

class OverviewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOverviewBinding
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarOverview.title = "Heute"
        setSupportActionBar(binding.toolbarOverview)


        app = application as MainApp

        setTextViews()

        binding.breakfastButton.setOnClickListener() {
            buttonPressed("breakfast")
        }

        binding.lunchButton.setOnClickListener() {
            buttonPressed("lunch")
        }

        binding.dinnerButton.setOnClickListener() {
            buttonPressed("dinner")
        }

        binding.snacksButton.setOnClickListener() {
            buttonPressed("snacks")
        }


    }

    private fun buttonPressed (meal : String) {
        Timber.i("button pressed: %s", meal)
        val launcherIntent = Intent(this, FoodListActivity::class.java)
        launcherIntent.putExtra("meal", meal)
        startActivity(launcherIntent)
    }

    private fun sumCalories() : Int{
        var count = 0
        for (food in app.consumedFoods.findAll()) {
            count += food.nutritionalValues.calories
        }
        return count
    }

    private fun sumCarbs() : Int{
        var count = 0
        for (food in app.consumedFoods.findAll()) {
            count += food.nutritionalValues.carbs
        }
        return count
    }

    private fun sumProtein() : Int{
        var count = 0
        for (food in app.consumedFoods.findAll()) {
            count += food.nutritionalValues.protein
        }
        return count
    }

    private fun sumFat() : Int{
        var count = 0
        for (food in app.consumedFoods.findAll()) {
            count += food.nutritionalValues.fat
        }
        return count
    }

    private fun setTextViews() {
        binding.caloriesTextView.setText(sumCalories())
        binding.carbsTextNumber.setText(sumCarbs())
        binding.proteinTextNumber.setText(sumProtein())
        binding.fatTextNumber.setText(sumFat())
    }
}