package oth.wit.kapk_project.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId() : Long {
    return lastId++
}

class FoodMemStore : FoodStore {
    val foods = ArrayList<FoodModel>()

    override fun findAll(): List<FoodModel> {
        return foods
    }

    override fun create(food: FoodModel) {
        food.id = getId()
        foods.add(food)
        logAll()
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
    }

    override fun delete(food: FoodModel) {
        foods.remove(food)
    }


    fun logAll() {
        foods.forEach{ i("$it") }
    }
}