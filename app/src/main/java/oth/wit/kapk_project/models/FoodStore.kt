package oth.wit.kapk_project.models

interface FoodStore {
    fun findAll(): List<FoodModel>
    fun create(food: FoodModel)
    fun update(food: FoodModel)
    fun delete(food: FoodModel)
}