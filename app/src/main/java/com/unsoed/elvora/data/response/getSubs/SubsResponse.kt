package com.unsoed.elvora.data.response.getSubs

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SubsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

@Parcelize
data class AllSubsriptionsItem(

	@field:SerializedName("shipping_id")
	val shippingId: Int? = null,

	@field:SerializedName("rent_type")
	val rentType: RentType? = null,

	@field:SerializedName("battery_name")
	val batteryName: String? = null,

	@field:SerializedName("contract_file")
	val contractFile: String? = null,

	@field:SerializedName("rent_type_id")
	val rentTypeId: Int? = null,

	@field:SerializedName("expiration_date")
	val expirationDate: String? = null,

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
): Parcelable

@Parcelize
data class ActiveSubscription(

	@field:SerializedName("shipping_id")
	val shippingId: Int? = null,

	@field:SerializedName("rent_type")
	val rentType: RentType? = null,

	@field:SerializedName("battery_name")
	val batteryName: String? = null,

	@field:SerializedName("contract_file")
	val contractFile: String? = null,

	@field:SerializedName("rent_type_id")
	val rentTypeId: Int? = null,

	@field:SerializedName("expiration_date")
	val expirationDate: String? = null,

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
): Parcelable

@Parcelize
data class RentType(
	@field:SerializedName("price")
	val price: Int? = null
): Parcelable

data class Data(

	@field:SerializedName("allSubsriptions")
	val allSubsriptions: List<AllSubsriptionsItem>? = null,

	@field:SerializedName("activeSubscription")
	val activeSubscription: ActiveSubscription? = null
)
