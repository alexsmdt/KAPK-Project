package oth.wit.kapk_project.models

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import oth.wit.kapk_project.helpers.exists
import timber.log.Timber
import timber.log.Timber.i

class FireFoodStore(private val context: Context, branch : String, child : String?) : FoodStore{
    //<string name="default_web_client_id">1040330250217-50o0gq96734gabocrkvaor5l60goboh2.apps.googleusercontent.com</string>
    var foods = mutableListOf<FoodModel>()

    var db : DatabaseReference = FirebaseDatabase.getInstance("https://kapk-project-default-rtdb.europe-west1.firebasedatabase.app").getReference(branch)

    var task : Task<DataSnapshot>

    private var listeners = ArrayList<FoodStoreChangeListener>()

    init {

        if(child != null)
            db = db.child(child)

        task = db.get()

        task.addOnSuccessListener {
            i("ALEX firebase Got value ${it.value}")
            if (it.value != null) {
                val map: HashMap<String, FoodModel>?
                map =
                    it.getValue<HashMap<String, FoodModel>?>() as HashMap<String, FoodModel>
                foods = ArrayList(map.values).toMutableList()
                foods.sortBy{
                    it.brand
                }

            }
        }.addOnFailureListener {
            i("ALEX firebase Error getting data ${it.message}")
        }

    }

    override fun addFoodStoreChangeListener(listener : FoodStoreChangeListener ) {
        listeners.add(listener)
    }

    override fun removeFoodStoreChangeListener(listener : FoodStoreChangeListener ) {
        listeners.remove(listener)
    }

    private fun fireFoodStoreChangeListeners() {
        for ( listener in listeners )
            listener.onFoodStoreChange()
    }
    fun getQueryTask() : Task<DataSnapshot> {
        return task
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

    override fun add(food: FoodModel): Boolean {
        val key = db.push().key
        key?.let {
            food.fbId = key
            db.child(key).setValue(food)
        }
        val ret = foods.add(food)
        foods.sortBy{
            it.brand
        }
        fireFoodStoreChangeListeners()
        return ret
    }

    override fun remove(food: FoodModel): Boolean {
        db.child(food.fbId).removeValue()
        val ret = foods.remove(food)
        foods.sortBy{
            it.brand
        }
        fireFoodStoreChangeListeners()
        return ret
    }

    override fun replace(oldFoodModel: FoodModel, newFoodModel: FoodModel): Boolean {
        db.child(oldFoodModel.fbId).setValue(newFoodModel)
        foods.removeIf{ f -> f.fbId == oldFoodModel.fbId }
        val ret = foods.add(newFoodModel)
        foods.sortBy{
            it.brand
        }
        fireFoodStoreChangeListeners()
        return ret
    }

    override fun clear() {
        db.removeValue()
        foods.clear()
        fireFoodStoreChangeListeners()
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

    override fun toArrayList(): ArrayList<FoodModel> {
        return ArrayList<FoodModel>(foods)
    }
}
