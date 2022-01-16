package oth.wit.kapk_project.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId() : Long {
    return lastId++
}

class FoodMemStore : FoodStore {
    val foods = ArrayList<FoodModel>()


    override fun add(food: FoodModel): Boolean {
        TODO("Not yet implemented")
    }

    override fun remove(food: FoodModel): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun listIterator(): MutableListIterator<FoodModel> {
        TODO("Not yet implemented")
    }

    override fun contains(food: FoodModel): Boolean {
        TODO("Not yet implemented")
    }

    override fun find(food: FoodModel): FoodModel? {
        TODO("Not yet implemented")
    }

    override fun count(): Int {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): FoodModel {
        TODO("Not yet implemented")
    }

    fun logAll() {
        foods.forEach{ i("$it") }
    }
}