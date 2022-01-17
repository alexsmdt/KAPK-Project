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
import oth.wit.kapk_project.adapters.FoodAdapter
import oth.wit.kapk_project.adapters.FoodListener
import oth.wit.kapk_project.databinding.ActivityFoodListBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.FoodStore
import timber.log.Timber.i


class FoodListActivity : AppCompatActivity(), FoodListener {
    lateinit var app: MainApp
    private lateinit var binding : ActivityFoodListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var meal : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        meal = intent.getStringExtra("meal").toString()
        binding.toolbar.title = meal
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadFoods()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, FoodCreateActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        when (item.itemId) {
            R.id.item_back -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onFoodClick(food: FoodModel) {
        i("ALEX FoodListActivity.onFoodClick: $food")
        val launcherIntent = Intent(this, AddConsumedFoodActivity::class.java)
        launcherIntent.putExtra("food", food)
        launcherIntent.putExtra("meal", meal)
        startActivity(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadFoods() }
    }

    private fun loadFoods() {
        showFoods(app.foods)
    }

    fun showFoods (foods: FoodStore) {
        binding.recyclerView.adapter = FoodAdapter(foods, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}
