package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import oth.wit.kapk_project.databinding.ActivityMainMenuBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.MealType
import oth.wit.kapk_project.models.NutritionalValues
import oth.wit.kapk_project.models.ServingSize
import timber.log.Timber
import timber.log.Timber.i

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarOverview.title = "Today"
        setSupportActionBar(binding.toolbarOverview)


        app = application as MainApp

        setTextViews()

        binding.breakfastButton.setOnClickListener() {
            buttonPressed(MealType.BREAKFAST)
        }

        binding.lunchButton.setOnClickListener() {
            buttonPressed(MealType.LUNCH)
        }

        binding.dinnerButton.setOnClickListener() {
            buttonPressed(MealType.DINNER)
        }

        binding.snacksButton.setOnClickListener() {
            buttonPressed(MealType.SNACKS)
        }

        registerRefreshCallback()
    }

    private fun buttonPressed (meal : MealType) {
        i("ALEX button pressed")
        Timber.i("button pressed: %s", meal)
        var launcherIntent = Intent(this, FoodListActivity::class.java)
        if (meal == MealType.BREAKFAST) {
            launcherIntent = Intent(this, MealListActivity::class.java)
        }
        launcherIntent.putExtra("meal", meal.name)
        refreshIntentLauncher.launch(launcherIntent)
    }


    @SuppressLint("SetTextI18n")
    private fun setTextViews() {
        i("ALEX setTextViews")
        val nv = app.consumedFoods.sumNutritionalValues()
        binding.caloriesTextView.text = String.format("%.1f", nv.caloriesInKcal) + " kcal"
        binding.carbsTextNumber.setText(String.format("%.1f", nv.carbsInG) + " g")
        binding.proteinTextNumber.setText(String.format("%.1f", nv.proteinInG) + " g")
        binding.fatTextNumber.setText(String.format("%.1f", nv.fatInG) + " g")

        binding.carbsProgressBar.progress = nv.carbsInG.toInt()
        binding.proteinProgressBar.progress = nv.proteinInG.toInt()
        binding.fatProgressBar.progress = nv.fatInG.toInt()
        binding.caloriesProgressBar.progress = nv.caloriesInKcal.toInt()

        if (binding.caloriesProgressBar.progress > binding.caloriesProgressBar.max) {
            ColorStateList.valueOf(Color.RED)
                .also { binding.caloriesProgressBar.progressTintList = it }
        }
        binding.caloriesProgressBar
    }

    private fun registerRefreshCallback() {
        i("ALEX registerRefreshCallback")
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {setTextViews()}
    }


}