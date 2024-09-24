package com.unsoed.elvora.data.response.map

import com.google.gson.annotations.SerializedName

data class MapResponse(

	@field:SerializedName("stations")
	val stations: List<StationsItem>? = null
)

data class StationsItem(

	@field:SerializedName("distance")
	val distance: Double? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("station")
	val station: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)
