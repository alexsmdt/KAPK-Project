package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityMainMenuBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.MealType
import oth.wit.kapk_project.models.NutritionalValues
import oth.wit.kapk_project.models.ServingSize
import timber.log.Timber
import timber.log.Timber.i

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    lateinit var toogle : ActionBarDrawerToggle

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        i("ALEX MainMenuActivity.onCreate()")
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main_menu)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)


        toogle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toogle)

        toogle.syncState()

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarOverview.title = "Overview"
        setSupportActionBar(binding.toolbarOverview)

        val appSettingPrefs: SharedPreferences = getSharedPreferences("appSettingPrefs", 0)
        val sharedPrefsEdit : SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn : Boolean = appSettingPrefs.getBoolean("NightMode", false)

        /*
        val user : TextView = findViewById(R.id.user_name)
        user.text = FirebaseAuth.getInstance().currentUser?.email
        */

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainMenuActivity::class.java))
                R.id.logout ->  {
                    Firebase.auth.signOut()
                    val intent = Intent(this, GoogleLoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.mode -> {
                    changeMode(sharedPrefsEdit)
                }
            }
            true
        }
        //binding.toolbarOverview.title = "Today"
        //setSupportActionBar(binding.toolbarOverview)


        app = application as MainApp

        //Firebase.auth.signOut()

        setTextViews()

        binding.breakfastButton.setOnClickListener() {
            buttonPressed(MealType.BREAKFAST)
        }

        binding.lunchButton.setOnClickListener() {
            buttonPressed(MealType.LUNCH)
        }

        binding.dinnerButton.setOnClickListener() {
            buttonPressed(MealType.DINNER)
        }

        binding.snacksButton.setOnClickListener() {
            buttonPressed(MealType.SNACKS)
        }

        registerRefreshCallback()
    }

    private fun buttonPressed (meal : MealType) {
        i("ALEX MainMenuActivity go to MealListActivity")
        Timber.i("button pressed: %s", meal)
        var launcherIntent = Intent(this, MealListActivity::class.java)

        launcherIntent.putExtra("meal", meal.name)
        refreshIntentLauncher.launch(launcherIntent)
    }


    @SuppressLint("SetTextI18n")
    private fun setTextViews() {
        i("ALEX setTextViews")
        val nv = app.consumedFoods.sumNutritionalValues()
        binding.caloriesTextView.text = String.format("%.1f", nv.caloriesInKcal) + " kcal"
        binding.carbsTextNumber.setText(String.format("%.1f", nv.carbsInG) + " g")
        binding.proteinTextNumber.setText(String.format("%.1f", nv.proteinInG) + " g")
        binding.fatTextNumber.setText(String.format("%.1f", nv.fatInG) + " g")

        binding.carbsProgressBar.progress = nv.carbsInG.toInt()
        binding.proteinProgressBar.progress = nv.proteinInG.toInt()
        binding.fatProgressBar.progress = nv.fatInG.toInt()
        binding.caloriesProgressBar.progress = nv.caloriesInKcal.toInt()

        if (binding.caloriesProgressBar.progress > binding.caloriesProgressBar.max) {
            ColorStateList.valueOf(Color.RED)
                .also { binding.caloriesProgressBar.progressTintList = it }
        }
        binding.caloriesProgressBar
    }

    private fun registerRefreshCallback() {
        i("ALEX registerRefreshCallback")
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {setTextViews()}
    }

    private fun changeMode(sharedPrefsEdit : SharedPreferences.Editor) {
        i("ALEX change Mode before if")
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            i("ALEX no night mode")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            i("ALEX set Night mode yes")
            //sharedPrefsEdit.putBoolean("NightMode", true)
            i("ALEX sharedprefs")
            //sharedPrefsEdit.apply()
            i("ALEX sharedprefs apply")
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefsEdit.putBoolean("NightMode", false)
            sharedPrefsEdit.apply()
        }
    }


}