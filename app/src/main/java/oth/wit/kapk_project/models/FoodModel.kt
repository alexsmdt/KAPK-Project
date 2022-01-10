package oth.wit.kapk_project.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
/*
@Parcelize
data class FoodModel(var id: Long = 0,
                     var brand : String = "",
                     var productName : String= "",
                     var productCategory : String = "",
                     var barcode : String = "") : Parcelable

 */


@Parcelize
data class FoodModel(var id: Long = 0,
                     var fbId: String = "",
                     var brand : String = "",
                     var productName : String= "",
                     var productCategory : String = "",
                     var barcode : String = "",
                     var nutritionalValues: NutritionalValues = NutritionalValues()) : Parcelable



@Parcelize
data class NutritionalValues(var servingSize : ServingSize = ServingSize(),
                             var calories : Int = 0,
                             var fat : Int = 0,
                             var carbs : Int = 0,
                             var protein : Int = 0) : Parcelable

@Parcelize
data class ServingSize(var amount : Int = 0,
                       var unit : String = "") : Parcelable
