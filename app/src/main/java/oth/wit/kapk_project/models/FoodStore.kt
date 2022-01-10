package oth.wit.kapk_project.models

interface FoodStore {
    fun findAll(): List<FoodModel>
    fun create(food: FoodModel)
    fun update(food: FoodModel)  //TODO("update for nutritions" in every implementation of this interface)
    fun delete(food: FoodModel)
    fun addNutritionalInformation(nutritionalValues: NutritionalValues, food: FoodModel)
}