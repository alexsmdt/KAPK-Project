package oth.wit.kapk_project.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import oth.wit.kapk_project.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var foods : FoodStore
    lateinit var consumedFoods : FoodStore

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Timber.plant(Timber.DebugTree())
        //foods = FoodMemStore()
        //foods = FoodJSONStore(applicationContext, "foods")
        foods = FireFoodStore(applicationContext, "foods")
        //foods.clear()

        //consumedFoods = FoodJSONStore(applicationContext, "consumedFoods")
        consumedFoods = FireFoodStore(applicationContext, "consumedFoods")
        //consumedFoods.clear()

        i("ALEX Placemark started")
    }

}