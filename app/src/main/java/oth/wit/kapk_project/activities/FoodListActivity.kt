package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
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
import oth.wit.kapk_project.models.MealType
import timber.log.Timber.i
import java.util.*
import kotlin.collections.ArrayList


class FoodListActivity : AppCompatActivity(), FoodListener {
    lateinit var app: MainApp
    private lateinit var binding : ActivityFoodListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var meal : MealType
    lateinit var tempFoods : ArrayList<FoodModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        i("ALEX FoodListActivity.onCreate()")
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        meal = MealType.valueOf(intent.getStringExtra("meal").toString())
        binding.toolbarList.title = " Add to " + meal.printableName
        setSupportActionBar(binding.toolbarList)

        app = application as MainApp

        tempFoods = app.foods.toArrayList()

        i("ALEX create LayoutManager()")
        val layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = FoodAdapter(tempFoods,this)
        binding.recyclerView.layoutManager = layoutManager

        val searchView = binding.productSearch


        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                i("sucess")

                tempFoods.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){

                    app.foods.toArrayList().forEach{
                        if(it.productName.lowercase(Locale.getDefault()).contains(searchText)){
                            tempFoods.add(it)
                        }
                    }
                    binding.recyclerView.adapter?.notifyDataSetChanged()

                }else{

                    tempFoods.clear()
                    tempFoods.addAll(app.foods.toArrayList())
                    binding.recyclerView.adapter?.notifyDataSetChanged()

                }

                return false

            }

        })

        //loadFoods()

        registerRefreshCallback()
        i("ALEX finish onCreate()")
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
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onFoodClick(food: FoodModel) {
        i("ALEX FoodListActivity.onFoodClick: $food")
        val launcherIntent = Intent(this, AddConsumedFoodActivity::class.java)
        launcherIntent.putExtra("food", food)
        launcherIntent.putExtra("meal", meal.name)
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
        binding.recyclerView.adapter = FoodAdapter(foods.toArrayList(), this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}
