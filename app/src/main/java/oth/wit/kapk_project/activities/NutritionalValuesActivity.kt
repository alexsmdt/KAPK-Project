package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var spinner : Spinner
    private var unitSpecification: UnitSpecification = UnitSpecification.GRAM


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNutritionalValuesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        food = intent.getParcelableExtra("food")!!
        binding.toolbarCreate.title = "Nutritional Values " + food.productName
        setSupportActionBar(binding.toolbarCreate)


        app = application as MainApp



        spinner = binding.spinner

        val units = arrayOf("g", "ml")

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, units)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position) {
                    0 -> unitSpecification = UnitSpecification.GRAM
                    1 -> unitSpecification = UnitSpecification.MILLILITER
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        i("before intent usage")

        binding.btnCreate.setOnClickListener {
            val nutritionalValues = NutritionalValues()
            //nutritionalValues.caloriesInKcal = binding.calories.text.toString().toDouble()

            var value = binding.calories.text.toString().toDoubleOrNull()
            if (value == null) {
                Snackbar.make(it, "Calories: Not a number", Snackbar.LENGTH_LONG).show()
            } else {
                nutritionalValues.caloriesInKcal = value
                value = binding.fat.text.toString().toDoubleOrNull()
                if ( value == null ) {
                    Snackbar.make(it, "Fat: Not a number", Snackbar.LENGTH_LONG).show()
                } else {
                    nutritionalValues.fatInG = value
                    value = binding.protein.text.toString().toDoubleOrNull()
                    if ( value == null ) {
                        Snackbar.make(it, "Protein: Not a number", Snackbar.LENGTH_LONG).show()
                    } else {
                        nutritionalValues.proteinInG = value
                        value = binding.carbs.text.toString().toDoubleOrNull()
                        if ( value == null ) {
                            Snackbar.make(it, "Carbs: Not a number", Snackbar.LENGTH_LONG).show()
                        } else {
                            nutritionalValues.carbsInG = value
                            value = binding.servingSize.text.toString().toDoubleOrNull()
                            if ( value == null ) {
                                Snackbar.make(it, "Serving size: Not a number", Snackbar.LENGTH_LONG).show()
                            } else {
                                nutritionalValues.servingSize = ServingSize(value, unitSpecification)
                                food.nutritionalValues = nutritionalValues
                                app.foods.add(food)
                                i("ALEX $food")
                                setResult(RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }

            i("ALEX continue Button Pressed")

        }
    }

}