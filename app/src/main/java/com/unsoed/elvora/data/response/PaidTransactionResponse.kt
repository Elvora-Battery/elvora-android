package com.unsoed.elvora.data.response

import com.google.gson.annotations.SerializedName

data class PaidTransactionResponse(

	@field:SerializedName("success")
	val success: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
