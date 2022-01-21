package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import oth.wit.kapk_project.R
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import timber.log.Timber.i


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var appname : TextView
    lateinit var lottie : LottieAnimationView
    lateinit var app: MainApp
    //var db : DatabaseReference = FirebaseDatabase.getInstance("https://kapk-project-default-rtdb.europe-west1.firebasedatabase.app").reference
    lateinit var foods : MutableList<FoodModel>
    lateinit var consumedFoods : MutableList<FoodModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        i("ALEX SplashScreenActivity.onCreate()")
        appname = findViewById(R.id.appname)
        lottie = findViewById(R.id.lottie)

        app = application as MainApp

        appname.animate().translationY(-1700F).setDuration(2700).startDelay = 0
        //lottie.animate().translationX(2000F).setDuration(2000).startDelay = 2900

        //loadDatabase()

        Handler(Looper.getMainLooper()).postDelayed({
            i("ALEX SplashScreenActivity go to GoogleLoginActivity")
            val launcherIntent = Intent(this, GoogleLoginActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)
            finish()
        }, 5000)

        registerRefreshCallback()
    }

    private fun registerRefreshCallback() {
        i("ALEX SplashScreenActivity.registerRefreshCallback()")
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
    /*
    private fun loadDatabase() {
        db.child("consumedFoods").get().addOnSuccessListener {
            i("firebase Got value ${it.value}")
            val map = it.getValue<HashMap<String, FoodModel>>() as HashMap<String, FoodModel>
            consumedFoods = ArrayList(map.values).toMutableList()
        }.addOnFailureListener{
            i("firebase Error getting data")
        }

        db.child("consumedFoods").get().addOnSuccessListener {
            i("firebase Got value ${it.value}")
            val map = it.getValue<HashMap<String, FoodModel>>() as HashMap<String, FoodModel>
            foods = ArrayList(map.values).toMutableList()
            Handler(Looper.getMainLooper()).postDelayed({
                val launcherIntent = Intent(this, MainMenuActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }, 1000)
        }.addOnFailureListener{
            i("firebase Error getting data")
        }
       }

     */



}