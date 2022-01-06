package oth.wit.kapk_project.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.snackbar.Snackbar
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityMainBinding
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.main.MainApp
import timber.log.Timber
import timber.log.Timber.i

class FoodCreateActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var food = FoodModel()
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarContinue.title = title
        setSupportActionBar(binding.toolbarContinue)


        app = application as MainApp

        i("Placemark Activity started...")

        if (intent.hasExtra("food_edit")) {
            edit = true
            food = intent.extras?.getParcelable("food_edit")!!
            binding.brand.setText(food.brand)
            binding.productName.setText(food.productName)
            binding.productCategory.setText(food.productCategory)
            binding.barcode.setText(food.barcode)
        }

        binding.btnContinue.setOnClickListener(){

            food.brand = binding.brand.text.toString()
            food.productName = binding.productName.text.toString()
            food.productCategory = binding.productCategory.text.toString()
            food.barcode = binding.barcode.text.toString()

            if (food.brand.isEmpty() || food.productName.isEmpty()) {
                Snackbar
                    .make(it,R.string.enter_food_information, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.foods.update(food.copy())
                } else {
                    app.foods.create(food.copy())
                }
            }
            i("continue Button Pressed: $food.brand ${food.productName}")
            setResult(RESULT_OK)
            onContinueButtonPressed()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_food, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }

    //TO-DO: rename this method
    private fun onContinueButtonPressed() {
        val launcherIntent = Intent(this, NutritionalValuesActivity::class.java)
        launcherIntent.putExtra("food", food)
        startActivity(launcherIntent)
        //refreshIntentLauncher.launch(launcherIntent)
    }
}