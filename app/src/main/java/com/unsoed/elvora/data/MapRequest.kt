package com.unsoed.elvora.data

data class MapRequest(
    val location: Location
)

data class Location(
    val latitude: Double,
    val longitude: Double
)
