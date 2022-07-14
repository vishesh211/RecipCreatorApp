package com.example.recipecreatorapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    val user_id:String = "",
    val user_name:String = "",
    val title:String = "",
    val category:String = "",
    val recipeProcess:String = "",
    val image:String = "",
    var recipe_id:String = "",
): Parcelable
