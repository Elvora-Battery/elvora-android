package com.unsoed.elvora.data.response.home

import com.google.gson.annotations.SerializedName

data class DashboardResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("battery")
	val battery: Battery? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("transaction")
	val transaction: Transaction? = null
)

data class User(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Transaction(

	@field:SerializedName("token_id")
	val tokenId: Int? = null,

	@field:SerializedName("battery_name")
	val batteryName: String? = null,

	@field:SerializedName("rent_type_id")
	val rentTypeId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Battery(

	@field:SerializedName("arus")
	val arus: Double? = null,

	@field:SerializedName("remainingTime")
	val remainingTime: Int? = null,

	@field:SerializedName("distanceTravelled")
	val distanceTravelled: Double? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("status_relay")
	val statusRelay: Boolean? = null,

	@field:SerializedName("daya")
	val daya: Double? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("token_id")
	val tokenId: Int? = null,

	@field:SerializedName("suhu")
	val suhu: Int? = null,

	@field:SerializedName("chargingStatus")
	val chargingStatus: String? = null,

	@field:SerializedName("tegangan")
	val tegangan: Double? = null,

	@field:SerializedName("batteryPercentage")
	val batteryPercentage: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("daya_digunakan")
	val dayaDigunakan: Double? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
