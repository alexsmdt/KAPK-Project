package oth.wit.kapk_project.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FoodModel(var brand : String = "",
                     var productName : String = "",
                     var productCategory : ProductCategory = ProductCategory.MISCELLANEOUS,
                     var barcode : String = "",
                     var nutritionalValues: NutritionalValues = NutritionalValues(ServingSize(), 0.0, 0.0, 0.0, 0.0),
                     var meal : MealType = MealType.OTHER,
                     var fbId : String = "") : Parcelable








