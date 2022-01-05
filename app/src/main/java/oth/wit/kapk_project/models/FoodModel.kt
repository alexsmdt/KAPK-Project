package oth.wit.kapk_project.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodModel(var id: Long = 0,
                     var brand : String = "",
                     var productName : String= "",
                     var productCategory : String = "",
                     var barcode : String = "") : Parcelable
