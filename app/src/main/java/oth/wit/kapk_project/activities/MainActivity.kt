package oth.wit.kapk_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityMainBinding
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.main.MainApp
import timber.log.Timber
import timber.log.Timber.i

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var food = FoodModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarContinue.title = title

        Timber.plant(Timber.DebugTree())

        binding.btnContinue.setOnClickListener() {
            food.brand = binding.brand.text.toString()
            food.productName = binding.productName.text.toString()
            food.productCategory = binding.productCategory.text.toString()
            food.barcode = binding.barcode.text.toString()

            if (food.brand.isEmpty() || food.productName.isNotEmpty()) {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
                i("continue Button Pressed: $food.brand ${food.productName}")
            } else {
                app.foods.create(food.copy())
            }
            i("continue Button Pressed: $food.brand ${food.productName}")
            setResult(RESULT_OK)
            finish()
        }
    }
}