package com.unsoed.elvora.data.response.new

import com.google.gson.annotations.SerializedName

data class NewTransactionResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

data class Data(

	@field:SerializedName("shipping_id")
	val shippingId: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("battery_name")
	val batteryName: String? = null,

	@field:SerializedName("rent_type_id")
	val rentTypeId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("expiration_date")
	val expirationDate: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
