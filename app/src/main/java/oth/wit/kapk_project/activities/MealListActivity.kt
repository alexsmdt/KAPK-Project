package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import oth.wit.kapk_project.R
import oth.wit.kapk_project.adapters.ConsumedFoodAdapter
import oth.wit.kapk_project.adapters.ConsumedFoodListener
import oth.wit.kapk_project.adapters.FoodAdapter
import oth.wit.kapk_project.adapters.FoodListener
import oth.wit.kapk_project.databinding.ActivityConsumedMealBinding
import oth.wit.kapk_project.databinding.ActivityFoodListBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.FoodStore
import oth.wit.kapk_project.models.MealType
import timber.log.Timber
import timber.log.Timber.i

class MealListActivity : AppCompatActivity(), ConsumedFoodListener {
    lateinit var app: MainApp
    private lateinit var binding : ActivityConsumedMealBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var meal : MealType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i("ALEX MealListActivity.onCreate()")
        binding = ActivityConsumedMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        meal = MealType.valueOf(intent.getStringExtra("meal").toString())
        binding.toolbar.title = meal.printableName
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadFoods()

        registerRefreshCallback()
        i("ALEX end of MealListActivity.onCreate()")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        i("ALEX MealListActivity.onCreateOptionsMenu()")
        menuInflater.inflate(R.menu.menu_main, menu)
        i("ALEX end of MealListActivity.onCreateOptionsMenu()")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        i("ALEX MealListActivity.onOptionsitemSelected()")
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, FoodListActivity::class.java)
                launcherIntent.putExtra("meal", meal.name)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        i("ALEX end of MealListActivity.onOptionsitemSelected()")
        return super.onOptionsItemSelected(item)
    }

    override fun onFoodClick(food: FoodModel) {
        i("ALEX MealListActivity.onFoodClick: $food")
        val launcherIntent = Intent(this, AddConsumedFoodActivity::class.java)
        launcherIntent.putExtra("food", food)
        launcherIntent.putExtra("meal", meal.name)
        launcherIntent.putExtra("edit", true)
        refreshIntentLauncher.launch(launcherIntent)
        i("ALEX end of MealListActivity.onFoodClick: $food")
    }


    private fun registerRefreshCallback() {
        i("ALEX MealListActivity.registerRefreshCallback()")
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadFoods() }
        i("ALEX end of MealListActivity.registerRefreshCallback()")
    }

    private fun loadFoods() {
        i("ALEX MealListActivity.loadFoods()")
        showFoods(app.consumedFoods.getMeal(meal))
        i("ALEX end of MealListActivity.loadFoods()")
    }

    fun showFoods (foods: MutableList<FoodModel>) {
        i("ALEX MealListActivity.showFoods()")
        binding.recyclerView.adapter = ConsumedFoodAdapter(foods, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("ALEX end of MealListActivity.showFoods()")
    }
}