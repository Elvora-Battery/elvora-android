package com.unsoed.elvora.data.response.transaction

import com.google.gson.annotations.SerializedName

data class ActivityTransactionResponse(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

data class Token(

	@field:SerializedName("token")
	val token: String? = null
)

data class DataItem(

	@field:SerializedName("shipping_id")
	val shippingId: Int? = null,

	@field:SerializedName("battery_name")
	val batteryName: String? = null,

	@field:SerializedName("contract_file")
	val contractFile: String? = null,

	@field:SerializedName("rent_type_id")
	val rentTypeId: Int? = null,

	@field:SerializedName("expiration_date")
	val expirationDate: String? = null,

	@field:SerializedName("token")
	val token: Token? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("battery_id")
	val batteryId: Int? = null,

	@field:SerializedName("token_id")
	val tokenId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("payment")
	val payment: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
