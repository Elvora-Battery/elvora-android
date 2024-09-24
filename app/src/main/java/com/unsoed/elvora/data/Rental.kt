package com.unsoed.elvora.data

data class Rental(
    val id: Int,
    val price: String,
    val priceInt: Double,
    val type: String,
    val capacity: Int,
    val description: String
)
