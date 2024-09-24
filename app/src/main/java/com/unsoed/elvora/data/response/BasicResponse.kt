package com.unsoed.elvora.data.response

import com.google.gson.annotations.SerializedName
import com.unsoed.elvora.data.response.Data

data class BasicResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
