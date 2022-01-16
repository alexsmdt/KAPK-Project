package oth.wit.kapk_project.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServingSize(var amount : Double = 100.0,
                       var unit : UnitSpecification = UnitSpecification.GRAM) : Parcelable
{
    override fun toString(): String {
        return amount.toString() + " " + unit.printableName
    }
}
