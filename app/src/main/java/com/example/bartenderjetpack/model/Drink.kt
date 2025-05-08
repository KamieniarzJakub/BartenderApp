package com.example.bartenderjetpack.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drink(val name: String, val ingredients: String, val recipe: String) : Parcelable