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

    fun logAll() {
        foods.forEach{ i("$it") }
    }
}