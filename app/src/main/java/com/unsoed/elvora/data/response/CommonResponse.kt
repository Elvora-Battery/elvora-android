package com.unsoed.elvora.data.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @field:SerializedName("message")
    val message: String? = null,
)
