package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityAddConsumedFoodBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.MealType
import oth.wit.kapk_project.models.NutritionalValues
import timber.log.Timber.i

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // else {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    // }
}

class AddConsumedFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddConsumedFoodBinding
    lateinit var food : FoodModel
    private lateinit var consumedFood : FoodModel
    lateinit var app: MainApp
    private lateinit var meal : MealType
    private var edit : Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        i("ALEX AddConsumedFoodsActivity started")

        binding = ActivityAddConsumedFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        meal = MealType.valueOf(intent.getStringExtra("meal").toString())
        binding.toolbarAdd.title = meal.printableName
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        food = intent.getParcelableExtra("food")!!
        edit = intent.getBooleanExtra("edit",false)

        consumedFood = food.copy()

        consumedFood.meal = meal

        if (!edit) {
            binding.editImageButton.visibility = INVISIBLE
            binding.deleteImageButton.visibility = INVISIBLE
        }
        else {
            binding.deleteImageButton.setOnClickListener {
                app.consumedFoods.remove(food)
                setResult(RESULT_OK)
                finish()
            }
            binding.editImageButton.setOnClickListener {
                app.consumedFoods.replace(food,consumedFood)
                setResult(RESULT_OK)
                finish()
            }
        }


        i("ALEX $food")

        val nv = consumedFood.nutritionalValues
        val sz = nv.servingSize

        binding.brandTextView.text = consumedFood.brand
        binding.nameTextView.text = consumedFood.productName

        binding.servingSizeEditTextNumber.setText("${sz.amount}")
        binding.unitTextView.text = sz.unit.printableName

        refreshNutritionalValues(nv)

        binding.servingSizeEditTextNumber.setOnEditorActionListener {
            v , actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                val value = binding.servingSizeEditTextNumber.text.toString().toDoubleOrNull()
                if (value == null) {
                    Snackbar.make(v, "Invalid input: Not a number", Snackbar.LENGTH_LONG).show()
                } else {
                    nv.updateServingSize(binding.servingSizeEditTextNumber.text.toString().toDouble())
                    refreshNutritionalValues(nv)
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.addImageButton.setOnClickListener {
            app.consumedFoods.add(consumedFood)
            setResult(RESULT_OK)
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

    @SuppressLint("SetTextI18n")
    private fun refreshNutritionalValues(nv : NutritionalValues) {
        binding.caloriesEditTextNumber.text = String.format("%.1f", nv.caloriesInKcal) + " kcal"
        binding.carbsEditTextNumber.text = String.format("%.1f", nv.carbsInG) + " g"
        binding.proteinEditTextNumber.text = String.format("%.1f", nv.proteinInG) + " g"
        binding.fatEditTextNumber.text = String.format("%.1f", nv.fatInG) + " g"
    }

}