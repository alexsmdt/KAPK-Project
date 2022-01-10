package oth.wit.kapk_project.main

import android.app.Application
import oth.wit.kapk_project.models.FoodFireStore
import oth.wit.kapk_project.models.FoodJSONStore
import oth.wit.kapk_project.models.FoodMemStore
import oth.wit.kapk_project.models.FoodStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var foods : FoodStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //foods = FoodMemStore()
        //foods = FoodJSONStore(applicationContext)
        foods = FoodFireStore(applicationContext)
        i("Placemark started")
    }

}