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



        i("ALEX FoodTracker started")

    }

}