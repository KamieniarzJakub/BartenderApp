package com.example.bartenderjetpack.model

data class DrinkCategory(
    val name: String,
    val description: String,
    val drinks: List<Drink>
)