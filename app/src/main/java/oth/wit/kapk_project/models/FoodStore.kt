package oth.wit.kapk_project.models


interface FoodStore {

    fun add(food : FoodModel) : Boolean
    fun remove(food : FoodModel) : Boolean
    fun clear()
    fun listIterator() : MutableListIterator<FoodModel>
    fun contains(food : FoodModel) : Boolean
    fun find(food : FoodModel) : FoodModel?
    fun count() : Int
    operator fun get(index : Int) : FoodModel

    public fun sumNutritionalValues() : NutritionalValues {
        var nv = NutritionalValues(ServingSize(),0.0,0.0,0.0,0.0)
        for (food in listIterator()) {
            nv.caloriesInKcal += food.nutritionalValues.caloriesInKcal
            nv.carbsInG += food.nutritionalValues.carbsInG
            nv.fatInG += food.nutritionalValues.fatInG
            nv.proteinInG += food.nutritionalValues.proteinInG
        }
        return nv
    }

}