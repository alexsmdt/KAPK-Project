package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityAddConsumedFoodBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.NutritionalValues
import timber.log.Timber.i

class AddConsumedFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddConsumedFoodBinding
    lateinit var food : FoodModel
    lateinit var consumedFood : FoodModel
    lateinit var app: MainApp

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        i("ALEX AddConsumedFoodsActivity started")

        binding = ActivityAddConsumedFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = intent.getStringExtra("meal")
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        food = intent.getParcelableExtra<FoodModel>("food")!!

        consumedFood = food.copy()

        i("ALEX $food")

        var nv = consumedFood.nutritionalValues
        var sz = nv.servingSize

        binding.brandTextView.text = consumedFood.brand
        binding.nameTextView.text = consumedFood.productName

        binding.servingSizeEditTextNumber.setText("${sz.amount}")
        binding.unitTextView.text = sz.unit.printableName

        refreshNutritionalValues(nv)

        binding.servingSizeEditTextNumber.setOnEditorActionListener {
            v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                nv.updateServingSize(binding.servingSizeEditTextNumber.text.toString().toDouble())
                refreshNutritionalValues(nv)
                true
            } else {
                false
            }
        }

        binding.button.setOnClickListener() {
            var amount = binding.servingSizeEditTextNumber.text.toString().toDouble()
            // kalorien, carbs, proteine und fat mit faktor multiplizieren
            //app.consumedFoods.changeNutritionalInformation(factor, food.id)
            // food item zu FoodList der konsumierten Produkte hinzufügen
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
        binding.caloriesEditTextNumber.text = "${nv.caloriesInKcal} kcal"
        binding.carbsEditTextNumber.text = "${nv.carbsInG} g"
        binding.proteinEditTextNumber.text = "${nv.proteinInG} g"
        binding.fatEditTextNumber.text = "${nv.fatInG} g"
    }

}