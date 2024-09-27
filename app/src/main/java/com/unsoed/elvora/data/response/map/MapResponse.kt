package com.unsoed.elvora.data.response.map

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MapResponse(

	@field:SerializedName("stations")
	val stations: List<StationsItem>? = null
)

@Parcelize
data class StationsItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("distance")
	val distance: Double? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("station")
	val station: String? = null,

	@field:SerializedName("connections")
	val connections: List<ConnectionsItem>? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null,

	@field:SerializedName("status")
	val status: String? = null
): Parcelable

@Parcelize
data class ConnectionsItem(

	@field:SerializedName("power")
	val power: Int? = null,

	@field:SerializedName("type")
	val type: String? = null
): Parcelable
