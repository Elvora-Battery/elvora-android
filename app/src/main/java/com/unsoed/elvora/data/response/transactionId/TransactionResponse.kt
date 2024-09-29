package com.unsoed.elvora.data.response.transactionId

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

data class Token(
	@field:SerializedName("token")
	val token: String? = null
)

data class RentType(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("rent_period")
	val rentPeriod: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("capacity")
	val capacity: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Data(

	@field:SerializedName("rent_type")
	val rentType: RentType? = null,

	@field:SerializedName("transaction")
	val transaction: Transaction? = null,

	@field:SerializedName("token_verify")
	val tokenVerify: Boolean? = null
)

data class Transaction(

	@field:SerializedName("shipping_id")
	val shippingId: Any? = null,

	@field:SerializedName("battery_name")
	val batteryName: String? = null,

	@field:SerializedName("contract_file")
	val contractFile: Any? = null,

	@field:SerializedName("rent_type_id")
	val rentTypeId: Int? = null,

	@field:SerializedName("expiration_date")
	val expirationDate: String? = null,

	@field:SerializedName("token")
	val token: Token? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

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
