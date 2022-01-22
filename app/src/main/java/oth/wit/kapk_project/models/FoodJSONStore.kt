package oth.wit.kapk_project.models

import UriParser
import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import oth.wit.kapk_project.helpers.*
import timber.log.Timber.i
import java.lang.reflect.Type
import java.util.*

val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<FoodModel>>() {}.type


class FoodJSONStore(private val context: Context, fileName : String) : FoodStore {

    private val jsonFile = "$fileName.json"

    var foods = mutableListOf<FoodModel>()

    init {
        if (exists(context, jsonFile)) {
            deserialize()
        }
    }

    override fun add(food: FoodModel): Boolean {
        i("ALEX FoodJSONStore($jsonFile): add $food")
        val ret = foods.add(food)
        if (ret)
            serialize()
        return ret
    }

    override fun remove(food: FoodModel): Boolean {
        val ret = foods.remove(food)
        if (ret)
            serialize()
        return ret
    }

    override fun replace(oldFoodModel: FoodModel, newFoodModel: FoodModel): Boolean {
        if (remove(oldFoodModel))
            i("ALEX Food removed")
        else
            i("ALEX Food not removed $oldFoodModel")
        return add(newFoodModel)
    }

    override fun clear() {
        foods.clear()
        serialize()
    }

    override fun addFoodStoreChangeListener(listener: FoodStoreChangeListener) {
        TODO("Not yet implemented")
    }

    override fun removeFoodStoreChangeListener(listener: FoodStoreChangeListener) {
        TODO("Not yet implemented")
    }

    override fun contains(food: FoodModel): Boolean {
        return foods.contains(food)
    }

    override fun find(food: FoodModel): FoodModel? {
        return foods.find{f -> f == food}
    }

    override fun listIterator(): MutableListIterator<FoodModel> {
        return foods.listIterator()
    }

    override fun count(): Int {
        return foods.count()
    }

    override fun get(index: Int) : FoodModel{
        return foods[index]
    }

    private fun serialize() {
        i("ALEX FoodJSONStore($jsonFile): serialize")
        val jsonString = gsonBuilder.toJson(foods, listType)
        write(context, jsonFile, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, jsonFile)
        foods = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        foods.forEach { i("$it") }
    }

    override fun toArrayList(): ArrayList<FoodModel> {
        return ArrayList<FoodModel>(foods)
    }

}
