package oth.wit.kapk_project.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import oth.wit.kapk_project.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "placemarks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<FoodModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class FoodJSONStore(private val context: Context) : FoodStore {

    var foods = mutableListOf<FoodModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<FoodModel> {
        logAll()
        return foods
    }

    override fun create(food: FoodModel) {
        food.id = generateRandomId()
        foods.add(food)
        serialize()
    }


    override fun update(food: FoodModel) {
        val foundFood: FoodModel? = foods.find { f -> f.id == food.id }
        if (foundFood != null) {
            foundFood.brand = food.brand
            foundFood.productName = food.productName
            foundFood.productCategory = food.productCategory
            foundFood.barcode = food.barcode
            logAll()
        }
        serialize()
    }

    override fun delete(food: FoodModel) {
        foods.remove(food)
        serialize()
    }

    override fun addNutritionalInformation(nutritionalValues: NutritionalValues, food: FoodModel) {
        val foundFood: FoodModel? = foods.find { f -> f.id == food.id }
        if (foundFood != null) {
            foundFood.nutritionalValues = nutritionalValues
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(foods, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        foods = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        foods.forEach { Timber.i("$it") }
    }

}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}