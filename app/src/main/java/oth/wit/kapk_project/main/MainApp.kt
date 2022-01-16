package oth.wit.kapk_project.main

import android.app.Application
import oth.wit.kapk_project.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var foods : FoodStore
    lateinit var consumedFoods : FoodStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //foods = FoodMemStore()
        foods = FoodJSONStore(applicationContext, "foods")
        /*
        foods.clear()
        foods.add(FoodModel(0, "Red Bull", "Sugarfree", "Energydrink","90162480",NutritionalValues(
            ServingSize(),3.0,0.1,0.2,0.2)))
        foods.add(FoodModel(0, "Barilla", "Penne", "Nudeln","none",NutritionalValues(
            ServingSize(),230.0,15.0,34.0,3.0)))

         */

        consumedFoods = FoodJSONStore(applicationContext, "consumedFoods")

        /*consumedFoods.add(FoodModel(0, "Bei Muttern", "Brot", "Gebäck","none",NutritionalValues(
            ServingSize(),130.0,5.0,4.0,3.0)))*/


        i("ALEX Placemark started")
    }

}