package oth.wit.kapk_project.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import oth.wit.kapk_project.models.ServingSize

@Parcelize
data class NutritionalValues(var servingSize : ServingSize = ServingSize(),
                             var caloriesInKcal : Double = 0.0,
                             var fatInG : Double = 0.0,
                             var carbsInG : Double = 0.0,
                             var proteinInG : Double = 0.0) : Parcelable
{
    fun updateServingSize(amount : Double) {
        val factor = amount / servingSize.amount
        caloriesInKcal *= factor
        fatInG *= factor
        carbsInG *= factor
        proteinInG *= factor
        servingSize.amount = amount
    }
}

