package com.unsoed.elvora.data.response.home

import com.google.gson.annotations.SerializedName

data class DashboardResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Transaction(

	@field:SerializedName("token_id")
	val tokenId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("battery_name")
	val batteryName: String? = null,

	@field:SerializedName("rent_type_id")
	val rentTypeId: Int? = null,
)

data class Data(

	@field:SerializedName("battery")
	val battery: Battery? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("transaction")
	val transaction: Transaction? = null
)

data class Battery(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("arus")
	val arus: String? = null,

	@field:SerializedName("token_id")
	val tokenId: Int? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("suhu")
	val suhu: String? = null,

	@field:SerializedName("tegangan")
	val tegangan: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("daya")
	val daya: String? = null,

	@field:SerializedName("daya_digunakan")
	val dayaDigunakan: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class User(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
