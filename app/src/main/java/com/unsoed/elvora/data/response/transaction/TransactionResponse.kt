package com.unsoed.elvora.data.response.transaction

import com.google.gson.annotations.SerializedName
import com.unsoed.elvora.data.response.Data

data class TransactionResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

data class Shipping(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("full_address")
	val fullAddress: String? = null,

	@field:SerializedName("village")
	val village: String? = null,

	@field:SerializedName("street_name")
	val streetName: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Data(

	@field:SerializedName("rent_type")
	val rentType: RentType? = null,

	@field:SerializedName("shipping")
	val shipping: Shipping? = null
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
