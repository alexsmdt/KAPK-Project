package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import oth.wit.kapk_project.databinding.ActivityMainMenuBinding
import oth.wit.kapk_project.main.MainApp
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
            buttonPressed("Breakfast")
        }

        binding.lunchButton.setOnClickListener() {
            buttonPressed("Lunch")
        }

        binding.dinnerButton.setOnClickListener() {
            buttonPressed("Dinner")
        }

        binding.snacksButton.setOnClickListener() {
            buttonPressed("Snacks")
        }

        registerRefreshCallback()
    }

    private fun buttonPressed (meal : String) {
        i("ALEX button pressed")
        Timber.i("button pressed: %s", meal)
        val launcherIntent = Intent(this, FoodListActivity::class.java)
        launcherIntent.putExtra("meal", meal)
        refreshIntentLauncher.launch(launcherIntent)
    }


    @SuppressLint("SetTextI18n")
    private fun setTextViews() {
        i("ALEX setTextViews")
        val nv = app.consumedFoods.sumNutritionalValues()
        binding.caloriesTextView.text = "${nv.caloriesInKcal} kcal"
        binding.carbsTextNumber.setText("${nv.carbsInG} g")
        binding.proteinTextNumber.setText("${nv.proteinInG} g")
        binding.fatTextNumber.setText("${nv.fatInG} g")
    }

    private fun registerRefreshCallback() {
        i("ALEX registerRefreshCallback")
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {setTextViews()}
    }


}