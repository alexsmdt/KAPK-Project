package oth.wit.kapk_project.models

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import timber.log.Timber.i

class FoodFireStore(val context: Context) : FoodStore {

    //var foods = HashMap<String, FoodModel>()
    var foods = mutableListOf<FoodModel>()
    var userId: String = "alex@12345"
    //var db : DatabaseReference = Firebase.database.reference("foods")
    var db : DatabaseReference = FirebaseDatabase.getInstance("https://kapk-project-default-rtdb.europe-west1.firebasedatabase.app").getReference("foods")
    lateinit var st : StorageReference


    override fun findAll(): List<FoodModel> {

        val task = db.get().addOnSuccessListener {
            i("firebase Got value ${it.value}")
            val map = it.getValue<HashMap<String, FoodModel>>() as HashMap<String, FoodModel>
            foods = ArrayList(map.values).toMutableList()
            logAll()
        }.addOnFailureListener{
            i("firebase Error getting data")
        }
        return foods
    }

    //TODO("add method to interface")
     fun findById(id: Long): FoodModel? {
        return foods.find { p -> p.id == id }
    }

    fun findByFbId(fbId : String) : FoodModel? {
        var food : FoodModel? = null
        db.child(fbId).get().addOnSuccessListener {
            i("firebase Got value ${it.value}")
            food = it.getValue<FoodModel>() as FoodModel
        }.addOnFailureListener{
            i("firebase Error getting data")
        }
        return food
    }

    override fun create(food: FoodModel) {
        i("create new food")

        val key = db.push().key
        key?.let {
            food.fbId = key
            foods.add(food)
            db.child(key).setValue(food)
        }
        /*
        db.child(food.fbId).get().addOnSuccessListener {
            i("firebase Got value ${it.value}")
            food = it.getValue<FoodModel>() as FoodModel
        }.addOnFailureListener{
            i("firebase Error getting data")
        }

         */

    }


    override fun update(food: FoodModel) {
        val foundFood : FoodModel? = findByFbId(food.fbId)  //TODO("food.findById)
        if (foundFood != null) {
            foundFood.brand = food.brand
            foundFood.productName = food.productName
            foundFood.productCategory = food.productCategory
            foundFood.barcode = food.barcode
            logAll()
        }

        db.child(food.fbId).setValue(food)
    }

    override fun delete(food: FoodModel) {
        db.child(food.fbId).removeValue()
        foods.remove(food)
    }

    override fun addNutritionalInformation(nutritionalValues: NutritionalValues, food: FoodModel) {
        val foundFood: FoodModel? = foods.find { f -> f.id == food.id }
        if (foundFood != null) {
            foundFood.nutritionalValues = nutritionalValues
        }
    }

    private fun logAll() {
        foods.forEach { Timber.i("$it") }
    }

    //TODO("add to FoodModel interface instead of private fun override")
    private fun clear() {
        foods.clear()
    }

    fun createReference() {
        db = Firebase.database.reference
    }


}