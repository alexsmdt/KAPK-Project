package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import oth.wit.kapk_project.R
import oth.wit.kapk_project.adapters.FoodAdapter
import oth.wit.kapk_project.adapters.FoodListener
import oth.wit.kapk_project.databinding.ActivityFoodListBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import timber.log.Timber


class FoodListActivity : AppCompatActivity(), FoodListener {
    lateinit var app: MainApp
    private lateinit var binding : ActivityFoodListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    var db : DatabaseReference = FirebaseDatabase.getInstance("https://kapk-project-default-rtdb.europe-west1.firebasedatabase.app").getReference("foods")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadFoods()

        registerRefreshCallback()

        setResult(RESULT_OK)
        finish()
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
        return super.onOptionsItemSelected(item)
    }

    //Wieso on PlacemarkClick()?
    override fun onPlacemarkClick(food: FoodModel) {
        val launcherIntent = Intent(this, FoodAddActivity::class.java)
        //launcherIntent.putExtra("food_edit", food)
        launcherIntent.putExtra("food", food)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadFoods() }
    }

    private fun loadFoods() {
        /*
        db.get().addOnSuccessListener {
            Timber.i("firebase Got value ${it.value}")
            val map = it.getValue<HashMap<String, FoodModel>>() as HashMap<String, FoodModel>
            //app.foods.foods = ArrayList(map.values).toMutableList()
            showFoods(ArrayList(map.values).toMutableList())
        }.addOnFailureListener{
            Timber.i("firebase Error getting data")
        }

         */
        showFoods(app.foods.findAll())
    }

    fun showFoods (foods: List<FoodModel>) {
        binding.recyclerView.adapter = FoodAdapter(foods, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}
