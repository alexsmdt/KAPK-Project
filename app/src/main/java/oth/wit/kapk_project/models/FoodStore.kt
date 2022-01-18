package oth.wit.kapk_project.models

import timber.log.Timber.i


interface FoodStore {

    fun add(food : FoodModel) : Boolean
    fun remove(food : FoodModel) : Boolean
    fun clear()
    fun listIterator() : MutableListIterator<FoodModel>
    fun contains(food : FoodModel) : Boolean
    fun find(food : FoodModel) : FoodModel?
    fun count() : Int
    fun replace(oldFood: FoodModel, newFoodModel: FoodModel) : Boolean
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


    fun getMeal(meal: MealType) : MutableList<FoodModel> {
        i("ALEX getMeal($meal)")
        val list = mutableListOf<FoodModel>()
        for (food in listIterator()) {
            if (food.meal == meal)
                list.add(food)
        }
        return list
    }

}